package com.nagarro.springSecurity.springSecurity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.nagarro.springSecurity.springSecurity.entity.Role;
import com.nagarro.springSecurity.springSecurity.entity.User;
import com.nagarro.springSecurity.springSecurity.repository.UserRepository;


@SpringBootApplication
public class SpringSecurityApplication implements CommandLineRunner{
	
	@Autowired
	private UserRepository userRepository;
	
	

	public static void main(String[] args) {
		SpringApplication.run(SpringSecurityApplication.class, args);
	}

public void run(String... args) {
		
		User adminAccount=userRepository.findByRole(Role.ADMIN);
		if(adminAccount==null) {
			User user=new User();
			user.setEmail("admin@gmail.com");
			user.setFirstName("ak");
			user.setLastName("Kumar");
			user.setRole(Role.ADMIN);
			user.setPassword(new BCryptPasswordEncoder().encode("Admin"));
			userRepository.save(user);
			
		}
		else {
			System.out.println("******************************"+adminAccount);
		}
	}
}
