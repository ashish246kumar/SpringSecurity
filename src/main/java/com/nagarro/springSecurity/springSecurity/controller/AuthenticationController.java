package com.nagarro.springSecurity.springSecurity.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nagarro.springSecurity.springSecurity.dto.JwtAuthenticationResponse;
import com.nagarro.springSecurity.springSecurity.dto.RefreshTokenRequest;
import com.nagarro.springSecurity.springSecurity.dto.SignInRequest;
import com.nagarro.springSecurity.springSecurity.dto.SignUpRequest;
import com.nagarro.springSecurity.springSecurity.entity.User;
import com.nagarro.springSecurity.springSecurity.service.impl.AuthenticationServiceImpl;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthenticationController {

	@Autowired
	private AuthenticationServiceImpl authenticationServiceImpl;
	
	@PostMapping("/signUp")
	public ResponseEntity<User>signUp(@RequestBody SignUpRequest signUpRequest){
		return ResponseEntity.ok(authenticationServiceImpl.signUp(signUpRequest));
	}
	@PostMapping("/signIn")
	public ResponseEntity<JwtAuthenticationResponse>signIn(@RequestBody SignInRequest signInRequest){
		return ResponseEntity.ok(authenticationServiceImpl.signIn(signInRequest));
	}
	@PostMapping("/refresh")
	public ResponseEntity<JwtAuthenticationResponse>refresh(@RequestBody RefreshTokenRequest refreshTokenRequest){
		return ResponseEntity.ok(authenticationServiceImpl.refreshToken(refreshTokenRequest));
	}
}
