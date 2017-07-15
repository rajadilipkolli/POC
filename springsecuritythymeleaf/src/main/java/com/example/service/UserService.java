package com.example.service;

import java.util.Collection;
import java.util.Optional;

import com.example.domain.User;
import com.example.domain.UserCreateForm;

public interface UserService {

	Optional<User> getUserById(String id);

	Optional<User> getUserByUserName(String userName);

	Collection<User> getAllUsers();

	User create(UserCreateForm form);

}
