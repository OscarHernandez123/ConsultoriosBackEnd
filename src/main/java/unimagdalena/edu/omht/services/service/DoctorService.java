package unimagdalena.edu.omht.services.service;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import unimagdalena.edu.omht.dtos.DoctorDtos.CreateDoctorRequest;
import unimagdalena.edu.omht.dtos.DoctorDtos.DoctorResponse;
import unimagdalena.edu.omht.dtos.DoctorDtos.PatchDoctorRequest;
import unimagdalena.edu.omht.dtos.DoctorDtos.UpdateDoctorRequest;

public interface DoctorService {
    DoctorResponse create(CreateDoctorRequest request);
    DoctorResponse patch(PatchDoctorRequest request, UUID doctorId);
    DoctorResponse update(UpdateDoctorRequest request, UUID doctorId);
    DoctorResponse get(UUID doctorId);
    Page<DoctorResponse> list(Pageable pageable);
}
