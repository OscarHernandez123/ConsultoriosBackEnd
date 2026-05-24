package unimagdalena.edu.omht.services.service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import unimagdalena.edu.omht.dtos.AvailabilityDtos.AvailabilitySlotResponse;

public interface AvailabilityService {
    List<AvailabilitySlotResponse> getAvailableSlots(UUID doctorId, LocalDate date);
}
