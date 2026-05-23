package unimagdalena.edu.omht.entities;

import jakarta.persistence.*;
import lombok.*;
import unimagdalena.edu.omht.enums.OfficeStatus;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name="offices")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Office {
    @Id @GeneratedValue (strategy = GenerationType.UUID) private UUID id;
    @Column (nullable = false) private String location;
    @Enumerated(EnumType.STRING) @Column (nullable = false) private OfficeStatus status;
    @Column (name = "created_at") private Instant createdAt;
    @Column (name = "updated_at") private Instant updatedAt;
    @OneToMany (mappedBy = "office") private List<Appointment> appointments;
}