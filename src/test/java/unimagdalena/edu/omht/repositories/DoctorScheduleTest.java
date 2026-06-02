package unimagdalena.edu.omht.repositories;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.DayOfWeek;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import unimagdalena.edu.omht.entities.Doctor;
import unimagdalena.edu.omht.entities.DoctorSchedule;
import unimagdalena.edu.omht.enums.DoctorStatus;

public class DoctorScheduleTest extends AbstractRepositoryIT{

    @Autowired DoctorScheduleRepository doctorScheduleRepository;
    
    @Autowired DoctorRepository doctorRepository;

    @Test
    void shouldFindByDoctorIdAndDayOfWeek(){

        Doctor doctor = Doctor.builder()
            .fullName("Oscar Turizo")
            .email("oscar@gmail.com")
            .status(DoctorStatus.ACTIVE)
            .build();

        doctorRepository.save(doctor);

        DoctorSchedule schedule1 = DoctorSchedule.builder()
            .dayOfWeek(DayOfWeek.FRIDAY)
            .doctor(doctor)
            .build();

        DoctorSchedule schedule2 = DoctorSchedule.builder()
            .dayOfWeek(DayOfWeek.MONDAY)
            .doctor(doctor)
            .build();

        doctorScheduleRepository.save(schedule1);
        doctorScheduleRepository.save(schedule2);

        List<DoctorSchedule> schedules = doctorScheduleRepository.findByDoctorIdAndDayOfWeek(doctor.getId(), DayOfWeek.FRIDAY);

        assertThat(schedules).isNotEmpty();
        assertThat(schedules).hasSize(1);
        DoctorSchedule foundSchedule = schedules.get(0);
        assertThat(foundSchedule.getDoctor().getFullName()).isEqualTo("Oscar Turizo");
        assertThat(foundSchedule.getDayOfWeek()).isEqualTo(DayOfWeek.FRIDAY);
    }
}
