package com.example.entities;

import java.util.Collection;
import java.util.stream.Collectors;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.Data;

@Document
@Data
public class User implements UserDetails {

	private static final long serialVersionUID = 1L;

	private String email;

	private String username;

	private String password;

	private boolean enabled;

	private boolean locked = false;

	private Collection<Role> roles;

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return getRoles().stream().map(Role::getAuthority)
				.map(SimpleGrantedAuthority::new).collect(Collectors.toSet());
	}

	@Override
	public String getPassword() {
		return password;
	}

	@Override
	public String getUsername() {
		return username;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true; // Not Implemented
	}

	@Override
	public boolean isAccountNonLocked() {
		return !isLocked();
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true; // Not Implemented
	}

	@Override
	public boolean isEnabled() {
		return enabled;
	}

}
