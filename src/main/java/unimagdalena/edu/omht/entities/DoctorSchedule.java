package unimagdalena.edu.omht.entities;

import jakarta.persistence.*;
import lombok.*;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.UUID;

@Entity
@Table(name="doctors_schedules")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DoctorSchedule {
    @Id @GeneratedValue (strategy = GenerationType.UUID) private UUID id;
    @Enumerated(EnumType.STRING) @Column (nullable = false, name = "day_of_week") private DayOfWeek dayOfWeek;
    @Column (nullable = false, name = "start_at") private LocalTime startAt;
    @Column (nullable = false, name = "end_at") private LocalTime endAt;
    @ManyToOne @JoinColumn (name = "doctor_id") private Doctor doctor;
}