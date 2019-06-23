/*
 * Copyright 2015-2018 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.poc.springboot.mongodb.security.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.poc.springboot.mongodb.security.domain.Role;
import org.poc.springboot.mongodb.security.domain.User;
import org.poc.springboot.mongodb.security.repository.RoleRepository;
import org.poc.springboot.mongodb.security.repository.UserRepository;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Custom Implemetation of UserDetailsService.
 *
 * @author Raja Kolli
 * @since 0.2.1
 *
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

	private final UserRepository userRepository;

	private final RoleRepository roleRepository;

	private final BCryptPasswordEncoder bCryptPasswordEncoder;

	public CustomUserDetailsService(UserRepository userRepository, RoleRepository roleRepository,
			BCryptPasswordEncoder bCryptPasswordEncoder) {
		super();
		this.userRepository = userRepository;
		this.roleRepository = roleRepository;
		this.bCryptPasswordEncoder = bCryptPasswordEncoder;
	}

	public User findUserByEmail(String email) {
		return this.userRepository.findByEmail(email);
	}

	public void saveUser(User user) {
		user.setPassword(this.bCryptPasswordEncoder.encode(user.getPassword()));
		user.setEnabled(true);
		Role userRole = this.roleRepository.findByRole("ADMIN");
		user.setRoles(new HashSet<>(Arrays.asList(userRole)));
		this.userRepository.save(user);
	}

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

		User user = this.userRepository.findByEmail(email);
		if (user != null) {
			List<GrantedAuthority> authorities = getUserAuthority(user.getRoles());
			return buildUserForAuthentication(user, authorities);
		}
		else {
			throw new UsernameNotFoundException("username not found");
		}
	}

	private List<GrantedAuthority> getUserAuthority(Set<Role> userRoles) {
		Set<GrantedAuthority> roles = new HashSet<>();
		userRoles.forEach((role) -> roles.add(new SimpleGrantedAuthority(role.getRole())));

		return new ArrayList<>(roles);
	}

	private UserDetails buildUserForAuthentication(User user, List<GrantedAuthority> authorities) {
		return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), authorities);
	}

}
