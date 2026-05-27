package unimagdalena.edu.omht.controllers;

import java.net.URI;
import java.time.DayOfWeek;
import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import unimagdalena.edu.omht.dtos.DoctorScheduleDtos.CreateDoctorScheduleRequest;
import unimagdalena.edu.omht.dtos.DoctorScheduleDtos.DoctorScheduleResponse;
import unimagdalena.edu.omht.services.serviceInterface.DoctorScheduleService;

@RestController
@RequestMapping("/api/doctors/{doctorId}/schedules")
@RequiredArgsConstructor
@Validated
public class DoctorScheduleController {

    private final DoctorScheduleService doctorScheduleService;

    @PostMapping
    public ResponseEntity<DoctorScheduleResponse> createSchedule(
        @PathVariable("doctorId") UUID doctorId,
        @Valid @RequestBody CreateDoctorScheduleRequest request
    ){

        DoctorScheduleResponse scheduleCreated = doctorScheduleService.create(request, doctorId);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(scheduleCreated.id())
            .toUri();
        
            return ResponseEntity.created(location).body(scheduleCreated);
    }

    @GetMapping
    public ResponseEntity<List<DoctorScheduleResponse>> getDoctorSchedules(
        @PathVariable("doctorId") UUID doctorId,
        @RequestParam("dayOfWeek") DayOfWeek dayOfWeek
    ){
        List<DoctorScheduleResponse> schedules = doctorScheduleService.listByDoctor(doctorId, dayOfWeek);
        return ResponseEntity.ok(schedules);
    }
}
