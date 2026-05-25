package unimagdalena.edu.omht.services.serviceImpl;

import java.util.List;

import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import unimagdalena.edu.omht.dtos.AppointmentTypeDtos.AppointmentTypeResponse;
import unimagdalena.edu.omht.dtos.AppointmentTypeDtos.CreateAppointmentTypeRequest;
import unimagdalena.edu.omht.entities.AppointmentType;
import unimagdalena.edu.omht.mappers.AppointmentTypeMapper;
import unimagdalena.edu.omht.repositories.AppointmentTypeRepository;
import unimagdalena.edu.omht.services.service.AppointmentTypeService;

@Service
@RequiredArgsConstructor
@Transactional
public class AppointmentTypeServiceImpl implements AppointmentTypeService{

    private final AppointmentTypeRepository appointmentTypeRepository;

    @Override
    public AppointmentTypeResponse create(CreateAppointmentTypeRequest request) {

        AppointmentType appointmentType = AppointmentTypeMapper.toEntity(request);
        
        AppointmentType saved = appointmentTypeRepository.save(appointmentType);

        return AppointmentTypeMapper.toResponse(saved);
    }

    @Override
    public List<AppointmentTypeResponse> listAll() {
        return appointmentTypeRepository.findAll()
            .stream()
            .map(AppointmentTypeMapper::toResponse)
            .toList();
    }
    
}
