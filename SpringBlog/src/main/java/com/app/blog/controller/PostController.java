package com.app.blog.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.blog.dto.PostDto;
import com.app.blog.service.PostService;

@RestController
@RequestMapping("/api/post")
public class PostController {
	@Autowired
	private PostService postService;
	
	@PostMapping("/")
	public ResponseEntity<?> createPost(@RequestBody PostDto postDto) {
		postService.createPost(postDto);
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	@GetMapping("/all")
	public ResponseEntity<List<PostDto>> getAllPost(){
		return new ResponseEntity<>(postService.getAllPost(),HttpStatus.OK);
		
	}
	
	@GetMapping("/get/{id}")
	public ResponseEntity<PostDto> getSinglePost(@PathVariable Long id){
		return new  ResponseEntity<>(postService.readSinglePost(id),HttpStatus.OK);
	}

}
