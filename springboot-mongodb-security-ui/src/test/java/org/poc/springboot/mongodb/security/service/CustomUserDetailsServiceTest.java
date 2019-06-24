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

import java.util.Optional;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.poc.springboot.mongodb.security.domain.User;
import org.poc.springboot.mongodb.security.repository.RoleRepository;
import org.poc.springboot.mongodb.security.repository.UserRepository;
import org.poc.springboot.mongodb.security.util.MockObjectCreator;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class CustomUserDetailsServiceTest {

	@Mock
	RoleRepository roleRepository;

	@Mock
	UserRepository userRepository;

	@Mock
	BCryptPasswordEncoder bCryptPasswordEncoder;

	@InjectMocks
	CustomUserDetailsServiceImpl customUserDetailsService;

	@Test
	void testLoadUserByUserName() {
		// given
		given(this.userRepository.findByEmail("junit@email.com")).willReturn(Optional.of(MockObjectCreator.getUser()));

		// when
		UserDetails userDetails = this.customUserDetailsService.loadUserByUsername("junit@email.com");

		// then
		assertThat(userDetails.isAccountNonExpired()).isTrue();
		assertThat(userDetails.isAccountNonLocked()).isTrue();
		assertThat(userDetails.isCredentialsNonExpired()).isTrue();
		assertThat(userDetails.isEnabled()).isTrue();
		assertThat(userDetails.getUsername()).isEqualTo("junit@email.com");
		assertThat(userDetails.getPassword()).isEqualTo("junitPassword");
	}

	@Test
	void testLoadUserByUserNameNotFound() {
		// given
		given(this.userRepository.findByEmail("junit@email.com")).willReturn(Optional.empty());

		// when
		assertThatThrownBy(() -> this.customUserDetailsService.loadUserByUsername("junit@email.com"))
				.isInstanceOf(UsernameNotFoundException.class).hasMessage("username not found");

	}

	@Test
	void testFindUserByEmail() {
		// given
		given(this.userRepository.findByEmail("junit@email.com")).willReturn(Optional.of(MockObjectCreator.getUser()));

		// when
		Optional<User> user = this.customUserDetailsService.findUserByEmail("junit@email.com");

		// then
		assertThat(user).isPresent();
		assertThat(user.get().getId()).isNull();
		assertThat(user.get().getEmail()).isEqualTo("junit@email.com");
		assertThat(user.get().getPassword()).isEqualTo("junitPassword");
		assertThat(user.get().getRoles()).isNotEmpty().hasSize(1);
	}

	@Test
	void saveUser() {
		User user = MockObjectCreator.getUser();
		String encodedPassword = RandomStringUtils.randomAlphanumeric(5);

		// given
		given(this.roleRepository.findByRole("ADMIN")).willReturn(MockObjectCreator.getRole());
		given(this.userRepository.save(any(User.class))).willReturn(user);
		given(this.bCryptPasswordEncoder.encode(user.getPassword())).willReturn(encodedPassword);

		// when
		this.customUserDetailsService.saveUser(user);

		// then
		verify(this.bCryptPasswordEncoder, times(1)).encode("junitPassword");
		verify(this.roleRepository, times(1)).findByRole("ADMIN");
		verify(this.userRepository, times(1)).save(any(User.class));
	}

}
