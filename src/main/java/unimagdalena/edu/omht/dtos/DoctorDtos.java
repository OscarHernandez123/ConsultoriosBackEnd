package unimagdalena.edu.omht.dtos;

import java.time.Instant;
import java.util.UUID;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class DoctorDtos {

    public record CreateDoctorRequest(
        @NotBlank String fullName,
        @Email @NotBlank String email,
        @NotNull UUID specialtyId,
        @Valid DoctorProfileDtos.DoctorProfileCreateRequest profile
    ){}

    public record PatchDoctorRequest(
        String fullName,
        @Email String email,
        UUID specialtyId,
        DoctorProfileDtos.DoctorProfilePatchRequest profile
    ){}

    public record UpdateDoctorRequest(
        @NotBlank String fullName,
        @Email @NotBlank String email,
        @NotNull UUID specialtyId,
        @Valid DoctorProfileDtos.DoctorProfileCreateRequest profile
    ){}

    public record DoctorResponse(
        UUID id,
        String fullName,
        String email,
        DoctorProfileDtos.DoctorProfileResponse profile,
        Instant createdAt,
        Instant updatedAt,
        SpecialtyDtos.SpecialtyResponse specialty
    ){}
}
