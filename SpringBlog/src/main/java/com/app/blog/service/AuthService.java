package com.app.blog.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.app.blog.dto.AuthenticationResponse;
import com.app.blog.dto.LoginRequest;
import com.app.blog.dto.RegisterRequest;
import com.app.blog.model.User;
import com.app.blog.repository.UserRepo;
import com.app.blog.security.JwtProvider;

@Service
public class AuthService {
	
	@Autowired
	private UserRepo userRepo;
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private JwtProvider jwtProvider;
	
	public void signup(RegisterRequest registerRequest) {
		User user =new User();
		user.setUsername(registerRequest.getUsername());
		user.setPassword(encodePassword(registerRequest.getPassword()));
		user.setEmail(registerRequest.getEmail());
		userRepo.save(user);
	}
	
	
	public AuthenticationResponse login(LoginRequest loginRequest) {
		AuthenticationResponse authResponse=new AuthenticationResponse();
			Authentication authenticate=authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(),
					loginRequest.getPassword()));
		SecurityContextHolder.getContext().setAuthentication(authenticate);
		authResponse.setToken(jwtProvider.generateToken(authenticate));
		org.springframework.security.core.userdetails.User user=getCurrentUser().orElseThrow(()->
		new IllegalArgumentException("No User logged in"));;
		authResponse.setUsername(user.getUsername());
		return authResponse;
	}
	
	public  Optional<org.springframework.security.core.userdetails.User> getCurrentUser() {
		org.springframework.security.core.userdetails.User principal=(org.springframework.security.core.userdetails.User) SecurityContextHolder
				.getContext().getAuthentication().getPrincipal();
		return Optional.of(principal);
	}
	
	private String encodePassword(String password) {
		return passwordEncoder.encode(password);
	}
}
