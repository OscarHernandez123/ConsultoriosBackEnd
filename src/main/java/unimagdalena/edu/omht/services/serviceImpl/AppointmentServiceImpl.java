package unimagdalena.edu.omht.services.serviceImpl;

import java.time.DayOfWeek;
import java.time.Instant;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import unimagdalena.edu.omht.dtos.AppointmentDtos.AppointmentCancelRequest;
import unimagdalena.edu.omht.dtos.AppointmentDtos.AppointmentCompleteRequest;
import unimagdalena.edu.omht.dtos.AppointmentDtos.AppointmentResponse;
import unimagdalena.edu.omht.dtos.AppointmentDtos.CreateAppointmentRequest;
import unimagdalena.edu.omht.entities.Appointment;
import unimagdalena.edu.omht.entities.AppointmentType;
import unimagdalena.edu.omht.entities.Doctor;
import unimagdalena.edu.omht.entities.DoctorSchedule;
import unimagdalena.edu.omht.entities.Office;
import unimagdalena.edu.omht.entities.Patient;
import unimagdalena.edu.omht.enums.AppointmentStatus;
import unimagdalena.edu.omht.enums.OfficeStatus;
import unimagdalena.edu.omht.enums.PatientStatus;
import unimagdalena.edu.omht.exceptions.BusinessException;
import unimagdalena.edu.omht.exceptions.ConflictException;
import unimagdalena.edu.omht.exceptions.ResourceNotFoundException;
import unimagdalena.edu.omht.mappers.AppointmentMapper;
import unimagdalena.edu.omht.repositories.AppointmentRepository;
import unimagdalena.edu.omht.repositories.AppointmentTypeRepository;
import unimagdalena.edu.omht.repositories.DoctorRepository;
import unimagdalena.edu.omht.repositories.DoctorScheduleRepository;
import unimagdalena.edu.omht.repositories.OfficeRepository;
import unimagdalena.edu.omht.repositories.PatientRepository;
import unimagdalena.edu.omht.services.service.AppointmentService;

@Service
@RequiredArgsConstructor
@Transactional
public class AppointmentServiceImpl implements AppointmentService{
    
    private final AppointmentRepository appointmentRepository;

    private final DoctorRepository doctorRepository;
    
    private final PatientRepository patientRepository;

    private final OfficeRepository officeRepository;

    private final AppointmentTypeRepository typeRepository;

    private final DoctorScheduleRepository doctorScheduleRepository;

    @Override
    public AppointmentResponse create(CreateAppointmentRequest request) {

        Patient patient = patientRepository.findById(request.patientId())
            .orElseThrow(() -> new ResourceNotFoundException("Patient not found"));

        Doctor doctor = doctorRepository.findById(request.doctorId())
            .orElseThrow(() -> new ResourceNotFoundException("Doctor not found"));

        Office office = officeRepository.findById(request.officeId())
            .orElseThrow(() -> new ResourceNotFoundException("Office not found"));

        AppointmentType type = typeRepository.findById(request.appointmentTypeId())
            .orElseThrow(() -> new ResourceNotFoundException("Type not found"));

        if(patient.getStatus() == PatientStatus.INACTIVE || patient.getStatus() == PatientStatus.BLOCKED){
            throw new BusinessException("Patient is inactive");
        }

        if(office.getStatus() == OfficeStatus.INACTIVE || office.getStatus() == OfficeStatus.MAINTENANCE){
            throw new BusinessException("Office is unavailable");
        }

        Instant present = Instant.now();

        if(request.startAt().isBefore(present)){
            throw new BusinessException("Appointment can not be created in the past");
        }

        Instant endAt = request.startAt().plus(type.getDurationMinutes(), ChronoUnit.MINUTES);

        ZoneId zoneId = ZoneId.systemDefault();
        DayOfWeek requestedDay = request.startAt().atZone(zoneId).getDayOfWeek();
        LocalTime requestedStartTime = request.startAt().atZone(zoneId).toLocalTime();
        LocalTime requestedEndTime = endAt.atZone(zoneId).toLocalTime();

        List<DoctorSchedule> doctorSchedules = doctorScheduleRepository
            .findByDoctorIdAndDayOfWeek(doctor.getId(), requestedDay);

        if(doctorSchedules.isEmpty()){
            throw new BusinessException("The doctor does not work on the chosen day");
        }

        boolean isWithinWorkingHours = doctorScheduleRepository.isWithinWorkingHours(
            doctor.getId(), requestedDay, requestedStartTime, requestedEndTime
        );

        if(!isWithinWorkingHours){
            throw new BusinessException("The requested appointment time is outside the doctor's working hours.");
        }

        boolean hasDoctorOverlap = appointmentRepository
            .existsOverlappingDoctorAppointment(doctor.getId(), request.startAt(), endAt);

        if(hasDoctorOverlap){
            throw new ConflictException("Doctor is already booked for this time slot.");
        }

        boolean hasOfficeOverlap = appointmentRepository
            .existsOverlappingOfficeAppointment(office.getId(), request.startAt(), endAt);

        if(hasOfficeOverlap){
            throw new ConflictException("Office is already booked for this time slot.");
        }

        boolean hasPatientOverlap = appointmentRepository
            .existsOverlappingPatientAppointment(patient.getId(), request.startAt(), endAt);

        if(hasPatientOverlap) {
            throw new ConflictException("Patient already has another active appointment during this time slot.");
        }

        Appointment appointment = AppointmentMapper.toEntity(request, doctor, patient, office, type);
        appointment.setStatus(AppointmentStatus.SCHEDULED);

        Appointment saved = appointmentRepository.save(appointment);

        return AppointmentMapper.toResponse(saved);
    }

