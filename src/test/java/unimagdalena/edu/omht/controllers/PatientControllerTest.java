package unimagdalena.edu.omht.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import unimagdalena.edu.omht.dtos.PatientDtos.UpdatePatientRequest;
import unimagdalena.edu.omht.dtos.PatientDtos.PatientResponse;
import unimagdalena.edu.omht.enums.PatientStatus;
import unimagdalena.edu.omht.services.serviceInterface.PatientService;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PatientController.class)
public class PatientControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private PatientService patientService;

    @Test
    void shouldUpdatePatientAndReturn200() throws Exception {
        
        UUID patientId = UUID.randomUUID();

        UpdatePatientRequest request = new UpdatePatientRequest(
                "Oscar Turizo", 
                "3001234567", 
                "oscar@gmail.com", 
                PatientStatus.ACTIVE
        );

        PatientResponse response = new PatientResponse(
                patientId, 
                "Oscar Turizo", 
                "3001234567", 
                "oscar@gmail.com", 
                PatientStatus.ACTIVE,
                null,
                null
        );

        when(patientService.update(any(UpdatePatientRequest.class), eq(patientId)))
                .thenReturn(response);

        mockMvc.perform(put("/api/patients/{id}", patientId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.fullName").value("Oscar Turizo"))
                .andExpect(jsonPath("$.email").value("oscar@gmail.com"));
    }
}
