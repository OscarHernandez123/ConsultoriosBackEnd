package unimagdalena.edu.omht.services.serviceInterface;

import java.util.List;

import unimagdalena.edu.omht.dtos.AppointmentTypeDtos.AppointmentTypeResponse;
import unimagdalena.edu.omht.dtos.AppointmentTypeDtos.CreateAppointmentTypeRequest;

public interface AppointmentTypeService {
    AppointmentTypeResponse create(CreateAppointmentTypeRequest request);
    List<AppointmentTypeResponse> listAll();
}
