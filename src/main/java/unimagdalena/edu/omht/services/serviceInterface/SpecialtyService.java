package unimagdalena.edu.omht.services.serviceInterface;

import java.util.List;

import unimagdalena.edu.omht.dtos.SpecialtyDtos.CreateSpecialtyRequest;
import unimagdalena.edu.omht.dtos.SpecialtyDtos.SpecialtyResponse;

public interface SpecialtyService {
    SpecialtyResponse create(CreateSpecialtyRequest request);
    List<SpecialtyResponse> listAll();
}
