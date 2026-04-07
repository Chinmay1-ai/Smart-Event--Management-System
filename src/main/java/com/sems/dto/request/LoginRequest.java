package com.sems.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginRequest {
    // Simpler than register — just email and password
    // Client sends:
    // {
    //   "email": "chinmay@gmail.com",
    //   "password": "mypassword123"
    // }

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "Password is required")
    private String password;
    // No @Size here — if their password is correct, length doesn't matter
    // @Size on login would be confusing:
    // What if user registered before you added the @Size rule?
    // Just check @NotBlank and let BCrypt verify the password
}