package unimagdalena.edu.omht.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.github.dockerjava.api.exception.ConflictException;

import unimagdalena.edu.omht.dtos.AppointmentDtos.AppointmentCancelRequest;
import unimagdalena.edu.omht.dtos.AppointmentDtos.AppointmentCompleteRequest;
import unimagdalena.edu.omht.dtos.AppointmentDtos.AppointmentResponse;
import unimagdalena.edu.omht.dtos.AppointmentDtos.CreateAppointmentRequest;
import unimagdalena.edu.omht.entities.Appointment;
import unimagdalena.edu.omht.entities.AppointmentType;
import unimagdalena.edu.omht.entities.Doctor;
import unimagdalena.edu.omht.entities.Office;
import unimagdalena.edu.omht.entities.Patient;
import unimagdalena.edu.omht.enums.AppointmentStatus;
import unimagdalena.edu.omht.enums.OfficeStatus;
import unimagdalena.edu.omht.enums.PatientStatus;
import unimagdalena.edu.omht.exceptions.BusinessException;
import unimagdalena.edu.omht.repositories.AppointmentRepository;
import unimagdalena.edu.omht.repositories.AppointmentTypeRepository;
import unimagdalena.edu.omht.repositories.DoctorRepository;
import unimagdalena.edu.omht.repositories.DoctorScheduleRepository;
import unimagdalena.edu.omht.repositories.OfficeRepository;
import unimagdalena.edu.omht.repositories.PatientRepository;
import unimagdalena.edu.omht.services.serviceImpl.AppointmentServiceImpl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import java.util.Collections;
import java.time.DayOfWeek;
import java.time.Instant;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
public class AppointmentServiceImplTest {

    @Mock AppointmentRepository appointmentRepository;

    @Mock AppointmentTypeRepository typeRepository;

    @Mock DoctorRepository doctorRepository;

    @Mock PatientRepository patientRepository;

    @Mock OfficeRepository officeRepository;

    @Mock DoctorScheduleRepository scheduleRepository;

    @InjectMocks AppointmentServiceImpl appointmentService;

    private UUID doctorId, patientId, officeId, typeId;

    private Doctor doctor;

    private Patient patient;

    private Office office;

    private AppointmentType type;

    @BeforeEach
    void setUp() {

        doctorId = UUID.randomUUID();
        patientId = UUID.randomUUID();
        officeId = UUID.randomUUID();
        typeId = UUID.randomUUID();

        doctor = Doctor.builder()
            .id(doctorId)
            .fullName("Oscar Turizo")
            .email("oscar@gmail.com")
            .build();

        patient = Patient.builder()
            .id(patientId)
            .fullName("Manuel Hernandez")
            .email("manuel@gmail.com")
            .status(PatientStatus.ACTIVE)
            .build();

        office = Office.builder()
            .id(officeId)
            .location("101")
            .status(OfficeStatus.ACTIVE)
            .build();

        type = AppointmentType.builder()
            .id(typeId)
            .title("Cardiology")
            .durationMinutes(30)
            .build();

    }

    @Test
    void shouldCreateAppointmentAndCalculateEndAtCorrectly(){

        Instant startAt = Instant.now();
        Instant endAtTest = Instant.now().plus(30, ChronoUnit.MINUTES);

        CreateAppointmentRequest request = new CreateAppointmentRequest(
            startAt, doctorId, patientId, officeId, typeId);

        Appointment savedAppointment = Appointment.builder()
            .id(UUID.randomUUID())
            .doctor(doctor)
            .patient(patient)
            .office(office)
            .appointmentType(type)
            .build();

        when(doctorRepository.findById(doctorId)).thenReturn(Optional.of(doctor));
        when(patientRepository.findById(patientId)).thenReturn(Optional.of(patient));
        when(officeRepository.findById(officeId)).thenReturn(Optional.of(office));
        when(typeRepository.findById(typeId)).thenReturn(Optional.of(type));
        when(appointmentRepository.save(any(Appointment.class))).thenReturn(savedAppointment);
        
        AppointmentResponse response = appointmentService.create(request);

        assertThat(response).isNotNull();
        assertThat(response.doctorFullName()).isEqualTo("Oscar Turizo");
        assertThat(response.endAt()).isEqualTo(endAtTest);
        verify(doctorRepository, times(1)).findById(doctorId);
        verify(patientRepository, times(1)).findById(patientId);
        verify(officeRepository, times(1)).findById(officeId);
        verify(typeRepository, times(1)).findById(typeId);
        verify(appointmentRepository, times(1)).save(any(Appointment.class));
    }

