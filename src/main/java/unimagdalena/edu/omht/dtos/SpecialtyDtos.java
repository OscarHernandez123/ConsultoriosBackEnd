package unimagdalena.edu.omht.dtos;

import java.util.UUID;

import jakarta.validation.constraints.NotBlank;

public class SpecialtyDtos {

    public record CreateSpecialtyRequest(
        @NotBlank String title
    ){}

    public record SpecialtyResponse(
        UUID id,
        String title
    ){}
}
