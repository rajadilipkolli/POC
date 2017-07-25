package com.example.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.example.entities.User;

public interface UserRepository extends MongoRepository<User, String> {
	
	User findUserByUsername(String username);

	User findUserByEmail(String email);

	User findUserByUsernameAndPassword(String username, String givenPassword);
}
