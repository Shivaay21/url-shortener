package com.example.urlshortner.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegisterRequestDTO {
    @Email
    @NotBlank
    private String email;

    @NotBlank
    @Size(min = 6)
    private String password;
}
