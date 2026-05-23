package unimagdalena.edu.omht.repositories;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import unimagdalena.edu.omht.entities.DoctorSchedule;
import java.util.List;
import java.time.DayOfWeek;


public interface DoctorScheduleRepository extends JpaRepository <DoctorSchedule, UUID>{
    List<DoctorSchedule> findByDoctorIdAndDayOfWeek(UUID doctorID, DayOfWeek dayOfWeek);
}
