package unimagdalena.edu.omht.security.domine;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;
import java.util.UUID;

@Entity 
@Table(name="users")
@Getter 
@Setter 
@Builder 
@NoArgsConstructor 
@AllArgsConstructor
public class AppUser {
    @Id @GeneratedValue (strategy = GenerationType.UUID) private UUID id;
    @Column (nullable = false, unique = true) private String email;
    @Column (nullable = false, unique = true) private String password;
    @ElementCollection (fetch = FetchType.EAGER)
    @Enumerated (EnumType.STRING)
    @CollectionTable (name = "user_roles", joinColumns = @JoinColumn(name = "user_id"))
    @Column (name = "role") private Set<Role> roles;
}
