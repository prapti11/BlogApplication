package com.app.blog.service;

import java.util.Collection;
import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.app.blog.model.User;
import com.app.blog.repository.UserRepo;

@Service
public class MyUserDetailsService implements UserDetailsService{
	@Autowired
	private UserRepo userRepo; 
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user =userRepo.findUserByUsername(username)
				.orElseThrow(()-> new UsernameNotFoundException("No user found with username "+ username));
		return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(),
				true, true, true,true,getAuthorities("ROLE+USER"));
	}
	
	private Collection<? extends GrantedAuthority> getAuthorities(String role){
		return Collections.singletonList(new SimpleGrantedAuthority(role));
	}

}
