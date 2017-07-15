package com.example.bootstrap;

import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.example.domain.Role;
import com.example.domain.User;
import com.example.repositories.UserRepository;

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
		adminUser.setRole(Role.ADMIN);
		userRepository.save(adminUser);
		log.debug("Saved User Id {}", adminUser.getId());
		User user = new User();
		user.setUsername("user");
		user.setPassword(passwordEncoder.encode("user123"));
		user.setRole(Role.USER);
		userRepository.save(user);
		log.debug("Saved User Id {}", user.getId());

	}

}
