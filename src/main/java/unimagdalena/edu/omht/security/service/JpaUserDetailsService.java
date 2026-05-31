package unimagdalena.edu.omht.security.service;

import lombok.RequiredArgsConstructor;
import unimagdalena.edu.omht.security.domine.AppUser;
import unimagdalena.edu.omht.security.repository.AppUserRepository;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class JpaUserDetailsService implements UserDetailsService{
    
    private final AppUserRepository appUserRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        
        AppUser user = appUserRepository.findByEmailIgnoreCase(username)
            .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        Set<SimpleGrantedAuthority> authorities = user.getRoles().stream()
            .map(Enum::name)
            .map(SimpleGrantedAuthority::new)
            .collect(Collectors.toSet());

        return User.withUsername(user.getEmail())
                .password(user.getPassword())
                .authorities(authorities)
                .build();
    }

}
