package unimagdalena.edu.omht.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import unimagdalena.edu.omht.dtos.DoctorScheduleDtos.CreateDoctorScheduleRequest;
import unimagdalena.edu.omht.dtos.DoctorScheduleDtos.DoctorScheduleResponse;
import unimagdalena.edu.omht.entities.Doctor;
import unimagdalena.edu.omht.entities.DoctorProfile;
import unimagdalena.edu.omht.entities.DoctorSchedule;
import unimagdalena.edu.omht.entities.Specialty;
import unimagdalena.edu.omht.exceptions.ConflictException;
import unimagdalena.edu.omht.repositories.DoctorRepository;
import unimagdalena.edu.omht.repositories.DoctorScheduleRepository;
import unimagdalena.edu.omht.services.serviceImpl.DoctorScheduleServiceImpl;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public class DoctorScheduleServiceImplTest {

    @Mock private DoctorScheduleRepository scheduleRepository;

    @Mock private DoctorRepository doctorRepository;

    @InjectMocks private DoctorScheduleServiceImpl scheduleService;

    @Test
    void shouldCreateDoctorSchedule(){

        LocalTime startAt = LocalTime.of(8, 0);
        LocalTime endAt = LocalTime.of(12, 0);

        CreateDoctorScheduleRequest request = new CreateDoctorScheduleRequest(
            DayOfWeek.FRIDAY, startAt, endAt);

        DoctorProfile profile = DoctorProfile.builder().phone("3015975842").bio("doctor").build();
        
        UUID specialtyId = UUID.randomUUID();
        UUID doctorId = UUID.randomUUID();
        
        Specialty specialty = Specialty.builder().id(specialtyId).title("Cargiology").build();

        Doctor doctor = Doctor.builder()
            .id(doctorId)
            .fullName("Oscar Turizo")
            .email("oscar@gmail.com")
            .doctorProfile(profile)
            .specialty(specialty)
            .build();

        DoctorSchedule savedSchedule = DoctorSchedule.builder()
            .dayOfWeek(DayOfWeek.FRIDAY)
            .startAt(startAt)
            .endAt(endAt)
            .doctor(doctor)
            .build();

        when(doctorRepository.findById(doctorId)).thenReturn(Optional.of(doctor));
        when(scheduleRepository.save(any(DoctorSchedule.class))).thenReturn(savedSchedule);

        DoctorScheduleResponse response = scheduleService.create(request, doctorId);

        assertThat(response).isNotNull();
        assertThat(response.dayOfWeek()).isEqualTo(DayOfWeek.FRIDAY);
        verify(doctorRepository, times(1)).findById(doctorId);
        verify(scheduleRepository, times(1)).save(any(DoctorSchedule.class));
    }

    @Test
    void shouldThrowExceptionWhenScheduleOverlaps(){

        LocalTime startAt = LocalTime.of(8, 0);
        LocalTime endAt = LocalTime.of(11, 0);

        CreateDoctorScheduleRequest request = new CreateDoctorScheduleRequest(
            DayOfWeek.FRIDAY, startAt, endAt);

        UUID doctorId = UUID.randomUUID();

        Doctor doctor = Doctor.builder()
            .id(doctorId)
            .fullName("Oscar Turizo")
            .email("oscar@gmail.com")
            .build();
            
        when(doctorRepository.findById(doctorId)).thenReturn(Optional.of(doctor));
        when(scheduleRepository.existsOverlappingSchedule(
            doctorId, request.dayOfWeek(), request.startAt(), request.endAt())).thenReturn(true);

        Exception error = assertThrows(ConflictException.class, ()->{
            scheduleService.create(request, doctorId);
        });

        assertThat(error.getMessage()).isEqualTo("An overlapping schedule already exists for this doctor on the selected day");
        verify(scheduleRepository, never()).save(any(DoctorSchedule.class));        
    }
}
