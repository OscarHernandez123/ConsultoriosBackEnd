package unimagdalena.edu.omht.mappers;

import unimagdalena.edu.omht.dtos.OfficeDtos.CreateOfficeRequest;
import unimagdalena.edu.omht.dtos.OfficeDtos.OfficeResponse;
import unimagdalena.edu.omht.dtos.OfficeDtos.PatchOfficeRequest;
import unimagdalena.edu.omht.dtos.OfficeDtos.UpdateOfficeRequest;
import unimagdalena.edu.omht.entities.Office;
import unimagdalena.edu.omht.enums.OfficeStatus;

public class OfficeMapper {

    public static Office toEntity(CreateOfficeRequest request){
        return Office.builder()
                .name(request.name())
                .location(request.location())
                .status(OfficeStatus.ACTIVE)
                .build();
    }

    public static void patch(Office office, PatchOfficeRequest request){

        if(request.name() != null){
            office.setName(request.name());
        }

        if(request.location() != null){
            office.setLocation(request.location());
        }

        if(request.status() != null){
            office.setStatus(request.status());
        }
    }

    public static void update(Office office, UpdateOfficeRequest request){
        office.setName(request.name());
        office.setLocation(request.location());
        office.setStatus(request.status());
    }

    public static OfficeResponse toResponse(Office office){
        return new OfficeResponse(
            office.getId(),
            office.getName(),
            office.getLocation(),
            office.getStatus(),
            office.getCreatedAt(),
            office.getUpdatedAt()
        );
    }
}
