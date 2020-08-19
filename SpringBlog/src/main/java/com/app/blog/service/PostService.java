package com.app.blog.service;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import com.app.blog.dto.PostDto;
import com.app.blog.exception.PostNotFoundException;
import com.app.blog.model.Post;
import com.app.blog.repository.PostRepo;

@Service
public class PostService {
	
	@Autowired
	private PostRepo postRepo;
	@Autowired
	private AuthService authService;
	
	public void createPost(PostDto postDto) {
		Post post=mapFromDtoToPost(postDto); 
		postRepo.save(post);
	}
	
	public List<PostDto> getAllPost() {
		List<Post> posts=postRepo.findAll();
		return posts.stream().map(this::mapFromPostToDto).collect(Collectors.toList());
	}
	
	public PostDto readSinglePost(Long id) {
		Post post=postRepo.findById(id).orElseThrow(()->new PostNotFoundException("For id" +id));
		return mapFromPostToDto(post);
	}
	private PostDto mapFromPostToDto(Post post) {
		PostDto postDto=new PostDto();
		postDto.setId(post.getId());
		postDto.setTitle(post.getTitle());
		postDto.setContent(post.getContent());
		postDto.setUsername(post.getUsername());
		
		return postDto;
	}
	
	private Post mapFromDtoToPost(PostDto postDto) {
		Post post=new Post();
		post.setTitle(postDto.getTitle());
		post.setContent(postDto.getContent());
		User user=authService.getCurrentUser().orElseThrow(()->
				new IllegalArgumentException("No User logged in"));
		post.setUsername(user.getUsername());
		post.setCreatedOn(Instant.now());
		
		return post;
	}
}
