package unimagdalena.edu.omht.mappers;

import unimagdalena.edu.omht.dtos.AppointmentTypeDtos.AppointmentTypeResponse;
import unimagdalena.edu.omht.dtos.AppointmentTypeDtos.CreateAppointmentTypeRequest;
import unimagdalena.edu.omht.entities.AppointmentType;

public class AppointmentTypeMapper {

    public static AppointmentType toEntity(CreateAppointmentTypeRequest request){
        return AppointmentType.builder()
                .title(request.title())
                .durationMinutes(request.durationMinutes())
                .build();
    }

    public static AppointmentTypeResponse toResponse(AppointmentType appointmentType){
        return new AppointmentTypeResponse(
            appointmentType.getId(),
            appointmentType.getTitle(),
            appointmentType.getDurationMinutes()
        );
    }
}
