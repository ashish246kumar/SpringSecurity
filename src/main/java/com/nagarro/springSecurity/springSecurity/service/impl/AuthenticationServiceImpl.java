package com.nagarro.springSecurity.springSecurity.service.impl;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.nagarro.springSecurity.springSecurity.dto.JwtAuthenticationResponse;
import com.nagarro.springSecurity.springSecurity.dto.RefreshTokenRequest;
import com.nagarro.springSecurity.springSecurity.dto.SignInRequest;
import com.nagarro.springSecurity.springSecurity.dto.SignUpRequest;
import com.nagarro.springSecurity.springSecurity.entity.Role;
import com.nagarro.springSecurity.springSecurity.entity.User;
import com.nagarro.springSecurity.springSecurity.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.var;

@Service
public class AuthenticationServiceImpl {

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private AuthenticationManager authenticationManager; 
	
	@Autowired
	private JwtServiceImpl jwtServiceImpl;
	
	public User signUp(SignUpRequest signUpRequest) {
		User user=new User();
		user.setEmail(signUpRequest.getEmail());
		user.setFirstName(signUpRequest.getFirstName());
		user.setLastName(signUpRequest.getLastName());
		user.setPassword(signUpRequest.getPassword());
		user.setRole(Role.USER);
		return userRepository.save(user);
		
	}
	public JwtAuthenticationResponse signIn(SignInRequest signInRequest) {
		authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(signInRequest.getEmail(),
				signInRequest.getPassword()
				));
		var user=userRepository.findByEmail(signInRequest.getEmail())
				.orElseThrow(()->new IllegalArgumentException("Invalid Email"));
		var jwt=jwtServiceImpl.generateToken(user);
		var refreshToken=jwtServiceImpl.geneRateRefreshToken(new HashMap<>(),user);
		JwtAuthenticationResponse jwtAuthenticationResponse=new JwtAuthenticationResponse();
		jwtAuthenticationResponse.setToken(jwt);
		jwtAuthenticationResponse.setRefreshToken(refreshToken);
		return jwtAuthenticationResponse;
		
	}
	public JwtAuthenticationResponse refreshToken(RefreshTokenRequest refreshTokenRequest) {
		
		String userEmail=jwtServiceImpl.extractUsername(refreshTokenRequest.getToken());
		User user=(User) userRepository.findByEmail(userEmail).orElseThrow();
		if(jwtServiceImpl.isTokenValid(refreshTokenRequest.getToken(),user)) {
			var jwt=jwtServiceImpl.generateToken(user);
			JwtAuthenticationResponse jwtAuthenticationResponse=new JwtAuthenticationResponse();
			jwtAuthenticationResponse.setToken(jwt);
			jwtAuthenticationResponse.setRefreshToken(refreshTokenRequest.getToken());
			return jwtAuthenticationResponse;
		}
		return null;
	}
	
}
