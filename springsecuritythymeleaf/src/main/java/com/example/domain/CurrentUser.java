package com.example.domain;

import org.springframework.security.core.authority.AuthorityUtils;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
public class CurrentUser extends org.springframework.security.core.userdetails.User {

	private static final long serialVersionUID = 1L;

	private User user;

	public CurrentUser(User user) {
		super(user.getUsername(), user.getPassword(),
				AuthorityUtils.createAuthorityList(user.getRole().toString()));
		this.user = user;
	}

	public User getUser() {
		return user;
	}

	public String getId() {
		return user.getId();
	}

	public Role getRole() {
		return user.getRole();
	}

	@Override
	public String toString() {
		return "CurrentUser{" + "user=" + user + "} " + super.toString();
	}
}
