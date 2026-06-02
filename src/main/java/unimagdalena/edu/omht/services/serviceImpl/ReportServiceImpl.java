package unimagdalena.edu.omht.services.serviceImpl;

import java.time.Instant;
import java.util.List;

import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import unimagdalena.edu.omht.dtos.ReportDtos.DoctorProductivityResponse;
import unimagdalena.edu.omht.dtos.ReportDtos.NoShowPatientResponse;
import unimagdalena.edu.omht.dtos.ReportDtos.OfficeOccupancyResponse;
import unimagdalena.edu.omht.dtos.ReportDtos.SpecialtyCancellationResponse;
import unimagdalena.edu.omht.repositories.AppointmentRepository;
import unimagdalena.edu.omht.services.serviceInterface.ReportService;

@Service
@RequiredArgsConstructor
@Transactional
public class ReportServiceImpl implements ReportService{

    private final AppointmentRepository appointmentRepository;
    
    @Override
    public List<OfficeOccupancyResponse> getOfficeOccupancy(Instant startDate, Instant endDate) {
        return appointmentRepository.getOfficeOccupation(startDate, endDate)
            .stream()
            .map(p -> new OfficeOccupancyResponse(
                p.getOfficeId(),
                p.getName(),
                p.getOccupiedMinutes() != null ? p.getOccupiedMinutes() : 0L
            ))
            .toList();
    }

    @Override
    public List<DoctorProductivityResponse> getDoctorProductivity() {
        return appointmentRepository.getDoctorCompletedAppointment()
            .stream()
            .map(p -> new DoctorProductivityResponse(
                p.getDoctorId(),
                p.getFullName(),
                p.getCompletedAppointments()
            ))
            .toList();

    }

    @Override
    public List<NoShowPatientResponse> getNoShowPatients(Instant startDate, Instant endDate) {
        return appointmentRepository.getPatientbyAppointmentsNoShow(startDate, endDate)
            .stream()
            .map(p -> new NoShowPatientResponse(
                p.getPatientId(),
                p.getFullName(),
                p.getNoShowAppointments(),
                p.getLastNoShowDate()
            ))
            .toList();
    }

    @Override
    public List<SpecialtyCancellationResponse> getSpecialtyCancellations() {
        return appointmentRepository.countCancelledOrNoShowAppointmentBySpecialty()
                .stream()
                .map(p -> new SpecialtyCancellationResponse(
                        p.getSpecialtyId(),
                        p.getTitle(),
                        p.getCancelledOrNoShowAppointment()
                ))
                .toList();
    }

}
