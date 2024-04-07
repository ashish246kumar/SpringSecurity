package com.nagarro.springSecurity.springSecurity.service.impl;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

import java.security.Key;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtServiceImpl {

	public static final String SECRET = "5367566B59703373367639792F423F4528482B4D6251655468576D5A71347437";

	public String geneRateRefreshToken(Map<String,Object>extractClaim,UserDetails userDetails) {
		return Jwts.builder()
		        .setClaims(extractClaim)
		        .setSubject(userDetails.getUsername())
		        .setIssuedAt(new Date(System.currentTimeMillis()))
		        .setExpiration(new Date(System.currentTimeMillis()+1000*60*30))
		        .signWith(getSignKey(), SignatureAlgorithm.HS256).compact();
	}
	
	public String generateToken(UserDetails userDetails) {
		return Jwts.builder()
//        .setClaims(claims)
        .setSubject(userDetails.getUsername())
        .setIssuedAt(new Date(System.currentTimeMillis()))
        .setExpiration(new Date(System.currentTimeMillis()+1000*60*30*7))
        .signWith(getSignKey(), SignatureAlgorithm.HS256).compact();
	}
	private  Key getSignKey() {
        byte[] key= Decoders.BASE64.decode(SECRET);
        return Keys.hmacShaKeyFor(key);
    }
	 public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
	        final Claims claims = extractAllClaims(token);
	        return claimsResolver.apply(claims);
	    }
	 private Claims extractAllClaims(String token) {
	        return Jwts
	                .parserBuilder()
	                .setSigningKey(getSignKey())
	                .build()
	                .parseClaimsJws(token)
	                .getBody();
	    }
	 public String extractUsername(String token) {
	        return extractClaim(token, Claims::getSubject);
	    }
	 public Boolean isTokenExpired(String token) {
		    return extractClaim(token,Claims::getExpiration).before(new Date());
	        
	    }
	public boolean isTokenValid(String token,UserDetails userDetails) {
		final String userName=extractUsername(token);
		return (userName.equals(userDetails.getUsername()) && !isTokenExpired(token));
	}

	 

}
