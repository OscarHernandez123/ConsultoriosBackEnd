package unimagdalena.edu.omht.mappers;

import unimagdalena.edu.omht.dtos.DoctorScheduleDtos.CreateDoctorScheduleRequest;
import unimagdalena.edu.omht.dtos.DoctorScheduleDtos.DoctorScheduleResponse;
import unimagdalena.edu.omht.entities.Doctor;
import unimagdalena.edu.omht.entities.DoctorSchedule;

public class DoctorScheduleMapper {

    public static DoctorSchedule toEntity(CreateDoctorScheduleRequest request, Doctor doctor){
        return DoctorSchedule.builder()
                .dayOfWeek(request.dayOfWeek())
                .startAt(request.startAt())
                .endAt(request.endAt())
                .doctor(doctor)
                .build();
    }

    public static DoctorScheduleResponse toResponse(DoctorSchedule schedule){
        return new DoctorScheduleResponse(
            schedule.getId(),
            schedule.getDayOfWeek(),
            schedule.getStartAt(),
            schedule.getEndAt(),
            DoctorMapper.toResponse(schedule.getDoctor())
        );
    }
}
