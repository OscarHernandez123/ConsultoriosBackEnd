package unimagdalena.edu.omht.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;

import unimagdalena.edu.omht.dtos.DoctorDtos.CreateDoctorRequest;
import unimagdalena.edu.omht.dtos.DoctorDtos.DoctorResponse;
import unimagdalena.edu.omht.dtos.DoctorProfileDtos.CreateDoctorProfileRequest;
import unimagdalena.edu.omht.dtos.DoctorProfileDtos.DoctorProfileResponse;
import unimagdalena.edu.omht.services.serviceInterface.DoctorService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.UUID;

@WebMvcTest(DoctorController.class)
public class DoctorControllerTest {

    @Autowired private MockMvc mockMvc;

    @Autowired private ObjectMapper objectMapper;

    @MockitoBean private DoctorService doctorService;

    @Test
    void shouldCreateDoctorAndReturn201() throws Exception{

        CreateDoctorProfileRequest profileRequest = new CreateDoctorProfileRequest("3135569055", "Doctor");

        CreateDoctorRequest request = new CreateDoctorRequest(
            "Oscar Turizo", "oscar@gmail.com", UUID.randomUUID(), profileRequest);
        
        DoctorProfileResponse profileResponse = new DoctorProfileResponse(UUID.randomUUID(), "3135569055", "Doctor");

        DoctorResponse response = new DoctorResponse(
            UUID.randomUUID(), "Oscar Turizo", "oscar@gmail.com", profileResponse, null, null, null);

        when(doctorService.create(any(CreateDoctorRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/doctors")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.fullName").value("Oscar Turizo"))
                .andExpect(jsonPath("$.email").value("oscar@gmail.com"));

    }

    @Test
    void shouldReturn400WhenRequestIsInvalid() throws Exception{

        CreateDoctorProfileRequest profileRequest = new CreateDoctorProfileRequest("3135569055", "Doctor");

        CreateDoctorRequest request = new CreateDoctorRequest(
            "Oscar", "oscar", UUID.randomUUID(), profileRequest);
        
        DoctorProfileResponse profileResponse = new DoctorProfileResponse(UUID.randomUUID(), "3135569055", "Doctor");

        DoctorResponse response = new DoctorResponse(
            UUID.randomUUID(), "Oscar Turizo", "oscar@gmail.com", profileResponse, null, null, null);

        when(doctorService.create(any(CreateDoctorRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/doctors")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }
}
