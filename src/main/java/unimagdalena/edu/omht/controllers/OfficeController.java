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
import unimagdalena.edu.omht.dtos.OfficeDtos.CreateOfficeRequest;
import unimagdalena.edu.omht.dtos.OfficeDtos.OfficeResponse;
import unimagdalena.edu.omht.dtos.OfficeDtos.UpdateOfficeRequest;
import unimagdalena.edu.omht.services.serviceInterface.OfficeService;

@RestController
@RequestMapping("/api/offices")
@RequiredArgsConstructor
@Validated
public class OfficeController {

    private final OfficeService officeService;

    @PostMapping
    public ResponseEntity<OfficeResponse> createOffice(@Valid @RequestBody CreateOfficeRequest request){

        OfficeResponse officeCreated = officeService.create(request);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
            .path("/{id]")
            .buildAndExpand(officeCreated.id())
            .toUri();

        return ResponseEntity.created(location).body(officeCreated);
    }

    @GetMapping
    public ResponseEntity<Page<OfficeResponse>> getAllOffices(
        @RequestParam (defaultValue = "0") int page,
        @RequestParam (defaultValue = "10") int size
    ){
        Page<OfficeResponse> result = officeService
            .list(PageRequest.of(page, size, Sort.by("id").ascending()));

        return ResponseEntity.ok(result);
    }

    @PutMapping("/{id}")
    public ResponseEntity<OfficeResponse> updateOffice(
        @PathVariable("id") UUID officeId,
        @Valid @RequestBody UpdateOfficeRequest request
    ){
        return ResponseEntity.ok(officeService.update(request, officeId));
    }

}
