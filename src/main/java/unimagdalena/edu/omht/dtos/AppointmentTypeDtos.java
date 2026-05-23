package unimagdalena.edu.omht.dtos;

import java.util.UUID;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class AppointmentTypeDtos {

    public record CreateAppointmentTypeRequest(
        @NotBlank String title,
        @NotNull int durationMinutes
    ){}

    public record AppointmentTypeResponse(
        UUID id,
        String title,
        int durationMinutes
    ){}
}
