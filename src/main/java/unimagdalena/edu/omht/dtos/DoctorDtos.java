package unimagdalena.edu.omht.dtos;

import java.time.Instant;
import java.util.UUID;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import unimagdalena.edu.omht.dtos.DoctorProfileDtos.CreateDoctorProfileRequest;
import unimagdalena.edu.omht.dtos.DoctorProfileDtos.DoctorProfileResponse;
import unimagdalena.edu.omht.dtos.DoctorProfileDtos.PatchDoctorProfileRequest;
import unimagdalena.edu.omht.dtos.DoctorProfileDtos.UpdateDoctorProfileRequest;
import unimagdalena.edu.omht.dtos.SpecialtyDtos.SpecialtyResponse;

public class DoctorDtos {

    public record CreateDoctorRequest(
        @NotBlank String fullName,
        @Email @NotBlank String email,
        @NotNull UUID specialtyId,
        @Valid CreateDoctorProfileRequest profile
    ){}

    public record PatchDoctorRequest(
        String fullName,
        @Email String email,
        UUID specialtyId,
        PatchDoctorProfileRequest profile
    ){}

    public record UpdateDoctorRequest(
        @NotBlank String fullName,
        @Email @NotBlank String email,
        @NotNull UUID specialtyId,
        @Valid UpdateDoctorProfileRequest profile
    ){}

    public record DoctorResponse(
        UUID id,
        String fullName,
        String email,
        DoctorProfileResponse profile,
        Instant createdAt,
        Instant updatedAt,
        SpecialtyResponse specialty
    ){}
}
