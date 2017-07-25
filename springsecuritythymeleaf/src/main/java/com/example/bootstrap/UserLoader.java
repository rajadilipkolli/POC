package com.example.bootstrap;

import java.util.Arrays;

import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.example.entities.Role;
import com.example.entities.User;
import com.example.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@RequiredArgsConstructor
public class UserLoader implements ApplicationListener<ContextRefreshedEvent> {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;

	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		userRepository.deleteAll();
		User adminUser = new User();
		adminUser.setUsername("admin");
		adminUser.setPassword(passwordEncoder.encode("admin123"));
		adminUser.setRoles(Arrays.asList(Role.ROLE_ADMIN));
		userRepository.save(adminUser);
		log.debug("Saved User Name {}", adminUser.getName());
		User user = new User();
		user.setUsername("user");
		user.setPassword(passwordEncoder.encode("user123"));
		user.setRoles(Arrays.asList(Role.ROLE_USER));
		userRepository.save(user);
		log.debug("Saved User Name {}", user.getName());

	}

}
