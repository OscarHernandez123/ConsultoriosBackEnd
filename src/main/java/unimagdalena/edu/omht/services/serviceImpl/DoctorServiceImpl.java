package unimagdalena.edu.omht.services.serviceImpl;

import java.time.Instant;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import unimagdalena.edu.omht.dtos.DoctorDtos.CreateDoctorRequest;
import unimagdalena.edu.omht.dtos.DoctorDtos.DoctorResponse;
import unimagdalena.edu.omht.dtos.DoctorDtos.PatchDoctorRequest;
import unimagdalena.edu.omht.dtos.DoctorDtos.UpdateDoctorRequest;
import unimagdalena.edu.omht.entities.Doctor;
import unimagdalena.edu.omht.entities.Specialty;
import unimagdalena.edu.omht.exceptions.ResourceNotFoundException;
import unimagdalena.edu.omht.mappers.DoctorMapper;
import unimagdalena.edu.omht.repositories.DoctorRepository;
import unimagdalena.edu.omht.repositories.SpecialtyRepository;
import unimagdalena.edu.omht.services.serviceInterface.DoctorService;

@Service
@RequiredArgsConstructor
@Transactional
public class DoctorServiceImpl implements DoctorService{
    
    private final DoctorRepository doctorRepository;

    private final SpecialtyRepository specialtyRepository;

    @Override
    public DoctorResponse create(CreateDoctorRequest request) {
        
        Specialty specialty = specialtyRepository.findById(request.specialtyId())
            .orElseThrow(() -> new ResourceNotFoundException("Specialty not found"));

        Doctor doctor = DoctorMapper.toEntity(request, specialty);

        Doctor saved = doctorRepository.save(doctor);

        return DoctorMapper.toResponse(saved);
    }

    @Override
    public DoctorResponse patch(PatchDoctorRequest request, UUID doctorId) {

        Specialty specialty = specialtyRepository.findById(request.specialtyId())
            .orElseThrow(() -> new ResourceNotFoundException("Specialty not found"));

        Doctor doctor = doctorRepository.findById(doctorId)
            .orElseThrow(() -> new ResourceNotFoundException("Doctor not found"));
        
        DoctorMapper.patch(doctor, request, specialty);
        doctor.setUpdatedAt(Instant.now());

        Doctor saved = doctorRepository.save(doctor);

        return DoctorMapper.toResponse(saved);
    }

    @Override
    public DoctorResponse update(UpdateDoctorRequest request, UUID doctorId) {
        
        Specialty specialty = specialtyRepository.findById(request.specialtyId())
            .orElseThrow(() -> new ResourceNotFoundException("Specialty not found"));

        Doctor doctor = doctorRepository.findById(doctorId)
            .orElseThrow(() -> new ResourceNotFoundException("Doctor not found"));
        
        DoctorMapper.update(doctor, request, specialty);
        doctor.setUpdatedAt(Instant.now());

        Doctor saved = doctorRepository.save(doctor);

        return DoctorMapper.toResponse(saved);
    }

    @Override
    public DoctorResponse get(UUID doctorId) {
        
        Doctor doctor = doctorRepository.findById(doctorId)
            .orElseThrow(() -> new ResourceNotFoundException("Doctor not found"));

        return DoctorMapper.toResponse(doctor);
    }

    @Override
    public Page<DoctorResponse> list(Pageable pageable) {
        return doctorRepository.findAll(pageable).map(DoctorMapper::toResponse);
    }


}
