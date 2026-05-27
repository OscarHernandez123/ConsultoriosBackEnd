package unimagdalena.edu.omht.controllers;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import unimagdalena.edu.omht.dtos.SpecialtyDtos.CreateSpecialtyRequest;
import unimagdalena.edu.omht.dtos.SpecialtyDtos.SpecialtyResponse;
import unimagdalena.edu.omht.services.serviceInterface.SpecialtyService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/api/specialties")
@RequiredArgsConstructor
@Validated
public class SpecialtyController {

    private final SpecialtyService specialtyService;

    @PostMapping
    public ResponseEntity<SpecialtyResponse> createSpecialty(@Valid @RequestBody CreateSpecialtyRequest request){
        
        SpecialtyResponse specialtyCreated = specialtyService.create(request);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(specialtyCreated.id())
            .toUri();

        return ResponseEntity.created(location).body(specialtyCreated);
    }

    @GetMapping
    public ResponseEntity<List<SpecialtyResponse>> getAllSpecialties(){
        return ResponseEntity.ok(specialtyService.listAll());
    }    
}
