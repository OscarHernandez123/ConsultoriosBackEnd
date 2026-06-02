package unimagdalena.edu.omht.repositories;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import static org.assertj.core.api.Assertions.assertThat;
import unimagdalena.edu.omht.entities.Doctor;
import unimagdalena.edu.omht.entities.Specialty;
import unimagdalena.edu.omht.enums.DoctorStatus;

public class DoctorRepositoryTest extends AbstractRepositoryIT{

    @Autowired DoctorRepository doctorRepository;

    @Autowired SpecialtyRepository specialtyRepository;

    @Test
    void shouldFindDoctorBySpecialty(){

        Specialty specialty1 = Specialty.builder()
            .title("Cardiology")
            .build();

        Specialty specialty2 = Specialty.builder()
            .title("Dermatology")
            .build();

        specialtyRepository.save(specialty1);
        specialtyRepository.save(specialty2);

        Doctor doctor1 = Doctor.builder()
            .fullName("Oscar Turizo")
            .email("oscar@gmail.com")
            .status(DoctorStatus.ACTIVE)
            .specialty(specialty1)
            .build();

        Doctor doctor2 = Doctor.builder()
            .fullName("Manuel Hernandez")
            .email("manuel@gmail.com")
            .status(DoctorStatus.ACTIVE)
            .specialty(specialty2)
            .build();
        
        doctorRepository.save(doctor1);
        doctorRepository.save(doctor2);

        List<Doctor> doctors = doctorRepository.findBySpecialtyId(specialty1.getId());

        assertThat(doctors).isNotEmpty();
        assertThat(doctors).hasSize(1);
        Doctor foundDoctor = doctors.get(0);
        assertThat(foundDoctor.getFullName()).isEqualTo("Oscar Turizo");
        assertThat(foundDoctor.getSpecialty().getTitle()).isEqualTo("Cardiology");        
    }
}
