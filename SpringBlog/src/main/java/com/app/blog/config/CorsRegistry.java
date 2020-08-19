package com.app.blog.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
public class CorsRegistry implements WebMvcConfigurer {

	@Override
	public void addCorsMappings(org.springframework.web.servlet.config.annotation.CorsRegistry registry) {
		registry.addMapping("/**")
		.allowedOrigins("*")
		.allowedMethods("*")
		.maxAge(3600L)
		.allowedHeaders("*")
		.exposedHeaders("Authorization")
		.allowCredentials(true);
	}
	

}
