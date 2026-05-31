package unimagdalena.edu.omht.dtos;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.UUID;

import jakarta.validation.constraints.NotNull;
import unimagdalena.edu.omht.dtos.DoctorDtos.DoctorResponse;

public class DoctorScheduleDtos {

    public record CreateDoctorScheduleRequest(
        @NotNull DayOfWeek dayOfWeek,
        @NotNull LocalTime startAt,
        @NotNull LocalTime endAt
    ){}

    public record DoctorScheduleResponse(
        UUID id,
        DayOfWeek dayOfWeek,
        LocalTime startAt,
        LocalTime endAt,
        DoctorResponse doctor
    ){}
}
