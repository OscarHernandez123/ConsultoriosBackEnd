package unimagdalena.edu.omht.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import unimagdalena.edu.omht.entities.Office;

public interface OfficeRepository extends JpaRepository <Office, UUID>{

}
