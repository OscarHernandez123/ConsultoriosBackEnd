package unimagdalena.edu.omht.services.service;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import unimagdalena.edu.omht.dtos.AppointmentDtos.AppointmentCancelRequest;
import unimagdalena.edu.omht.dtos.AppointmentDtos.AppointmentCompleteRequest;
import unimagdalena.edu.omht.dtos.AppointmentDtos.AppointmentResponse;
import unimagdalena.edu.omht.dtos.AppointmentDtos.CreateAppointmentRequest;

public interface AppointmentService {
    AppointmentResponse create(CreateAppointmentRequest request);
    AppointmentResponse confirm(UUID appointmentId);
    AppointmentResponse cancel(UUID appointmentId, AppointmentCancelRequest reasonRequest);
    AppointmentResponse complete(UUID appointmentId, AppointmentCompleteRequest administrativeNoteRequest);
    AppointmentResponse markNoShow(UUID appointmentId);
    AppointmentResponse get(UUID appointmentId);
    Page<AppointmentResponse> list(Pageable pageable);
}
