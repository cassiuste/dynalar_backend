package com.dynalar.dynalar.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.dynalar.dynalar.respository.UserRepository;
import com.dynalar.dynalar.model.user.User;

@RestController
@RequestMapping("/user")
public class UserController {

	@Autowired
	private UserRepository userRepository;
	
	@GetMapping("/all")
	public Iterable<User> getAllUsers() {
		return userRepository.findAll();
	}
	
	@PostMapping("/login")
	public User login(@RequestBody User user) {	
		Optional<User> newUser = userRepository.findByEmail(user.getEmail());
		if (newUser.isPresent() && newUser.get().getPassword().equals(user.getPassword())) {
			User loggedInUser = newUser.get();
			loggedInUser.setPassword(null);
			return loggedInUser;
		}
		return null;
	}
}