package unimagdalena.edu.omht.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import unimagdalena.edu.omht.dtos.DoctorDtos.CreateDoctorRequest;
import unimagdalena.edu.omht.dtos.DoctorDtos.DoctorResponse;
import unimagdalena.edu.omht.dtos.DoctorProfileDtos.CreateDoctorProfileRequest;
import unimagdalena.edu.omht.entities.Doctor;
import unimagdalena.edu.omht.entities.DoctorProfile;
import unimagdalena.edu.omht.entities.Specialty;
import unimagdalena.edu.omht.exceptions.ResourceNotFoundException;
import unimagdalena.edu.omht.repositories.DoctorRepository;
import unimagdalena.edu.omht.repositories.SpecialtyRepository;
import unimagdalena.edu.omht.services.serviceImpl.DoctorServiceImpl;

import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public class DoctorServiceImplTest {

    @Mock private DoctorRepository doctorRepository;
    
    @Mock private SpecialtyRepository specialtyRepository;

    @InjectMocks private DoctorServiceImpl doctorService;

    @Test
    void shouldCreateDoctor(){

        UUID specialtyId = UUID.randomUUID();

        CreateDoctorProfileRequest profileRequest = new CreateDoctorProfileRequest(
            "3135569055", "Expert cardiologist");

        CreateDoctorRequest request = new CreateDoctorRequest(
            "Oscar", "oscar@gmail.com", specialtyId, profileRequest);
        
        DoctorProfile profile = DoctorProfile.builder().phone("3015975842").bio("doctor").build();

        Specialty specialty = Specialty.builder()
            .id(specialtyId)
            .title("Cardiology")
            .build();

        Doctor savedDoctor = Doctor.builder()
            .id(UUID.randomUUID())
            .fullName("Oscar Turizo")
            .email("oscar@gmail.com")
            .specialty(specialty)
            .doctorProfile(profile)
            .build();

        when(specialtyRepository.findById(specialtyId)).thenReturn(Optional.of(specialty));
        when(doctorRepository.save(any(Doctor.class))).thenReturn(savedDoctor);

        DoctorResponse response = doctorService.create(request);

        assertThat(response).isNotNull();
        assertThat(response.fullName()).isEqualTo("Oscar Turizo");
        verify(specialtyRepository, times(1)).findById(specialtyId);
        verify(doctorRepository, times(1)).save(any(Doctor.class));

    }

    @Test
    void shouldThrowExceptionWhenSpecialtyNotFound(){

        UUID specialtyId = UUID.randomUUID();

        CreateDoctorProfileRequest profileRequest = new CreateDoctorProfileRequest(
            "3135569055", "Expert cardiologist");

        CreateDoctorRequest request = new CreateDoctorRequest(
            "Oscar", "oscar@gmail.com", specialtyId, profileRequest);

        when(specialtyRepository.findById(specialtyId)).thenReturn(Optional.empty());

        Exception error = assertThrows(ResourceNotFoundException.class , () -> {
            doctorService.create(request);
        });

        assertThat(error.getMessage()).isEqualTo("Specialty not found");
        verify(doctorRepository, never()).save(any(Doctor.class));
    }
}
