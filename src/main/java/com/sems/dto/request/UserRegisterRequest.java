package com.sems.dto.request;

import com.sems.enums.Role;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserRegisterRequest {
    // This class represents what the CLIENT sends to us
    // when they want to register a new user
    //
    // Client sends this JSON:
    // {
    //   "name": "Chinmay",
    //   "email": "chinmay@gmail.com",
    //   "password": "mypassword123",
    //   "role": "ATTENDEE"
    // }

    @NotBlank(message = "Name is required")
    // @NotBlank checks THREE things at once:
    // 1. value is not null
    // 2. value is not empty string ""
    // 3. value is not just whitespace "   "
    //
    // If validation fails, Spring returns 400 Bad Request
    // with message: "Name is required"
    // You never write if-else for this — annotation handles it

    @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
    // @Size checks the LENGTH of the string
    // min=2 → "A" would fail (too short)
    // max=100 → more than 100 chars would fail (too long)
    // This matches the @Column(length=100) in User.java entity
    private String name;

    @NotBlank(message = "Email is required")
    @Email(message = "Please provide a valid email address")
    // @Email checks if the string is a valid email format
    // "chinmay@gmail.com"  → passes
    // "chinmay"            → fails (no @ symbol)
    // "chinmay@"           → fails (no domain)
    // "chinmay@gmail"      → fails (no .com/.in etc)
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 8, message = "Password must be at least 8 characters")
    // min=8 → enforces minimum password length
    // Notice: this field is named "password" not "passwordHash"
    // Client sends plain text password
    // Service layer hashes it with BCrypt before saving
    // The plain text password NEVER touches the database
    private String password;

    @NotNull(message = "Role is required")
    // @NotNull → value cannot be null
    // @NotBlank is for Strings only
    // @NotNull works for any type including enums
    // If client sends invalid role like "SUPERUSER":
    // Spring cannot deserialize it → 400 Bad Request automatically
    private Role role;
}