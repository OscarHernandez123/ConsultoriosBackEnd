package unimagdalena.edu.omht.repositories;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import unimagdalena.edu.omht.entities.Patient;
import unimagdalena.edu.omht.enums.PatientStatus;

public class PatientRepositoryTest extends AbstractRepositoryIT{

    @Autowired PatientRepository patientRepository;

    @Test
    void shouldFindById(){

        Patient patient = Patient.builder()
            .fullName("Oscar Turizo")
            .email("oscar@gmail.com")
            .phone("3014975842")
            .identification("1100623604")
            .status(PatientStatus.ACTIVE)
            .build();

        patientRepository.save(patient);

        Optional<Patient> foundPatient = patientRepository.findById(patient.getId());

        assertThat(foundPatient.get().getId()).isNotNull();
        assertThat(foundPatient.get().getFullName()).isEqualTo("Oscar Turizo");
        assertThat(foundPatient.get().getEmail()).isEqualTo("oscar@gmail.com");
        assertThat(foundPatient.get().getStatus()).isEqualTo(PatientStatus.ACTIVE);
    }
}
