package com.example.domain;

import lombok.Data;
import lombok.NonNull;

@Data
public class UserCreateForm {
    @NonNull
    private String username = "";

    @NonNull
    private String password = "";

    @NonNull
    private String passwordRepeated = "";

    @NonNull
    private Role role = Role.USER;
}
