package unimagdalena.edu.omht.dtos;

import java.time.Instant;
import java.util.UUID;

public class ReportDtos {

    public record OfficeOccupancyResponse(
        UUID officeId,
        String officeLocation, 
        Long occupiedMinutes
    ){}

    public record DoctorProductivityResponse(
        UUID doctorId,
        String doctorFullName, 
        Long completedAppointments
    ){}

    // 3. Pacientes que no asisten
    public record NoShowPatientResponse(
        UUID patientId,
        String patientFullName,
        Long noShowAppointments, 
        Instant lastNoShowDate   
    ){}

    public record SpecialtyCancellationResponse(
        UUID specialtyId,
        String specialtyTitle,
        Long cancelledOrNoShowAppointments
    ){}
}
