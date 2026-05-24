package unimagdalena.edu.omht.mappers;

import unimagdalena.edu.omht.dtos.DoctorProfileDtos.CreateDoctorProfileRequest;
import unimagdalena.edu.omht.dtos.DoctorProfileDtos.DoctorProfileResponse;
import unimagdalena.edu.omht.dtos.DoctorProfileDtos.PatchDoctorProfileRequest;
import unimagdalena.edu.omht.dtos.DoctorProfileDtos.UpdateDoctorProfileRequest;
import unimagdalena.edu.omht.entities.DoctorProfile;

public class DoctorProfileMapper {
    
    public static DoctorProfile toEntity(CreateDoctorProfileRequest request){
        return DoctorProfile.builder()
                .phone(request.phone())
                .bio(request.bio())
                .build();
    }

    public static void patch(DoctorProfile profile, PatchDoctorProfileRequest request){

        if(profile.getPhone() != null){
            profile.setPhone(request.phone());
        }

        if(profile.getBio() != null){
            profile.setBio(request.bio());
        }

    }

    public static void update(DoctorProfile profile, UpdateDoctorProfileRequest request){

            profile.setPhone(request.phone());
            profile.setBio(request.bio());
    }

    public static DoctorProfileResponse toResponse(DoctorProfile profile){
        return new DoctorProfileResponse(
            profile.getId(),
            profile.getPhone(),
            profile.getBio()
        );
    }
}