    @Test
    void shouldThrowExceptionWhenAppointmentIsInThePast(){

        Instant startAt = Instant.now().minus(1, ChronoUnit.HOURS);

        CreateAppointmentRequest request = new CreateAppointmentRequest(
            startAt, doctorId, patientId, officeId, typeId);
            
        when(doctorRepository.findById(doctorId)).thenReturn(Optional.of(doctor));
        when(patientRepository.findById(patientId)).thenReturn(Optional.of(patient));
        when(officeRepository.findById(officeId)).thenReturn(Optional.of(office));
        when(typeRepository.findById(typeId)).thenReturn(Optional.of(type));

        Exception error = assertThrows(BusinessException.class, () -> {
            appointmentService.create(request);
        });

        assertThat(error.getMessage()).isEqualTo("Appointment can not be created in the past");
        verify(appointmentRepository, never()).save(any(Appointment.class));
    }

    @Test
    void shouldThrowExceptionWhenDoctorHasOverlappingAppointment(){

        Instant startAt = Instant.now().plus(1, ChronoUnit.DAYS);

        CreateAppointmentRequest request = new CreateAppointmentRequest(
            startAt, doctorId, patientId, officeId, typeId);
            
        when(doctorRepository.findById(doctorId)).thenReturn(Optional.of(doctor));
        when(patientRepository.findById(patientId)).thenReturn(Optional.of(patient));
        when(officeRepository.findById(officeId)).thenReturn(Optional.of(office));
        when(typeRepository.findById(typeId)).thenReturn(Optional.of(type));
        when(appointmentRepository.existsOverlappingDoctorAppointment(eq(doctorId), eq(request.startAt()), any(Instant.class)))
            .thenReturn(true);

        Exception error = assertThrows(ConflictException.class, () -> {
            appointmentService.create(request);
        });

        assertThat(error.getMessage()).isEqualTo("Doctor is already booked for this time slot");
        verify(appointmentRepository, never()).save(any(Appointment.class));

    }

    @Test
    void shouldThrowExceptionWhenOfficeHasOverlappingAppointment(){

        Instant startAt = Instant.now().plus(1, ChronoUnit.DAYS);

        CreateAppointmentRequest request = new CreateAppointmentRequest(
            startAt, doctorId, patientId, officeId, typeId);
            
        when(doctorRepository.findById(doctorId)).thenReturn(Optional.of(doctor));
        when(patientRepository.findById(patientId)).thenReturn(Optional.of(patient));
        when(officeRepository.findById(officeId)).thenReturn(Optional.of(office));
        when(typeRepository.findById(typeId)).thenReturn(Optional.of(type));
        when(appointmentRepository.existsOverlappingOfficeAppointment(eq(officeId), eq(request.startAt()), any(Instant.class)))
            .thenReturn(true);

        Exception error = assertThrows(ConflictException.class, () -> {
            appointmentService.create(request);
        });

        assertThat(error.getMessage()).isEqualTo("Office is already booked for this time slot");
        verify(appointmentRepository, never()).save(any(Appointment.class));
    }

