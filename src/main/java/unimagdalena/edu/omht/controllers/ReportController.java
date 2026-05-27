package unimagdalena.edu.omht.controllers;

import java.time.Instant;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import unimagdalena.edu.omht.dtos.ReportDtos.DoctorProductivityResponse;
import unimagdalena.edu.omht.dtos.ReportDtos.NoShowPatientResponse;
import unimagdalena.edu.omht.dtos.ReportDtos.OfficeOccupancyResponse;
import unimagdalena.edu.omht.dtos.ReportDtos.SpecialtyCancellationResponse;
import unimagdalena.edu.omht.services.serviceInterface.ReportService;

@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
@Validated
public class ReportController {

    private final ReportService reportService;

    @GetMapping("/office-occupancy")
    public ResponseEntity<List<OfficeOccupancyResponse>> getOfficeOccupancy(
            @RequestParam("startDate") Instant startDate,
            @RequestParam("endDate") Instant endDate
    ) {
        List<OfficeOccupancyResponse> report = reportService.getOfficeOccupancy(startDate, endDate);
        return ResponseEntity.ok(report);
    }

    @GetMapping("/doctor-productivity")
    public ResponseEntity<List<DoctorProductivityResponse>> getDoctorProductivity() {
        List<DoctorProductivityResponse> report = reportService.getDoctorProductivity();
        return ResponseEntity.ok(report);
    }

    @GetMapping("/no-show-patients")
    public ResponseEntity<List<NoShowPatientResponse>> getNoShowPatients(
            @RequestParam("startDate") Instant startDate,
            @RequestParam("endDate") Instant endDate
    ) {
        List<NoShowPatientResponse> report = reportService.getNoShowPatients(startDate, endDate);
        return ResponseEntity.ok(report);
    }

    @GetMapping("/specialty-cancellations")
    public ResponseEntity<List<SpecialtyCancellationResponse>> getSpecialtyCancellations() {
        List<SpecialtyCancellationResponse> report = reportService.getSpecialtyCancellations();
        return ResponseEntity.ok(report);
    }
}
