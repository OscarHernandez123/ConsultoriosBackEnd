package unimagdalena.edu.omht.projections;

import java.util.UUID;

public interface DoctorCompletedAppointmentProjection {
    UUID getDoctorId();
    String getFullName();
    Long getCompletedAppointments();
}
