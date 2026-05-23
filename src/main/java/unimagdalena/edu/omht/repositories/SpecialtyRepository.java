package unimagdalena.edu.omht.repositories;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import unimagdalena.edu.omht.entities.Specialty;

public interface SpecialtyRepository extends JpaRepository <Specialty, UUID>{

}
