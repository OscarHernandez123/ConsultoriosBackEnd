package unimagdalena.edu.omht.dtos;

import java.util.UUID;

import jakarta.validation.constraints.NotBlank;

public class DoctorProfileDtos {

     public record CreateDoctorProfileRequest(
        @NotBlank String phone,
        @NotBlank String bio
    ){}

    public record PatchDoctorProfileRequest(
        String phone,
        String bio
    ){}

    public record UpdateDoctorProfileRequest(
        @NotBlank String phone,
        @NotBlank String bio
    ){}

    public record DoctorProfileResponse(
        UUID id,
        String phone,
        String bio
    ){}
}
