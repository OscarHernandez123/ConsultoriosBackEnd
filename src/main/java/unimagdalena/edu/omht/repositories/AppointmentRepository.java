package unimagdalena.edu.omht.repositories;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import unimagdalena.edu.omht.entities.Appointment;

import java.time.Instant;
import java.util.List;
import unimagdalena.edu.omht.enums.AppointmentStatus;
import unimagdalena.edu.omht.projections.DoctorCompletedAppointmentProjection;
import unimagdalena.edu.omht.projections.OfficeOccupancyProjection;
import unimagdalena.edu.omht.projections.PatientNoShowAppointmentProjection;
import unimagdalena.edu.omht.projections.SpecialtyCancelledOrNoShowAppointmentProjection;


public interface AppointmentRepository extends JpaRepository <Appointment, UUID>{
    List<Appointment> findByStatusAndPatientId(AppointmentStatus status, UUID patientId);
    List<Appointment> findByStartAtBetween(Instant startDate, Instant endDate);

    @Query("""
            SELECT COUNT(a) > 0
            FROM Appointment a
            WHERE a.doctor.id = :doctorId
            AND a.status IN ('SCHEDULED', 'CONFIRMED')
            AND a.startAt < :newEnd
            AND a.endAt > :newStart
            """)
    boolean existsOverlappingDoctorAppointment(
        @Param ("doctorId") UUID doctorId,
        @Param ("newStart") Instant newStart,
        @Param ("newEnd") Instant newEnd
    );

    @Query("""
            SELECT COUNT > 0
            FROM Appointment a
            WHERE a.office.id = :officeId
            AND a.status IN ('SCHEDULED', 'CONFIRMED')
            AND a.startAt < :newEnd
            AND a.endAt > :newStart
            """)
    boolean exitsOverLappingOfficeAppointment(
        @Param ("officeId") UUID officeId,
        @Param ("newStart") Instant newStart,
        @Param ("newEnd") Instant newEnd
    );

    @Query("""
            SELECT a
            FROM Appointment a
            WHERE a.doctor.id = :doctorId
            AND a.status IN ('SCHEDULED', 'CONFIRMED')
            AND a.startAt >= :startOfDay
            AND a.startAt < :endOfDay
            ORDER BY a.startAt ASC
            """)
    List<Appointment> findActiveAppointmentsByDoctorAndDate(
        @Param ("doctorId") UUID doctorId,
        @Param ("startOfDay") Instant startOfDay,
        @Param ("endOfDay") Instant endOfDay
    );

    @Query("""
            SELECT a.office.id AS officeId,
                   a.office.location AS location,
                   SUM(a.appointmentType.durationMinutes) AS occupiedMinutes
            FROM Appointment a
            WHERE a.status IN ('SCHEDULED', 'CONFIRMED', 'COMPLETED')
            AND a.startAt BETWEEN :startDate AND :endDate
            GROUP BY a.office.id, a.office.location
            """)
    List<OfficeOccupancyProjection> getOfficeOccupation(
        @Param ("startDate") Instant startDate,
        @Param ("endDate") Instant endDate
    );

    @Query("""
            SELECT a.doctor.specialty.id AS specialtyId, 
                   a.doctor.specialty.title AS title,
                   COUNT(a) AS cancelledOrNoShowAppointments
            FROM Appointment a
            WHERE a.status IN ('CANCELLED', 'NO_SHOW')
            GROUP BY a.specialty.id, a.specialty.title
            """)
    List<SpecialtyCancelledOrNoShowAppointmentProjection> countCancelledOrNoShowAppointmentBySpecialty();

    @Query("""
            SELECT a.doctor.id AS doctorId,
                   a.doctor.fullName AS fullName,
                   COUNT(a) as completedAppointments
            FROM Appointment a
            WHERE a.status = 'COMPLETED'
            GROUP BY a.doctor.id, a.doctor.fullName
            ORDER BY COUNT(a) DESC
            """)
    List<DoctorCompletedAppointmentProjection> getDoctorCompletedAppointment();
    
    @Query("""
            SELECT a.patient.id AS patientId,
                   a.patient.fullName AS fullName,
                   COUNT(a) AS noShowAppointments,
                   MAX(a.startAt) AS lastNoShowDate
            FROM Appointment a
            WHERE a.status = 'NO_SHOW'
            AND a.startAt BETWEEN :startDate AND :endDate
            GROUP BY a.patient.id, a.patient.fullName
            ORDER BY COUNT(a) DESC
            """)
    List<PatientNoShowAppointmentProjection> getPatientbyAppointmentsNoShow(
        @Param ("startDate") Instant startDate,
        @Param ("endDate") Instant endDate
    );
}
