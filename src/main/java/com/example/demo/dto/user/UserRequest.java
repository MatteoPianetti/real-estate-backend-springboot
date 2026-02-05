package com.example.demo.dto.user;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserRequest {
    @NotNull
    private String email;
    
    @NotNull
    private String password;
    
    @NotNull
    private String firstName;
    
    @NotNull
    private String lastName;
}
