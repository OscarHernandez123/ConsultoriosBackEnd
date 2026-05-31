package unimagdalena.edu.omht.security.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import unimagdalena.edu.omht.security.dto.AuthDtos.AuthResponse;
import unimagdalena.edu.omht.security.dto.AuthDtos.LoginRequest;
import unimagdalena.edu.omht.security.dto.AuthDtos.RegisterRequest;
import unimagdalena.edu.omht.security.service.AuthService;

@RestController
@RequestMapping("/api/auth") 
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
        AuthResponse response = authService.register(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        AuthResponse response = authService.login(request);
        return ResponseEntity.ok(response);
    }
}
