package unimagdalena.edu.omht.controllers;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import unimagdalena.edu.omht.dtos.AppointmentTypeDtos.AppointmentTypeResponse;
import unimagdalena.edu.omht.dtos.AppointmentTypeDtos.CreateAppointmentTypeRequest;
import unimagdalena.edu.omht.services.service.AppointmentTypeService;

@RestController
@RequestMapping("/api/appointment-types")
@RequiredArgsConstructor
@Validated
public class AppointmentTypeController {
    
    private final AppointmentTypeService appointmentTypeService;

    @PostMapping
    public ResponseEntity<AppointmentTypeResponse> createAppointmentType(
        @Valid @RequestBody CreateAppointmentTypeRequest request){

            AppointmentTypeResponse appointmentTypeCreated = appointmentTypeService.create(request);
            URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(appointmentTypeCreated.id())
                .toUri();
                
            return ResponseEntity.created(location).body(appointmentTypeCreated);
    }

    @GetMapping
    public ResponseEntity<List<AppointmentTypeResponse>> listAllAppointmentTypes(){
        return ResponseEntity.ok(appointmentTypeService.listAll());
    }
}
