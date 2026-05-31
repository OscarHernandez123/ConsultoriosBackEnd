package unimagdalena.edu.omht.repositories;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import unimagdalena.edu.omht.entities.DoctorSchedule;
import java.util.List;
import java.time.DayOfWeek;
import java.time.LocalTime;


public interface DoctorScheduleRepository extends JpaRepository <DoctorSchedule, UUID>{
        List<DoctorSchedule> findByDoctorIdAndDayOfWeek(UUID doctorID, DayOfWeek dayOfWeek);

        @Query("""
                SELECT COUNT(s) > 0 
                FROM DoctorSchedule s
                WHERE s.doctor.id = :doctorId
                AND s.dayOfWeek = :dayOfWeek
                AND :startAt < s.endAt
                AND :endAt > s.startAt
                """)
        boolean existsOverlappingSchedule(
            @Param("doctorId") UUID doctorId,
            @Param("dayOfWeek") DayOfWeek dayOfWeek,
            @Param("startAt") LocalTime startAt,
            @Param("endAt") LocalTime endAt
        );

        @Query("""
                SELECT COUNT(s) > 0 
                FROM DoctorSchedule s
                WHERE s.doctor.id = :doctorId
                AND s.dayOfWeek = :dayOfWeek
                AND s.startAt <= :appointmentStart
                AND s.endAt >= :appointmentEnd
            """)
        boolean isWithinWorkingHours(
            @Param("doctorId") UUID doctorId,
            @Param("dayOfWeek") DayOfWeek dayOfWeek,
            @Param("appointmentStart") LocalTime appointmentStart,
            @Param("appointmentEnd") LocalTime appointmentEnd
        );
}
