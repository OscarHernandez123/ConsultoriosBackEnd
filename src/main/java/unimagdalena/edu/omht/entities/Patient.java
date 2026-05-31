package unimagdalena.edu.omht.entities;

import jakarta.persistence.*;
import lombok.*;
import unimagdalena.edu.omht.enums.PatientStatus;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name="patients")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Patient { 
    @Id @GeneratedValue (strategy = GenerationType.UUID) private UUID id;
    @Column (nullable = false, name = "full_name") private String fullName;
    @Column (nullable = false) private String phone;
    @Column (nullable = false) private String email;
    @Column(unique = true, nullable = false) private String identification;
    @Enumerated(EnumType.STRING) @Column (nullable = false) private PatientStatus status;
    @Column (name = "createdAt") private Instant createdAt;
    @Column (name = "updatedAt") private Instant updatedAt;
    @OneToMany (mappedBy = "patient") private List<Appointment> appointments;
}
