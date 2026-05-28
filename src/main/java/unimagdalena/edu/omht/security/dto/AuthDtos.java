package unimagdalena.edu.omht.security.dto;

import java.util.Set;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import unimagdalena.edu.omht.security.domine.Role;

public class AuthDtos {

    public record LoginRequest(
        @NotBlank @Email String email,
        @NotBlank String password
    ){}

    public record RegisterRequest(
        @NotBlank @Email String email,
        @NotBlank String password,
        Set<Role> roles
    ){}

    public record AuthResponse(
        String accessToken,
        String tokenType,
        long expiresInSeconds
    ){}
}