    @Test
    void shouldThrowExceptionWhenAppointmentIsOutsideDoctorSchedule(){

        Instant startAt = Instant.now().plus(1, ChronoUnit.DAYS);
        DayOfWeek requestedDay = startAt.atZone(ZoneId.systemDefault()).getDayOfWeek();

        CreateAppointmentRequest request = new CreateAppointmentRequest(
            startAt, doctorId, patientId, officeId, typeId);
            
        when(doctorRepository.findById(doctorId)).thenReturn(Optional.of(doctor));
        when(patientRepository.findById(patientId)).thenReturn(Optional.of(patient));
        when(officeRepository.findById(officeId)).thenReturn(Optional.of(office));
        when(typeRepository.findById(typeId)).thenReturn(Optional.of(type));
        when(scheduleRepository.findByDoctorIdAndDayOfWeek(doctorId, requestedDay))
            .thenReturn(Collections.emptyList());

        Exception error = assertThrows(ConflictException.class, () -> {
            appointmentService.create(request);
        });

        assertThat(error.getMessage()).isEqualTo("The doctor does not work on the chosen day");
        verify(appointmentRepository, never()).save(any(Appointment.class));
    }

    @Test
    void shouldCancelAppointmentSuccessfully(){
        
        UUID appointmentId = UUID.randomUUID();
        AppointmentCancelRequest cancelRequest = new AppointmentCancelRequest("Patient could not come");
        
        Appointment appointment = Appointment.builder()
            .id(appointmentId)
            .status(AppointmentStatus.SCHEDULED)
            .doctor(doctor)
            .patient(patient)
            .build();

        when(appointmentRepository.findById(appointmentId)).thenReturn(Optional.of(appointment));
        when(appointmentRepository.save(any(Appointment.class))).thenReturn(appointment);

        AppointmentResponse response = appointmentService.cancel(appointmentId, cancelRequest);

        assertThat(response).isNotNull();
        assertThat(response.status()).isEqualTo(AppointmentStatus.CANCELLED);
        verify(appointmentRepository, times(1)).findById(appointmentId);
        verify(appointmentRepository, times(1)).save(any(Appointment.class));
    }

    @Test
    void shouldCompleteAppointmentSuccessfully(){

        UUID appointmentId = UUID.randomUUID();
        AppointmentCompleteRequest completeRequest = new AppointmentCompleteRequest("Everything normal");
        
        Instant pastStartAt = Instant.now().minus(1, ChronoUnit.HOURS);
        
        Appointment appointment = Appointment.builder()
            .id(appointmentId)
            .status(AppointmentStatus.CONFIRMED) 
            .startAt(pastStartAt)
            .doctor(doctor)
            .patient(patient)
            .build();

        when(appointmentRepository.findById(appointmentId)).thenReturn(Optional.of(appointment));
        when(appointmentRepository.save(any(Appointment.class))).thenReturn(appointment);

        AppointmentResponse response = appointmentService.complete(appointmentId, completeRequest);

        assertThat(response).isNotNull();
        assertThat(response.status()).isEqualTo(AppointmentStatus.COMPLETED);
        verify(appointmentRepository, times(1)).save(any(Appointment.class));
    }

    @Test
    void shouldMarkAppointmentAsNoShowSuccessfully(){

        UUID appointmentId = UUID.randomUUID();
        
        Instant pastStartAt = Instant.now().minus(2, ChronoUnit.HOURS);
        
        Appointment appointment = Appointment.builder()
            .id(appointmentId)
            .status(AppointmentStatus.CONFIRMED) 
            .startAt(pastStartAt)
            .doctor(doctor)
            .patient(patient)
            .build();

        when(appointmentRepository.findById(appointmentId)).thenReturn(Optional.of(appointment));
        when(appointmentRepository.save(any(Appointment.class))).thenReturn(appointment);

        AppointmentResponse response = appointmentService.markNoShow(appointmentId);

        assertThat(response).isNotNull();
        assertThat(response.status()).isEqualTo(AppointmentStatus.NO_SHOW);
        verify(appointmentRepository, times(1)).save(any(Appointment.class));
    }

}
