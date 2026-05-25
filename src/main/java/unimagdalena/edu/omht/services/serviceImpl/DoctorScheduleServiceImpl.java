package unimagdalena.edu.omht.services.serviceImpl;

import java.time.DayOfWeek;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import unimagdalena.edu.omht.dtos.DoctorScheduleDtos.CreateDoctorScheduleRequest;
import unimagdalena.edu.omht.dtos.DoctorScheduleDtos.DoctorScheduleResponse;
import unimagdalena.edu.omht.entities.Doctor;
import unimagdalena.edu.omht.entities.DoctorSchedule;
import unimagdalena.edu.omht.exceptions.ConflictException;
import unimagdalena.edu.omht.exceptions.ResourceNotFoundException;
import unimagdalena.edu.omht.mappers.DoctorScheduleMapper;
import unimagdalena.edu.omht.repositories.DoctorRepository;
import unimagdalena.edu.omht.repositories.DoctorScheduleRepository;
import unimagdalena.edu.omht.services.service.DoctorScheduleService;

@Service
@RequiredArgsConstructor
@Transactional
public class DoctorScheduleServiceImpl implements DoctorScheduleService{
    
    private final DoctorScheduleRepository doctorScheduleRepository;

    private final DoctorRepository doctorRepository;

    @Override
    public DoctorScheduleResponse create(CreateDoctorScheduleRequest request) {

        Doctor doctor = doctorRepository.findById(request.doctorId())
            .orElseThrow(() -> new ResourceNotFoundException("DoctorNotFound")); 
            
        if(!request.startAt().isBefore(request.endAt())){
            throw new ValidationException("Invalid time range");
        }

        if(doctorScheduleRepository.existsOverlappingSchedule(
            doctor.getId(), request.dayOfWeek(), request.startAt(), request.endAt())){
                throw new ConflictException("An overlapping schedule already exists for this doctor on the selected day");
        }

        DoctorSchedule schedule = DoctorScheduleMapper.toEntity(request, doctor);

        DoctorSchedule saved = doctorScheduleRepository.save(schedule);

        return DoctorScheduleMapper.toResponse(saved);
    }

    @Override
    public List<DoctorScheduleResponse> listAll() {
        return doctorScheduleRepository.findAll()
            .stream()
            .map(DoctorScheduleMapper::toResponse)
            .toList();
    }

    @Override
    public List<DoctorScheduleResponse> listByDoctor(UUID doctorId, DayOfWeek dayOfWeek) {

        Doctor doctor = doctorRepository.findById(doctorId)
            .orElseThrow(() -> new ResourceNotFoundException("DoctorNotFound")); 

        return doctorScheduleRepository.findByDoctorIdAndDayOfWeek(doctor.getId(), dayOfWeek)
            .stream()
            .map(DoctorScheduleMapper::toResponse)
            .toList();
    }

}
