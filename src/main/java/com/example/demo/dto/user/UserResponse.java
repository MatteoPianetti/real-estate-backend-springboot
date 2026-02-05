package com.example.demo.dto.user;

import com.example.demo.domain.user.User;
import com.example.demo.domain.user.enumerated.Role;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponse {
    private Long id;
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private Role role;

    public static UserResponse from(User u) {
        UserResponse res = new UserResponse();

        res.setId(u.getId());
        res.setEmail(u.getEmail());
        res.setFirstName(u.getFirstName());
        res.setLastName(u.getLastName());
        res.setPassword(u.getPassword());
        res.setRole(u.getRole());

        return res;
    }
}
