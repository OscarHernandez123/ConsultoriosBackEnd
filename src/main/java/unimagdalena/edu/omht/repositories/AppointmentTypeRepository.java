package unimagdalena.edu.omht.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import unimagdalena.edu.omht.entities.AppointmentType;

public interface AppointmentTypeRepository extends JpaRepository <AppointmentType, UUID>{

}
