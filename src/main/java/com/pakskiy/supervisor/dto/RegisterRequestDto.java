package com.pakskiy.supervisor.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class RegisterRequestDto {
    @NotNull(message = "Username is required")
    private String username;

    @Email
    @NotNull(message = "Email is required")
    private  String email;

    @NotNull(message = "First name is required")
    private String firstName;

    @NotNull(message = "Last name  is required")
    private  String lastName;

    @NotNull(message = "Password is required")
    private  String password;
}
