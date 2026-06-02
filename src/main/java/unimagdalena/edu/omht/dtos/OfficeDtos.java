package unimagdalena.edu.omht.dtos;

import java.time.Instant;
import java.util.UUID;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import unimagdalena.edu.omht.enums.OfficeStatus;

public class OfficeDtos {

    public record CreateOfficeRequest(
        @NotBlank String name,
        @NotBlank String location
    ){}

    public record PatchOfficeRequest(
        String name,
        String location,
        OfficeStatus status
    ){}

    public record UpdateOfficeRequest(
        @NotBlank String name,
        @NotBlank String location,
        @NotNull OfficeStatus status
    ){}

    public record OfficeResponse(
        UUID id,
        String name,
        String location,
        OfficeStatus status,
        Instant createdAt,
        Instant updatedAt
    ){}
}
