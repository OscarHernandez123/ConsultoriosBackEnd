package unimagdalena.edu.omht.mappers;

import unimagdalena.edu.omht.dtos.AppointmentDtos.AppointmentResponse;
import unimagdalena.edu.omht.dtos.AppointmentDtos.CreateAppointmentRequest;
import unimagdalena.edu.omht.entities.Appointment;
import unimagdalena.edu.omht.entities.AppointmentType;
import unimagdalena.edu.omht.entities.Doctor;
import unimagdalena.edu.omht.entities.Office;
import unimagdalena.edu.omht.entities.Patient;
import unimagdalena.edu.omht.enums.AppointmentStatus;

public class AppointmentMapper {
    
    public static Appointment toEntity(
        CreateAppointmentRequest request,
        Doctor doctor,
        Patient patient,
        Office office,
        AppointmentType appointmentType
    ){
        return Appointment.builder()
                .startAt(request.startAt())
                .doctor(doctor)
                .patient(patient)
                .office(office)
                .appointmentType(appointmentType)
                .status(AppointmentStatus.SCHEDULED)
                .build();
    }

    public static AppointmentResponse toResponse(Appointment appointment){
        return new AppointmentResponse(
            appointment.getId(),
            appointment.getStartAt(), 
            appointment.getEndAt(), 
            appointment.getStatus(),
            appointment.getDoctor() != null ? appointment.getDoctor().getId() : null, 
            appointment.getDoctor() != null ? appointment.getDoctor().getFullName(): null,
            appointment.getPatient() != null ? appointment.getPatient().getId() : null,
            appointment.getPatient() != null ? appointment.getPatient().getFullName() : null,
            appointment.getOffice() != null ? appointment.getOffice().getId() : null,
            appointment.getOffice() != null ? appointment.getOffice().getLocation() : null,
            appointment.getAppointmentType() != null ? appointment.getAppointmentType().getId() : null,
            appointment.getAppointmentType() != null ? appointment.getAppointmentType().getTitle() : null,
            appointment.getCreatedAt(),
            appointment.getUpdatedAt()
        );
    }
}
