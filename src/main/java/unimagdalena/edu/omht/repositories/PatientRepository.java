package unimagdalena.edu.omht.repositories;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import unimagdalena.edu.omht.entities.Patient;

public interface PatientRepository extends JpaRepository <Patient, UUID>{
}
