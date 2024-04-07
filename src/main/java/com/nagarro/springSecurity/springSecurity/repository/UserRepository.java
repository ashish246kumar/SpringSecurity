package com.nagarro.springSecurity.springSecurity.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

import com.nagarro.springSecurity.springSecurity.entity.Role;
import com.nagarro.springSecurity.springSecurity.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User,Integer>{

	Optional<UserDetails> findByEmail(String username);
	User findByRole(Role role);
	
}
