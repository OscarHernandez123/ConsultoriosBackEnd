package unimagdalena.edu.omht.projections;

import java.time.Instant;
import java.util.UUID;

public interface PatientNoShowAppointmentProjection {
    UUID getPatientId();
    String getFullName();
    Long getNoShowAppointments();
    Instant getLastNoShowDate();
}
