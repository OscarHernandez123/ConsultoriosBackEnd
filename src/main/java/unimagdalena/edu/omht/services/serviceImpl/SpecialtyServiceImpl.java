package unimagdalena.edu.omht.services.serviceImpl;

import java.util.List;

import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import unimagdalena.edu.omht.dtos.SpecialtyDtos.CreateSpecialtyRequest;
import unimagdalena.edu.omht.dtos.SpecialtyDtos.SpecialtyResponse;
import unimagdalena.edu.omht.entities.Specialty;
import unimagdalena.edu.omht.mappers.SpecialtyMapper;
import unimagdalena.edu.omht.repositories.SpecialtyRepository;
import unimagdalena.edu.omht.services.serviceInterface.SpecialtyService;

@Service
@RequiredArgsConstructor
@Transactional
public class SpecialtyServiceImpl implements SpecialtyService{

    private final SpecialtyRepository specialtyRepository;

    @Override
    public SpecialtyResponse create(CreateSpecialtyRequest request) {

        Specialty specialty = SpecialtyMapper.toEntity(request);

        Specialty saved = specialtyRepository.save(specialty);
        
        return SpecialtyMapper.toResponse(saved);
    }

    @Override
    public List<SpecialtyResponse> listAll() {

        return specialtyRepository.findAll()
            .stream()
            .map(SpecialtyMapper::toResponse)
            .toList();
    }

}
