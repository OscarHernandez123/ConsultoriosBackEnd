package unimagdalena.edu.omht.dtos;

import java.time.Instant;
import java.util.UUID;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import unimagdalena.edu.omht.enums.PatientStatus;

public class PatientDtos {

    public record CreatePatientRequest(
        @NotBlank String fullName,
        @NotBlank String phone,
        @Email @NotBlank String email,
        @NotBlank String identification
    ){}

    public record PatchPatientRequest(
        String fullName,
        String phone,
        @Email String email,
        String identification,
        PatientStatus status
    ){}

    public record UpdatePatientRequest(
        @NotBlank String fullName,
        @NotBlank String phone,
        @NotBlank @Email String email,
        @NotBlank String identification,
        @NotNull PatientStatus status
    ){}

    public record PatientResponse(
        UUID id,
        String fullName,
        String phone,
        String email,
        String identification,
        PatientStatus status,
        Instant createdAt,
        Instant updatedAt
    ){}
}
