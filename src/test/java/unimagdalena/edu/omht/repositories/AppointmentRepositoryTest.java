package unimagdalena.edu.omht.repositories;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import static org.assertj.core.api.Assertions.assertThat;

import unimagdalena.edu.omht.entities.Appointment;
import unimagdalena.edu.omht.entities.AppointmentType;
import unimagdalena.edu.omht.entities.Doctor;
import unimagdalena.edu.omht.entities.Office;
import unimagdalena.edu.omht.entities.Patient;
import unimagdalena.edu.omht.enums.AppointmentStatus;
import unimagdalena.edu.omht.enums.OfficeStatus;
import unimagdalena.edu.omht.enums.PatientStatus;
import unimagdalena.edu.omht.projections.OfficeOccupancyProjection;
import unimagdalena.edu.omht.projections.PatientNoShowAppointmentProjection;

public class AppointmentRepositoryTest extends AbstractRepositoryIT{

    @Autowired AppointmentRepository appointmentRepository;

    @Autowired DoctorRepository doctorRepository;

    @Autowired PatientRepository patientRepository;

    @Autowired OfficeRepository officeRepository;

    @Autowired AppointmentTypeRepository typeRepository;

    @Test
    void shouldExistsOverlappingDoctorAppointment(){

        Doctor doctor = Doctor.builder()
            .fullName("Oscar Turizo")
            .email("oscar@gmail.com")
            .build();

        doctorRepository.save(doctor);

        Instant start1 = Instant.now();
        Instant end1 = start1.plusSeconds(1800);
        Instant start2 = Instant.now().plusSeconds(900);
        Instant end2 = start2.plusSeconds(1800);

        Appointment appointment1 = Appointment.builder()
            .doctor(doctor)
            .startAt(start1)
            .endAt(end1)
            .status(AppointmentStatus.SCHEDULED)
            .build();

        appointmentRepository.save(appointment1);

        boolean existOverlapping = appointmentRepository.existsOverlappingDoctorAppointment(doctor.getId(), start2, end2);

        assertThat(existOverlapping).isEqualTo(true);
    }

    @Test
    void shouldFindByStartAtBetween(){

        Instant start1 = Instant.now();
        Instant end1 = start1.plusSeconds(1800);

        Appointment appointment = Appointment.builder()
            .startAt(start1)
            .endAt(end1)
            .status(AppointmentStatus.SCHEDULED)
            .build();

        appointmentRepository.save(appointment);

        Instant start2 = Instant.now().minusSeconds(1800);
        Instant end2 = Instant.now().plusSeconds(3600);

        List<Appointment> appointments = appointmentRepository.findByStartAtBetween(start2, end2);

        assertThat(appointments).isNotNull();
        assertThat(appointments).hasSize(1);
        Appointment foundAppointment = appointments.get(0);
        assertThat(foundAppointment.getStartAt()).isEqualTo(start1);
        assertThat(foundAppointment.getStatus()).isEqualTo(AppointmentStatus.SCHEDULED);
    }

    @Test
    void shouldGetOfficeOccupation(){
        
        Office office = Office.builder()
            .location("101")
            .status(OfficeStatus.ACTIVE)
            .build();

        officeRepository.save(office);

        AppointmentType type = AppointmentType.builder()
            .title("General")
            .durationMinutes(30)
            .build();

        typeRepository.save(type);

        LocalDate today = LocalDate.now();

        Instant start1 = today.atTime(10, 0)
                .atZone(ZoneId.systemDefault()) 
                .toInstant();  
                
        Instant end1 = start1.plus(type.getDurationMinutes(), ChronoUnit.MINUTES);

        Appointment appointment1 = Appointment.builder()
            .startAt(start1)
            .endAt(end1)
            .status(AppointmentStatus.SCHEDULED)
            .office(office)
            .appointmentType(type)
            .build();

        Instant start2 = today.atTime(11, 0)
                .atZone(ZoneId.systemDefault()) 
                .toInstant();  
        Instant end2 = start1.plus(type.getDurationMinutes(), ChronoUnit.MINUTES);

        Appointment appointment2 = Appointment.builder()
            .startAt(start2)
            .endAt(end2)
            .status(AppointmentStatus.SCHEDULED)
            .office(office)
            .appointmentType(type)
            .build();

        appointmentRepository.save(appointment1);
        appointmentRepository.save(appointment2);

        Instant startDate = today.atTime(8, 0)
                .atZone(ZoneId.systemDefault()) 
                .toInstant();                   

        Instant endDate = today.atTime(18, 0)
                .atZone(ZoneId.systemDefault())
                .toInstant();

        List<OfficeOccupancyProjection> result = appointmentRepository.getOfficeOccupation(startDate, endDate);

        assertThat(result).isNotEmpty();
        assertThat(result).hasSize(1);        
        OfficeOccupancyProjection projection = result.get(0);      
        assertThat(projection.getLocation()).isEqualTo("101");
        assertThat(projection.getOccupiedMinutes()).isEqualTo(60L);
    }

    @Test
    void shouldGetPatientbyAppointmentsNoShow(){

        Patient patient = Patient.builder()
            .fullName("Oscar Turizo")
            .email("oscar@gmail.com")
            .phone("3014975842")
            .identification("1100623604")
            .status(PatientStatus.ACTIVE)
            .build();

        patientRepository.save(patient);

        AppointmentType type = AppointmentType.builder()
            .title("General")
            .durationMinutes(30)
            .build();

        typeRepository.save(type);

        LocalDate today = LocalDate.now();

        Instant start1 = today.atTime(10, 0)
                .atZone(ZoneId.systemDefault()) 
                .toInstant();  

        Appointment appointment1 = Appointment.builder()
            .startAt(start1)
            .status(AppointmentStatus.NO_SHOW)
            .patient(patient)
            .appointmentType(type)
            .build();
            
        appointmentRepository.save(appointment1);

        Instant startDate = today.atTime(8, 0)
                .atZone(ZoneId.systemDefault()) 
                .toInstant();                   

        Instant endDate = today.atTime(18, 0)
                .atZone(ZoneId.systemDefault())
                .toInstant();

        List<PatientNoShowAppointmentProjection> result = appointmentRepository.getPatientbyAppointmentsNoShow(startDate, endDate);
        
        assertThat(result).isNotNull();
        assertThat(result).hasSize(1);
        PatientNoShowAppointmentProjection projection = result.get(0);
        assertThat(projection.getFullName()).isEqualTo("Oscar Turizo");
        assertThat(projection.getNoShowAppointments()).isEqualTo(1L);
        assertThat(projection.getLastNoShowDate()).isEqualTo(start1);
    }

}