    @Override
    public AppointmentResponse confirm(UUID appointmentId) {

        Appointment appointment = appointmentRepository.findById(appointmentId)
            .orElseThrow(() -> new ResourceNotFoundException("Appointment not found"));
        
        if(appointment.getStatus() != AppointmentStatus.SCHEDULED){
            throw new BusinessException("Only scheduled appointments can be confirmed");
        }

        appointment.setStatus(AppointmentStatus.CONFIRMED);
        appointment.setUpdatedAt(Instant.now());
        Appointment saved = appointmentRepository.save(appointment);

        return AppointmentMapper.toResponse(saved);
    }

    @Override
    public AppointmentResponse cancel(UUID appointmentId, AppointmentCancelRequest cancelRequest) {

        Appointment appointment = appointmentRepository.findById(appointmentId)
            .orElseThrow(() -> new ResourceNotFoundException("Appointment not found"));
        
        if(appointment.getStatus() == AppointmentStatus.COMPLETED 
            || appointment.getStatus() == AppointmentStatus.NO_SHOW){
                throw new BusinessException("Only scheduled or confirmed appointments can be canceled");
        }

        appointment.setStatus(AppointmentStatus.CANCELLED);
        appointment.setCancellationReason(cancelRequest.reason());
        appointment.setUpdatedAt(Instant.now());
        Appointment saved = appointmentRepository.save(appointment);

        return AppointmentMapper.toResponse(saved);
    }

    @Override
    public AppointmentResponse complete(UUID appointmentId, AppointmentCompleteRequest administrativeNoteRequest) {

        Appointment appointment = appointmentRepository.findById(appointmentId)
            .orElseThrow(() -> new ResourceNotFoundException("Appointment not found"));

        if(appointment.getStatus() != AppointmentStatus.CONFIRMED){
            throw new BusinessException("Only confirmed appointments can be completed");
        }

        Instant present = Instant.now();

        if(appointment.getStartAt().isAfter(present)){
            throw new BusinessException("Cannot complete an appointment before its start time");
        }

        appointment.setStatus(AppointmentStatus.COMPLETED);
        appointment.setAdministrativeNote(administrativeNoteRequest.administrativeNotes());
        appointment.setUpdatedAt(Instant.now());
        Appointment saved = appointmentRepository.save(appointment);

        return AppointmentMapper.toResponse(saved);
    }

    @Override
    public AppointmentResponse markNoShow(UUID appointmentId) {

        Appointment appointment = appointmentRepository.findById(appointmentId)
            .orElseThrow(() -> new ResourceNotFoundException("Appointment not found"));

        if(appointment.getStatus() != AppointmentStatus.CONFIRMED){
            throw new BusinessException("Only confirmed appointments can be marked as no-show");
        }

        Instant present = Instant.now();

        if(appointment.getStartAt().isAfter(present)){
            throw new BusinessException("Cannot mark an appointment as no-show before its start time.");
        }

        appointment.setStatus(AppointmentStatus.NO_SHOW);
        appointment.setUpdatedAt(Instant.now());
        Appointment saved = appointmentRepository.save(appointment);

        return AppointmentMapper.toResponse(saved);
    }

    @Override
    public AppointmentResponse get(UUID appointmentId) {

        Appointment appointment = appointmentRepository.findById(appointmentId)
            .orElseThrow(() -> new ResourceNotFoundException("Appointment not found"));

        return AppointmentMapper.toResponse(appointment);
    }

    @Override
    public Page<AppointmentResponse> list(Pageable pageable) {
        return appointmentRepository.findAll(pageable).map(AppointmentMapper::toResponse);
    }

}
