package unimagdalena.edu.omht.dtos;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.UUID;

import jakarta.validation.constraints.NotNull;

public class DoctorScheduleDtos {

    public record CreateDoctorScheduleRequest(
        @NotNull DayOfWeek dayOfWeek,
        @NotNull LocalTime startAt,
        @NotNull LocalTime endAt,
        @NotNull UUID doctorId
    ){}

    public record DoctorScheduleResponse(
        UUID id,
        DayOfWeek dayOfWeek,
        LocalTime startAt,
        LocalTime endAt,
        DoctorDtos.DoctorResponse doctor
    ){}
}
