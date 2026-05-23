package unimagdalena.edu.omht.projections;

import java.util.UUID;

public interface SpecialtyCancelledOrNoShowAppointmentProjection {
    UUID getSpecialtyId();
    String getTitle();
    Long getCancelledOrNoShowAppointment();
}
