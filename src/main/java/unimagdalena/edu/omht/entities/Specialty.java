package unimagdalena.edu.omht.entities;

import jakarta.persistence.*;
import lombok.*;
import java.util.UUID;

@Entity
@Table(name = "specialties")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Specialty {
    @Id @GeneratedValue (strategy = GenerationType.UUID) private UUID id;
    @Column (nullable = false) private String title;
}
