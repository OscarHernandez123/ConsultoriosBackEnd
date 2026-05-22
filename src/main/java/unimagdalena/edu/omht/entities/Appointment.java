package unimagdalena.edu.omht.entities;

import jakarta.persistence.*;
import lombok.*;
import unimagdalena.edu.omht.enums.AppointmentStatus;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name="appointments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Appointment {
    @Id @GeneratedValue (strategy = GenerationType.UUID) private UUID id;
    @Column (nullable = false) private String title;
    @Column (nullable = false, name = "start_at") private Instant startAt;
    @Column (name = "end_at") private Instant endAt;
    @Enumerated(EnumType.STRING) @Column (nullable = false) private AppointmentStatus status;
    @Column (name = "created_at") private Instant createdAt;
    @Column (name = "updated_at") private Instant updatedAt;
    @Column (name = "cancellation_reason") private String cancellationReason;
    @Column (name = "administrative_note") private String administrativeNote;
    @ManyToOne @JoinColumn (name = "doctor_id") private Doctor doctor;
    @ManyToOne @JoinColumn (name = "patient_id") private Patient patient;
    @ManyToOne @JoinColumn (name = "office_id") private Office office;
    @ManyToOne @JoinColumn (name = "appointment_type_id") private AppointmentType appointmentType;
}