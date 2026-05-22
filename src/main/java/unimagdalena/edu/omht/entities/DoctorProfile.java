package unimagdalena.edu.omht.entities;

import jakarta.persistence.*;
import lombok.*;
import java.util.UUID;

@Entity
@Table(name="doctor_profiles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DoctorProfile {
    @Id @GeneratedValue (strategy = GenerationType.UUID) private UUID id;
    @Column (nullable = false) private String phone;
    @Column (nullable = false) private String bio;
    @OneToOne @JoinColumn (name = "doctor_id") private Doctor doctor;
}
