package unimagdalena.edu.omht.dtos;

import java.time.Instant;
import java.util.UUID;

import jakarta.validation.constraints.NotNull;
import unimagdalena.edu.omht.enums.AppointmentStatus;

public class AppointmentDtos {

    public record CreateAppointmentRequest(
        @NotNull Instant startAt,
        @NotNull UUID doctorId,
        @NotNull UUID patientId,
        @NotNull UUID officeId,
        @NotNull UUID appointmentTypeId
    ){} 

    public record AppointmentCancelRequest(
        @NotNull String reason
    ){}

    public record AppointmentCompleteRequest(
        @NotNull String administrativeNotes
    ){}

    public record AppointmentResponse(
        UUID id,
        Instant startAt,
        Instant endAt,
        AppointmentStatus status,
        UUID doctorId,
        String doctorFullName,
        UUID patientId,
        String patientFullName,
        UUID officeId,
        String officeLocation,
        UUID appointmentTypeId,
        String appointmentTypeTitle,
        Instant createAt,
        Instant updatedAt
    ){}
}
