package com.example.service.currentuser;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.domain.CurrentUser;
import com.example.domain.User;
import com.example.service.UserService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class CurrentUserDetailsService implements UserDetailsService {

	private final UserService userService;

	@Override
	public UserDetails loadUserByUsername(String username)
			throws UsernameNotFoundException {
		log.debug("Authenticating user with username={}",
				username.replaceFirst("@.*", "@***"));
		User user = userService.getUserByUserName(username)
				.orElseThrow(() -> new UsernameNotFoundException(
						String.format("User with username=%s was not found", username)));
		return new CurrentUser(user);
	}

}
