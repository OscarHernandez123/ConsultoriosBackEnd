package unimagdalena.edu.omht.services.service;

import java.time.Instant;
import java.util.List;

import unimagdalena.edu.omht.dtos.ReportDtos.DoctorProductivityResponse;
import unimagdalena.edu.omht.dtos.ReportDtos.NoShowPatientResponse;
import unimagdalena.edu.omht.dtos.ReportDtos.OfficeOccupancyResponse;
import unimagdalena.edu.omht.dtos.ReportDtos.SpecialtyCancellationResponse;

public interface ReportService {
    List<OfficeOccupancyResponse> getOfficeOccupancy(Instant startDate, Instant endDate);
    List<DoctorProductivityResponse> getDoctorProductivity();
    List<NoShowPatientResponse> getNoShowPatients(Instant startDate, Instant endDate);
    List<SpecialtyCancellationResponse> getSpecialtyCancellations();
}
