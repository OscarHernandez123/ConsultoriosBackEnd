package unimagdalena.edu.omht.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import unimagdalena.edu.omht.dtos.AvailabilityDtos.AvailabilitySlotResponse;
import unimagdalena.edu.omht.entities.Appointment;
import unimagdalena.edu.omht.entities.AppointmentType;
import unimagdalena.edu.omht.entities.Doctor;
import unimagdalena.edu.omht.entities.DoctorSchedule;
import unimagdalena.edu.omht.repositories.AppointmentRepository;
import unimagdalena.edu.omht.repositories.AppointmentTypeRepository;
import unimagdalena.edu.omht.repositories.DoctorRepository;
import unimagdalena.edu.omht.repositories.DoctorScheduleRepository;
import unimagdalena.edu.omht.services.serviceImpl.AvailabilityServiceImpl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import java.time.DayOfWeek;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
public class AvailabilityServiceImplTest {

    @Mock AppointmentRepository appointmentRepository;

    @Mock AppointmentTypeRepository appointmentTypeRepository;

    @Mock DoctorScheduleRepository doctorScheduleRepository;

    @Mock DoctorRepository doctorRepository;

    @InjectMocks AvailabilityServiceImpl availabilityService;
    @Test
    void shouldReturnAvailableSlotsSuccessfully() {
        
        UUID doctorId = UUID.randomUUID();
        UUID typeId = UUID.randomUUID();
        
        LocalDate testDate = LocalDate.of(2026, 5, 29); 
        DayOfWeek dayOfWeek = testDate.getDayOfWeek();
        ZoneId zoneId = ZoneId.systemDefault();

        Doctor doctor = Doctor.builder().id(doctorId).build();
        
        AppointmentType type = AppointmentType.builder()
            .id(typeId)
            .durationMinutes(30)
            .build();

        DoctorSchedule schedule = DoctorSchedule.builder()
            .startAt(LocalTime.of(8, 0))
            .endAt(LocalTime.of(10, 0))
            .build();

        Instant apptStart = testDate.atTime(8, 30).atZone(zoneId).toInstant();
        Instant apptEnd = testDate.atTime(9, 0).atZone(zoneId).toInstant();

        Appointment existingAppt = Appointment.builder()
            .startAt(apptStart)
            .endAt(apptEnd)
            .build();

        when(doctorRepository.findById(doctorId)).thenReturn(Optional.of(doctor));
        when(appointmentTypeRepository.findById(typeId)).thenReturn(Optional.of(type));        
        when(doctorScheduleRepository.findByDoctorIdAndDayOfWeek(doctorId, dayOfWeek))
            .thenReturn(List.of(schedule));
        when(appointmentRepository.findActiveAppointmentsByDoctorAndDate(
            eq(doctorId), any(Instant.class), any(Instant.class)
        )).thenReturn(List.of(existingAppt));

        List<AvailabilitySlotResponse> result = availabilityService.getAvailableSlots(doctorId, testDate, typeId);

        assertThat(result).isNotNull();
        assertThat(result).hasSize(3);

        assertThat(result.get(0).startAt()).isEqualTo(testDate.atTime(8, 0).atZone(zoneId).toInstant());
        assertThat(result.get(1).startAt()).isEqualTo(testDate.atTime(9, 0).atZone(zoneId).toInstant());
        assertThat(result.get(2).startAt()).isEqualTo(testDate.atTime(9, 30).atZone(zoneId).toInstant());
    }
}
