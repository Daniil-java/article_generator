package com.education.articlegenerator.controllers;

import com.education.articlegenerator.entities.GenerationRequest;
import com.education.articlegenerator.repositories.GenerationRequestRepository;
import com.education.articlegenerator.services.GenerationRequestService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
//@WebMvcTest(GenerationRequestController.class)
@AutoConfigureMockMvc
public class GenerationRequestControllerTest {
    @Autowired
    private MockMvc mvc;
//    @MockBean
//    private GenerationRequestService generationRequestService;
    @MockBean
    private GenerationRequestRepository generationRequestRepository;

    @Test
    public void getAllRequestsTest() throws Exception {
//        return generationRequestService.getAllRequests();
        List<GenerationRequest> generationRequests = new ArrayList<>();
        generationRequests.add(new GenerationRequest()
                .setId(1L));
        given(generationRequestRepository.findAll())
                .willReturn(generationRequests);

        this.mvc.perform(MockMvcRequestBuilders
                .get("/api/v1/generationrequest/all")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(1)))
        ;

    }

    @Test
    public void createRequestTest() {

    }

}
