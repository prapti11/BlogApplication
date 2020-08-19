package com.app.blog.security;

import java.io.InputStream;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

import javax.annotation.PostConstruct;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import com.app.blog.exception.SpringBlogException;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.IOException;

@Service
public class JwtProvider {
	private KeyStore keyStore;
	
	@PostConstruct
	public void init() throws Exception {
		try {
			keyStore=KeyStore.getInstance("JKS");
			InputStream resourceAsStream=getClass().getResourceAsStream("/springblog.jks");
			keyStore.load(resourceAsStream,"jwttoken".toCharArray());} 
		catch (KeyStoreException | CertificateException|NoSuchAlgorithmException|IOException e) {
			throw new SpringBlogException("Exception occured while loading keystore");
		}
		
	}
	
	public String generateToken(Authentication authentication) {
	User principal=(User)authentication.getPrincipal();
	return Jwts.builder()
			.setSubject(principal.getUsername())
			.signWith(getPrivateKey())
			.compact();
	}
	
	private PrivateKey getPrivateKey() {
		try {
			return (PrivateKey)keyStore.getKey("springblog", "jwttoken".toCharArray());
		} catch (UnrecoverableKeyException |KeyStoreException|NoSuchAlgorithmException e) {
			throw new SpringBlogException("Exception occured while retrieving private key from keystore");
		} 
	}
	
	
	public boolean validateToken(String jwt){
		Jwts.parser().setSigningKey(getPublicKey()).parseClaimsJws(jwt);
		return true;
	}
	
	private PublicKey getPublicKey() {
		try {
			return keyStore.getCertificate("springblog").getPublicKey();
		} catch (KeyStoreException e) {
			throw new SpringBlogException("Exception occured while retrieving private key from keystore");
		}
	}
	
	public String getUsernameFromJwt(String token) {
		Claims claims=Jwts.parser()
				.setSigningKey(getPublicKey())
				.parseClaimsJws(token)
				.getBody();
		return claims.getSubject();
	}

}
