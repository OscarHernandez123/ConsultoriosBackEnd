package unimagdalena.edu.omht.mappers;

import unimagdalena.edu.omht.entities.Specialty;
import unimagdalena.edu.omht.dtos.SpecialtyDtos.CreateSpecialtyRequest;
import unimagdalena.edu.omht.dtos.SpecialtyDtos.SpecialtyResponse;

public class SpecialtyMapper {

    public static Specialty toEntity(CreateSpecialtyRequest request){
        return Specialty.builder()
                .title(request.title())
                .build();
    }

    public static SpecialtyResponse toResponse(Specialty specialty){
        return new SpecialtyResponse(
            specialty.getId(),
            specialty.getTitle()
        );
    }
}
