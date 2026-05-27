package unimagdalena.edu.omht.controllers;

import java.net.URI;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import unimagdalena.edu.omht.dtos.PatientDtos.CreatePatientRequest;
import unimagdalena.edu.omht.dtos.PatientDtos.PatientResponse;
import unimagdalena.edu.omht.dtos.PatientDtos.UpdatePatientRequest;
import unimagdalena.edu.omht.services.serviceInterface.PatientService;

@RestController
@RequestMapping("/api/patients")
@RequiredArgsConstructor
@Validated
public class PatientController {

    private final PatientService patientService;

    @PostMapping
    public ResponseEntity<PatientResponse> createPatient(@Valid @RequestBody CreatePatientRequest request){

        PatientResponse patientCreated = patientService.create(request);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(patientCreated.id())
            .toUri();

        return ResponseEntity.created(location).body(patientCreated);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PatientResponse> getPatient(@PathVariable("id") UUID patientId){
        return ResponseEntity.ok(patientService.get(patientId));
    }

    @GetMapping
    public ResponseEntity<Page<PatientResponse>> getAllPatients(
        @RequestParam (defaultValue = "0") int page,
        @RequestParam (defaultValue = "10") int size
    ){
        Page<PatientResponse> result = patientService
            .list(PageRequest.of(page, size, Sort.by("id").ascending()));
        return ResponseEntity.ok(result);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PatientResponse> updatePatient(
        @PathVariable("id") UUID patientId,
        @Valid @RequestBody UpdatePatientRequest request
    ){
        return ResponseEntity.ok(patientService.update(request, patientId));
    }
}
