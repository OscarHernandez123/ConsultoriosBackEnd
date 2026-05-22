package unimagdalena.edu.omht.entities;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "appointments_types")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AppointmentType {
    @Id @GeneratedValue (strategy = GenerationType.UUID) private UUID id;
    @Column (nullable = false) private String title;
    @Column (nullable = false, name = "duration_minutes") private int durationMinutes;
    @OneToMany (mappedBy = "appointmentType") private List<Appointment> appointments;
}
