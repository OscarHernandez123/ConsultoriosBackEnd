package unimagdalena.edu.omht.security.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import unimagdalena.edu.omht.exceptions.BusinessException;
import unimagdalena.edu.omht.exceptions.ResourceNotFoundException;
import unimagdalena.edu.omht.security.domine.AppUser;
import unimagdalena.edu.omht.security.domine.Role;
import unimagdalena.edu.omht.security.dto.AuthDtos.AuthResponse;
import unimagdalena.edu.omht.security.dto.AuthDtos.LoginRequest;
import unimagdalena.edu.omht.security.dto.AuthDtos.RegisterRequest;
import unimagdalena.edu.omht.security.repository.AppUserRepository;

import java.util.Map;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AppUserRepository usersRepository;

    private final PasswordEncoder passwordEncoder;

    private final JwtService jwtService;

    private final AuthenticationManager authenticationManager;

    public AuthResponse register(RegisterRequest request) {
        
        if (usersRepository.existsByEmailIgnoreCase(request.email())) {
            throw new BusinessException("El correo ya está registrado");
        }

        Set<Role> roles = Optional.ofNullable(request.roles())
                .filter(r -> !r.isEmpty())
                .orElseGet(() -> Set.of(Role.PATIENT));

        AppUser user = AppUser.builder()
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .roles(roles)
                .build();

        usersRepository.save(user);

        UserDetails principal = User.withUsername(user.getEmail())
                .password(user.getPassword())
                .authorities(roles.stream().map(Enum::name).toArray(String[]::new))
                .build();

        String token = jwtService.generateToken(principal, Map.of("roles", roles));
        
        return new AuthResponse(token, "Bearer", jwtService.getExpirationSeconds()); 
    }

    public AuthResponse login(LoginRequest request) {
        
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.email(), request.password())
        );
        
        AppUser user = usersRepository.findByEmailIgnoreCase(request.email())
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));
                
        UserDetails principal = User.withUsername(user.getEmail())
                .password(user.getPassword())
                .authorities(user.getRoles().stream().map(Enum::name).toArray(String[]::new))
                .build();
                
        String token = jwtService.generateToken(principal, Map.of("roles", user.getRoles()));
        
        return new AuthResponse(token, "Bearer", jwtService.getExpirationSeconds());
    }
}
