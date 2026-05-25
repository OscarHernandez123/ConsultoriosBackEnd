package unimagdalena.edu.omht.mappers;

import unimagdalena.edu.omht.dtos.DoctorDtos.CreateDoctorRequest;
import unimagdalena.edu.omht.dtos.DoctorDtos.DoctorResponse;
import unimagdalena.edu.omht.dtos.DoctorDtos.PatchDoctorRequest;
import unimagdalena.edu.omht.dtos.DoctorDtos.UpdateDoctorRequest;
import unimagdalena.edu.omht.entities.Doctor;
import unimagdalena.edu.omht.entities.DoctorProfile;
import unimagdalena.edu.omht.entities.Specialty;

public class DoctorMapper {

    public static Doctor toEntity(CreateDoctorRequest request, Specialty specialty){
        DoctorProfile profile = null;

        if(request.profile() != null){
            profile = DoctorProfileMapper.toEntity(request.profile());
        }

        return Doctor.builder()
                .fullName(request.fullName())
                .email(request.email())
                .specialty(specialty)
                .doctorProfile(profile)
                .build();
    }

    public static void patch(Doctor doctor, PatchDoctorRequest request, Specialty specialty){

        if(request.fullName() != null){
            doctor.setFullName(request.fullName());
        }

        if(request.email() != null){
            doctor.setEmail(request.email());
        }

        if(specialty != null){
            doctor.setSpecialty(specialty);
        }

        if(request.profile() != null){
            if(doctor.getDoctorProfile() == null){
                DoctorProfile newProfile = new DoctorProfile();
                DoctorProfileMapper.patch(newProfile, request.profile());
                doctor.setDoctorProfile(newProfile);
            } else {
                DoctorProfileMapper.patch(doctor.getDoctorProfile(), request.profile());
            }
        }
    }

    public static void update(Doctor doctor, UpdateDoctorRequest request, Specialty specialty){
        doctor.setFullName(request.fullName());
        doctor.setEmail(request.email());
        doctor.setSpecialty(specialty);
        if (doctor.getDoctorProfile() == null) {
            doctor.setDoctorProfile(new DoctorProfile());
        }
        DoctorProfileMapper.update(doctor.getDoctorProfile(), request.profile());
    }

    public static DoctorResponse toResponse(Doctor doctor){
        return new DoctorResponse(
            doctor.getId(), 
            doctor.getFullName(), 
            doctor.getEmail(), 
            DoctorProfileMapper.toResponse(doctor.getDoctorProfile()), 
            doctor.getCreatedAt(), 
            doctor.getUpdatedAt(), 
            SpecialtyMapper.toResponse(doctor.getSpecialty())
        );
    }
}