package unimagdalena.edu.omht.mappers;

import unimagdalena.edu.omht.dtos.PatientDtos.CreatePatientRequest;
import unimagdalena.edu.omht.dtos.PatientDtos.PatchPatientRequest;
import unimagdalena.edu.omht.dtos.PatientDtos.PatientResponse;
import unimagdalena.edu.omht.dtos.PatientDtos.UpdatePatientRequest;
import unimagdalena.edu.omht.entities.Patient;
import unimagdalena.edu.omht.enums.PatientStatus;

public class PatientMapper{

    public static Patient toEntity(CreatePatientRequest request){
        return Patient.builder()
                .fullName(request.fullName())
                .phone(request.phone())
                .email(request.email())
                .status(PatientStatus.ACTIVE)
                .build();                                
    }

    public static void patch(Patient patient, PatchPatientRequest request){

        if(request.fullName() != null){
            patient.setFullName(request.fullName());
        }

        if(request.phone() != null){
            patient.setPhone(request.phone());
        }

        if(request.email() != null){
            patient.setEmail(request.email());
        }

        if(request.status() != null){
            patient.setStatus(request.status());
        }
    }

    public static void update(Patient patient, UpdatePatientRequest request){
        patient.setFullName(request.fullName());
        patient.setPhone(request.phone());
        patient.setEmail(request.email());
        patient.setStatus(request.status());
    }

    public static PatientResponse toResponse(Patient patient){
        return new PatientResponse(
            patient.getId(),
            patient.getFullName(), 
            patient.getPhone(),
            patient.getEmail(),
            patient.getStatus(),
            patient.getCreatedAt(),
            patient.getUpdatedAt()
        );
    }
}