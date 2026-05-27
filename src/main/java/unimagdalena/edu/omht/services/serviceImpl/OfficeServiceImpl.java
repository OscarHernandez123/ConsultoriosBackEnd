package unimagdalena.edu.omht.services.serviceImpl;

import java.time.Instant;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import unimagdalena.edu.omht.dtos.OfficeDtos.CreateOfficeRequest;
import unimagdalena.edu.omht.dtos.OfficeDtos.OfficeResponse;
import unimagdalena.edu.omht.dtos.OfficeDtos.PatchOfficeRequest;
import unimagdalena.edu.omht.dtos.OfficeDtos.UpdateOfficeRequest;
import unimagdalena.edu.omht.entities.Office;
import unimagdalena.edu.omht.enums.OfficeStatus;
import unimagdalena.edu.omht.exceptions.ResourceNotFoundException;
import unimagdalena.edu.omht.mappers.OfficeMapper;
import unimagdalena.edu.omht.repositories.OfficeRepository;
import unimagdalena.edu.omht.services.serviceInterface.OfficeService;

@Service
@RequiredArgsConstructor
@Transactional
public class OfficeServiceImpl implements OfficeService{

    private final OfficeRepository officeRepository;
    
    @Override
    public OfficeResponse create(CreateOfficeRequest request) {
        
        Office office = OfficeMapper.toEntity(request);
        office.setStatus(OfficeStatus.ACTIVE);

        Office saved = officeRepository.save(office);

        return OfficeMapper.toResponse(saved);
    }

    @Override
    public OfficeResponse patch(PatchOfficeRequest request, UUID officeId) {

        Office office = officeRepository.findById(officeId)
            .orElseThrow(() -> new ResourceNotFoundException("Office not found"));

        OfficeMapper.patch(office, request);

        office.setUpdatedAt(Instant.now());
        Office saved = officeRepository.save(office);

        return OfficeMapper.toResponse(saved);
    }

    @Override
    public OfficeResponse update(UpdateOfficeRequest request, UUID officeId) {

        Office office = officeRepository.findById(officeId)
            .orElseThrow(() -> new ResourceNotFoundException("Office not found"));

        OfficeMapper.update(office, request);

        office.setUpdatedAt(Instant.now());
        Office saved = officeRepository.save(office);

        return OfficeMapper.toResponse(saved);
    }

    @Override
    public OfficeResponse get(UUID officeId) {

        Office office = officeRepository.findById(officeId)
            .orElseThrow(() -> new ResourceNotFoundException("Office  not found"));

        return OfficeMapper.toResponse(office);
    }

    @Override
    public Page<OfficeResponse> list(Pageable pageable) {
        return officeRepository.findAll(pageable).map(OfficeMapper::toResponse);
    }

}
