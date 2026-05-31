package unimagdalena.edu.omht.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import unimagdalena.edu.omht.dtos.AvailabilityDtos.AvailabilitySlotResponse;
import unimagdalena.edu.omht.services.serviceInterface.AvailabilityService;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AvailabilityController.class)
public class AvailabilityControllerTest {

    @Autowired private MockMvc mockMvc;

    @MockitoBean private AvailabilityService availabilityService;

    @Test
    void shouldReturnAvailableSlotsSuccessfully() throws Exception {

        UUID doctorId = UUID.randomUUID();
        UUID appointmentTypeId = UUID.randomUUID();
        LocalDate day = LocalDate.now();

        AvailabilitySlotResponse slot1 = new AvailabilitySlotResponse(
                Instant.now(), 
                Instant.now().plusSeconds(1800));
                
        AvailabilitySlotResponse slot2 = new AvailabilitySlotResponse(
                Instant.now().plusSeconds(3600), 
                Instant.now().plusSeconds(5400));

        when(availabilityService.getAvailableSlots(eq(doctorId), eq(day), eq(appointmentTypeId)))
                .thenReturn(List.of(slot1, slot2));

        mockMvc.perform(get("/api/availability/doctors/{doctorId}", doctorId)
                .param("day", day.toString())
                .param("appointmentTypeId", appointmentTypeId.toString())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }
}
