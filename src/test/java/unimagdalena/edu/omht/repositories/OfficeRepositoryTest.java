package unimagdalena.edu.omht.repositories;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import unimagdalena.edu.omht.entities.Office;
import unimagdalena.edu.omht.enums.OfficeStatus;

public class OfficeRepositoryTest extends AbstractRepositoryIT{

    @Autowired OfficeRepository officeRepository;

    @Test
    void shouldFindById(){

        Office office = Office.builder()
            .name("office")
            .location("101")
            .status(OfficeStatus.ACTIVE)
            .build();

        officeRepository.save(office);

        Optional<Office> foundOffice = officeRepository.findById(office.getId());

        assertThat(foundOffice.get().getId()).isNotNull();
        assertThat(foundOffice.get().getLocation()).isEqualTo("101");
        assertThat(foundOffice.get().getStatus()).isEqualTo(OfficeStatus.ACTIVE);
        
    }
}
