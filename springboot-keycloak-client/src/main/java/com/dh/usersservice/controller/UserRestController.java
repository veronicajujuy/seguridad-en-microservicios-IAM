package com.dh.usersservice.controller;

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

import com.dh.usersservice.model.User;
import com.dh.usersservice.model.UserRequest;
import com.dh.usersservice.service.UserService;

@RestController
@RequestMapping
public class UserRestController {
	@Autowired
	private UserService userService;

	@GetMapping("/user/{id}")
	public User getById(@PathVariable String id) {
		return userService.findById(id);
	}
	
	@GetMapping("/users/{name}")
	public List<User> getByName(@PathVariable String name) {
		return userService.findByName(name);
	}

	@PostMapping("/user")
	public ResponseEntity<User> createUser(@RequestBody UserRequest userRequest) {
		try {
			User createdUser = userService.createUser(userRequest);
			return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
		}
	}
}
