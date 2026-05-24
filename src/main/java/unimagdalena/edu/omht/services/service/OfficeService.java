package unimagdalena.edu.omht.services.service;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import unimagdalena.edu.omht.dtos.OfficeDtos.CreateOfficeRequest;
import unimagdalena.edu.omht.dtos.OfficeDtos.OfficeResponse;
import unimagdalena.edu.omht.dtos.OfficeDtos.PatchOfficeRequest;
import unimagdalena.edu.omht.dtos.OfficeDtos.UpdateOfficeRequest;

public interface OfficeService {
    OfficeResponse create(CreateOfficeRequest request);
    OfficeResponse patch(PatchOfficeRequest request, UUID officeId);
    OfficeResponse update(UpdateOfficeRequest request, UUID officeId);
    OfficeResponse get(UUID officeId);
    Page<OfficeResponse> list(Pageable pageable);
}
