package com.example.domain;

import java.io.Serializable;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Document
@Data
@NoArgsConstructor
public class User implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private String id;
	@NonNull
	private String username;
	@NonNull
	private String password;
	private Role role;
}
