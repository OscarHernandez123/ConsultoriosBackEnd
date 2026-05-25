package unimagdalena.edu.omht.services.serviceImpl;

import java.time.DayOfWeek;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import unimagdalena.edu.omht.dtos.AvailabilityDtos.AvailabilitySlotResponse;
import unimagdalena.edu.omht.entities.Appointment;
import unimagdalena.edu.omht.entities.AppointmentType;
import unimagdalena.edu.omht.entities.Doctor;
import unimagdalena.edu.omht.entities.DoctorSchedule;
import unimagdalena.edu.omht.exceptions.ResourceNotFoundException;
import unimagdalena.edu.omht.repositories.AppointmentRepository;
import unimagdalena.edu.omht.repositories.AppointmentTypeRepository;
import unimagdalena.edu.omht.repositories.DoctorRepository;
import unimagdalena.edu.omht.repositories.DoctorScheduleRepository;
import unimagdalena.edu.omht.services.service.AvailabilityService;

@Service
@RequiredArgsConstructor
@Transactional
public class AvailabilityServiceImpl implements AvailabilityService{

    private final DoctorRepository doctorRepository;

    private final AppointmentTypeRepository appointmentTypeRepository;

    private final DoctorScheduleRepository doctorScheduleRepository;

    private final AppointmentRepository appointmentRepository;
    
    @Override
    public List<AvailabilitySlotResponse> getAvailableSlots(UUID doctorId, LocalDate date, UUID appointmentTypeId) {
        
        Doctor doctor = doctorRepository.findById(doctorId)
            .orElseThrow(() -> new ResourceNotFoundException("Doctor not found"));

        AppointmentType appointmentType = appointmentTypeRepository.findById(appointmentTypeId)
            .orElseThrow(() -> new ResourceNotFoundException("Appointment Type not found"));

        DayOfWeek dayOfWeek = date.getDayOfWeek();
        
        List<DoctorSchedule> doctorSchedules = doctorScheduleRepository
            .findByDoctorIdAndDayOfWeek(doctor.getId(), dayOfWeek);

        if(doctorSchedules.isEmpty()){
            return List.of();
        }

        ZoneId zoneId = ZoneId.systemDefault();
        Instant startOfDay = date.atStartOfDay(zoneId).toInstant(); 
        Instant endOfDay = date.plusDays(1).atStartOfDay(zoneId).toInstant();

        List<Appointment> appointments = appointmentRepository
            .findActiveAppointmentsByDoctorAndDate(doctor.getId(), startOfDay, endOfDay);

        List<AvailabilitySlotResponse> availableSlots = new ArrayList<>();

        for(DoctorSchedule schedule : doctorSchedules){
            
            Instant currentPointer = date.atTime(schedule.getStartAt()).atZone(zoneId).toInstant();
            Instant shiftEnd = date.atTime(schedule.getEndAt()).atZone(zoneId).toInstant();

            while(!currentPointer.plus(appointmentType.getDurationMinutes(), ChronoUnit.MINUTES).isAfter(shiftEnd)){

                Instant slotEnd = currentPointer.plus(appointmentType.getDurationMinutes(), ChronoUnit.MINUTES);

                Appointment overlappingAppt = null;

                for(Appointment appt : appointments){
                    if(appt.getStartAt().isBefore(slotEnd) && appt.getEndAt().isAfter(currentPointer)){
                        overlappingAppt = appt;
                        break;
                    }
                }

                if(overlappingAppt != null){
                    currentPointer = overlappingAppt.getEndAt();                    
                } else {
                    AvailabilitySlotResponse cleanSlot = new AvailabilitySlotResponse(currentPointer, slotEnd);
                    availableSlots.add(cleanSlot);
                    currentPointer = slotEnd;
                }
            }
        }

        return availableSlots;
        
    }
}
