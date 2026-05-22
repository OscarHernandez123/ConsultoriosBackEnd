package unimagdalena.edu.omht.entities;

import jakarta.persistence.*;
import lombok.*;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name="doctors")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Doctor {
    @Id @GeneratedValue (strategy = GenerationType.UUID) private UUID id;
    @Column (nullable = false, name = "full_name") private String fullName;
    @Column (nullable = false) private String email;
    @Column (name = "created_at") private Instant createdAt;
    @Column (name = "updated_at") private Instant updatedAt;
    @OneToOne (mappedBy = "doctor") private DoctorProfile doctorProfile;
    @OneToMany (mappedBy = "doctor") private List<DoctorSchedule> doctorSchedules;
    @OneToMany (mappedBy = "doctor") private List<Appointment> appointments;
    @ManyToOne @JoinColumn (name = "especialty_id") private Specialty specialty;
}
