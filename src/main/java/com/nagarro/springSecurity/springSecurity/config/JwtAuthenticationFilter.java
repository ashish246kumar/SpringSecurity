package com.nagarro.springSecurity.springSecurity.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
//import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import com.nagarro.springSecurity.springSecurity.service.impl.JwtServiceImpl;
import com.nagarro.springSecurity.springSecurity.service.impl.UserServiceImpl;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter{

	
	@Autowired
    private UserServiceImpl userServiceImpl;
	@Autowired
	private  JwtServiceImpl jwtServiceImpl;
	@Override
	protected void doFilterInternal(HttpServletRequest req,HttpServletResponse res,FilterChain filterChain) throws IOException, ServletException {
		 String authHeader = req.getHeader("Authorization");
	        String jwt = null;
	        String userEmail = null;
	        if(StringUtils.isEmpty(authHeader)||!org.apache.commons.lang3.StringUtils.startsWith(authHeader, "Bearer ")){
	        	filterChain.doFilter(req, res);
	        	return;
	        }
	    jwt=authHeader.substring(7);
	    userEmail=jwtServiceImpl.extractUsername(jwt);
	    if(StringUtils.isNotEmpty(userEmail)&&SecurityContextHolder.getContext().getAuthentication() == null) {
	    	 UserDetails userDetails = userServiceImpl.userDetailsService().loadUserByUsername(userEmail);
	    	 if(jwtServiceImpl.isTokenValid(jwt,userDetails)) {
	    		 SecurityContext securityContext=SecurityContextHolder.createEmptyContext();
	    		 UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
	    		 token.setDetails(new WebAuthenticationDetailsSource().buildDetails(req));
	    		 securityContext.setAuthentication(token);
	    		 SecurityContextHolder.setContext(securityContext);
	    		 
	    	 }
	    	 
	    }
	    filterChain.doFilter(req, res);
	    
	    
	}
}
