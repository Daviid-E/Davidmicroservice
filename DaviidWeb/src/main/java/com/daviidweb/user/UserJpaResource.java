package com.daviidweb.user;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("/jpa")
public class UserJpaResource {
		
		@Autowired
		private UserDao service;
		
		@Autowired
		private PostRepository postRepo;
		
		@Autowired
		private UserRepository userRepository;
		
		@GetMapping("/users")
		public List<User> retrieveAllUsers(){
			return userRepository.findAll();
		}
		@GetMapping("user/{id}")
		public EntityModel<User>  getUser(@PathVariable int id) {
			Optional<User> user = userRepository.findById(id);
			if(!user.isPresent()) 
				throw new UserNotFoundException("id-"+id);
			EntityModel<User> model = EntityModel.of(user.get());
			WebMvcLinkBuilder linkToUsers = linkTo(methodOn(this.getClass()).retrieveAllUsers());
			model.add(linkToUsers.withRel("all-users"));
			return model;
		}
		@GetMapping("user/{id}/posts")
		public List<Post> getUserPost(@PathVariable int id) {
			Optional<User> userOptional = userRepository.findById(id);
			if(!userOptional.isPresent()) {
				throw new UserNotFoundException("id-" + id);
			}
			return userOptional.get().getPosts();
		}
		
		@DeleteMapping("user/{id}")
		public void deleteUser(@PathVariable int id) {
			userRepository.deleteById(id);
			
		}
		
		@PostMapping("/users")
		public ResponseEntity<Object> saveUser(@Valid @RequestBody User user) {
			User savedUser = userRepository.save(user);
			
			URI location = ServletUriComponentsBuilder
				.fromCurrentRequest()
				.path("{id}")
				.buildAndExpand(savedUser.getId()).toUri();
			
			return ResponseEntity.created(location).build();
		}
		
		@PostMapping("/users/{id}/posts")
		public ResponseEntity<Object> userPost(@PathVariable int id,@Valid @RequestBody Post post ) {
			Optional<User> userOptional = userRepository.findById(id);
			if(!userOptional.isPresent()) {
				throw new UserNotFoundException("id-" + id);
			}
			User user = userOptional.get();
			post.setUser(user);
			
			Post userPost = postRepo.save(post);
			
			URI location = ServletUriComponentsBuilder
				.fromCurrentRequest()
				.path("{id}")
				.buildAndExpand(userPost.getId()).toUri();
			
			return ResponseEntity.created(location).build();
		}


}
