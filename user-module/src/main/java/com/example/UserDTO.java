package com.example;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.security.core.userdetails.User;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class UserDTO {
    @NotBlank
    private String name;

    @Email
    @NotBlank
    @Column(unique = true)
    private String email;

    @NotBlank
    @Column(unique = true)
    private String username;
    @NotBlank
    private String password;

    public com.example.AbstractUser to(){
        return AbstractUser.builder()
                .name(name)
                .email(email)
                .username(username)
                .password(password)
                .build();
    }

}
