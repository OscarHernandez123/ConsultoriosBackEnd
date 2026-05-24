package unimagdalena.edu.omht.services.service;

import java.util.List;
import java.util.UUID;

import unimagdalena.edu.omht.dtos.DoctorScheduleDtos.CreateDoctorScheduleRequest;
import unimagdalena.edu.omht.dtos.DoctorScheduleDtos.DoctorScheduleResponse;

public interface DoctorScheduleService {
    DoctorScheduleResponse create(CreateDoctorScheduleRequest request);
    List<DoctorScheduleResponse> listAll();
    List<DoctorScheduleResponse> listByDoctor(UUID doctorId);
}
