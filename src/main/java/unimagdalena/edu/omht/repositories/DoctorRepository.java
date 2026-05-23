package unimagdalena.edu.omht.repositories;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import unimagdalena.edu.omht.entities.Doctor;
import java.util.List;


public interface DoctorRepository extends JpaRepository <Doctor, UUID>{
    List<Doctor> findBySpecialtyId(UUID specialtyId);
}
