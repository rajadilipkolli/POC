package com.example.service.impl;

import java.util.Collection;
import java.util.Optional;

import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.domain.User;
import com.example.domain.UserCreateForm;
import com.example.repositories.UserRepository;
import com.example.service.UserService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

	private final UserRepository userRepository;

	@Override
	public Optional<User> getUserById(String id) {
		log.debug("Getting user={}", id);
		return userRepository.findById(id);
	}

	@Override
	public Collection<User> getAllUsers() {
		log.debug("Getting all users");
		return userRepository.findAll(Sort.by("username"));
	}

	@Override
	public User create(UserCreateForm form) {
		User user = new User();
		user.setUsername(form.getUsername());
		user.setPassword(new BCryptPasswordEncoder().encode(form.getPassword()));
		user.setRole(form.getRole());
		return userRepository.save(user);
	}

	@Override
	public Optional<User> getUserByUserName(String userName) {
		log.debug("Getting user by username={}", userName);
		return userRepository.findByUsername(userName);

	}

}
