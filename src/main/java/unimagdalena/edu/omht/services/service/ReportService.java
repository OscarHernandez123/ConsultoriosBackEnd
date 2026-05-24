package unimagdalena.edu.omht.services.service;

import java.time.Instant;
import java.util.List;

import unimagdalena.edu.omht.dtos.ReportDtos.DoctorProductivityResponse;
import unimagdalena.edu.omht.dtos.ReportDtos.NoShowPatientResponse;
import unimagdalena.edu.omht.dtos.ReportDtos.OfficeOccupancyResponse;

public interface ReportService {
    List<OfficeOccupancyResponse> officeOccupancy(Instant startDate, Instant endDate);
    List<DoctorProductivityResponse> doctorProductivity();
    List<NoShowPatientResponse> noShowPatient();
}
