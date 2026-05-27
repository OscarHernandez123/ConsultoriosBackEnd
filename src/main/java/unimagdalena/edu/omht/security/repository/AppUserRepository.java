package unimagdalena.edu.omht.security.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import unimagdalena.edu.omht.security.domine.AppUser;

public interface AppUserRepository extends JpaRepository<AppUser, UUID>{
    Optional<AppUser> findByEmailIgnoreCase(String email);
    boolean exiexistsByEmailIgnoreCase(String email);
}
