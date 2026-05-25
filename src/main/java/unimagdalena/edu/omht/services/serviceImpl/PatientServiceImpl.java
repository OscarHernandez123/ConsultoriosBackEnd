package unimagdalena.edu.omht.services.serviceImpl;

import java.time.Instant;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import unimagdalena.edu.omht.dtos.PatientDtos.CreatePatientRequest;
import unimagdalena.edu.omht.dtos.PatientDtos.PatchPatientRequest;
import unimagdalena.edu.omht.dtos.PatientDtos.PatientResponse;
import unimagdalena.edu.omht.dtos.PatientDtos.UpdatePatientRequest;
import unimagdalena.edu.omht.entities.Patient;
import unimagdalena.edu.omht.enums.PatientStatus;
import unimagdalena.edu.omht.exceptions.ResourceNotFoundException;
import unimagdalena.edu.omht.mappers.PatientMapper;
import unimagdalena.edu.omht.repositories.PatientRepository;
import unimagdalena.edu.omht.services.service.PatientService;

@Service
@RequiredArgsConstructor
@Transactional
public class PatientServiceImpl implements PatientService{

    private final PatientRepository patientRepository;

    @Override
    public PatientResponse create(CreatePatientRequest request) {

        Patient patient = PatientMapper.toEntity(request);
        patient.setStatus(PatientStatus.ACTIVE);

        Patient saved = patientRepository.save(patient);

        return PatientMapper.toResponse(saved);
    }

    @Override
    public PatientResponse patch(PatchPatientRequest request, UUID patientId) {
        
        Patient patient = patientRepository.findById(patientId)
            .orElseThrow(() -> new ResourceNotFoundException("Patient not found"));

        PatientMapper.patch(patient, request);

        patient.setUpdatedAt(Instant.now());
        Patient saved = patientRepository.save(patient);

        return PatientMapper.toResponse(saved);
    }

    @Override
    public PatientResponse update(UpdatePatientRequest request, UUID patientId) {

        Patient patient = patientRepository.findById(patientId)
            .orElseThrow(() -> new ResourceNotFoundException("Patient not found"));

        PatientMapper.update(patient, request);

        patient.setUpdatedAt(Instant.now());
        Patient saved = patientRepository.save(patient);

        return PatientMapper.toResponse(saved);
    }

    @Override
    public PatientResponse get(UUID patientId) {

        Patient patient = patientRepository.findById(patientId)
            .orElseThrow(() -> new ResourceNotFoundException("Patient not found"));

        return PatientMapper.toResponse(patient);   
    }

    @Override
    public Page<PatientResponse> list(Pageable pageable) {
        return patientRepository.findAll(pageable).map(PatientMapper::toResponse);
    }

}
