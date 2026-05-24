package unimagdalena.edu.omht.services.service;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import unimagdalena.edu.omht.dtos.PatientDtos.CreatePatientRequest;
import unimagdalena.edu.omht.dtos.PatientDtos.PatchPatientRequest;
import unimagdalena.edu.omht.dtos.PatientDtos.PatientResponse;
import unimagdalena.edu.omht.dtos.PatientDtos.UpdatePatientRequest;

public interface PatientService {
    PatientResponse create(CreatePatientRequest request);
    PatientResponse patch(PatchPatientRequest request, UUID patientId);
    PatientResponse update(UpdatePatientRequest request, UUID patientId);
    PatientResponse get(UUID patientId);
    Page<PatientResponse> list(Pageable pageable);
}
