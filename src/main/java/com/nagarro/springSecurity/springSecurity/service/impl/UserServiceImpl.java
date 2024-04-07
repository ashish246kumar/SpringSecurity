package com.nagarro.springSecurity.springSecurity.service.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.nagarro.springSecurity.springSecurity.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
public class UserServiceImpl {

	@Autowired
	private  UserRepository userRepository;
	
	public UserDetailsService userDetailsService() {
	    return new UserDetailsService() {
	    	public UserDetails loadUserByUsername(String username) {
	    	    Optional<UserDetails> userOptional = userRepository.findByEmail(username);
	    	    
	    	    if (userOptional.isPresent()) {
	    	        return userOptional.get();
	    	    } else {
	    	        throw new UsernameNotFoundException("User not found with username: " + username);
	    	    }
	    	}
	    };
	}
	
}
