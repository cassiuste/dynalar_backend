//Implement the Login for the dentist to access the system. The dentist should be able to log in using their email and password. If the login is successful, return the user details; otherwise, return null.
package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.example.demo.model.user.User;
import com.example.demo.repository.UserRepository;

@RestController
@RequestMapping("/user")
public class UserController {

	@Autowired
	private UserRepository userRepository;
	
	@GetMapping("/all")
	public Iterable<User> getAllUsers() {
		return userRepository.findAll();
	}
	
	@GetMapping("/login")
	public User login(@RequestParam String email, @RequestParam String password) {
		User user = userRepository.findByEmail(email);
		if (user != null && user.getPassword().equals(password)) {
			return user;
		}
		return null;
	}
}