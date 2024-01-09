package com.education.articlegenerator.services;

import com.education.articlegenerator.dtos.ErrorResponseException;
import com.education.articlegenerator.entities.GenerationRequest;
import com.education.articlegenerator.entities.Status;
import com.education.articlegenerator.repositories.GenerationRequestRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(classes = GenerationRequestService.class)
public class GenerationRequestServiceTest {
    @Autowired
    private GenerationRequestService generationRequestService;
    @MockBean
    private GenerationRequestRepository generationRequestRepository;
    private final Long TRUE_ID = 1L;
    private final Long FALSE_ID = 2L;
    private GenerationRequest generationRequest = new GenerationRequest()
            .setId(TRUE_ID)
            .setStatus(Status.CREATED);

    @BeforeEach
    public void setUp() {
        Mockito.doReturn(generationRequest).when(generationRequestRepository).save(new GenerationRequest());

        Mockito.doReturn(Optional.of(generationRequest)).when(generationRequestRepository).findById(TRUE_ID);
        Mockito.doReturn(Optional.empty()).when(generationRequestRepository).findById(FALSE_ID);

        ArrayList<GenerationRequest> generationRequestArrayList = new ArrayList<>();
        generationRequestArrayList.add(generationRequest);
        Mockito.doReturn(Optional.of(generationRequestArrayList))
                .when(generationRequestRepository).findGenerationRequestByStatus(Status.CREATED);
        Mockito.doReturn(Optional.of(new ArrayList<GenerationRequest>()))
                .when(generationRequestRepository).findGenerationRequestByStatus(Status.GENERATED);
    }

    @Test
    public void createRequestTest() {
        Assertions.assertEquals(generationRequest, generationRequestService.createRequest(new GenerationRequest()));
    }

    @Test
    public void getRequestByIdTest() {
        Assertions.assertEquals(generationRequest, generationRequestService.getRequestById(TRUE_ID));
        ErrorResponseException exception = assertThrows(ErrorResponseException.class, () -> {
            generationRequestService.getRequestById(FALSE_ID);
        });

        String expectedMessage = "Request does not exist";
        String errorStatus = exception.getErrorStatus().getMessage();
        assertTrue(errorStatus.contains(expectedMessage));
    }

    @Test
    public void getRequestsByStatusTest() {
        ArrayList<GenerationRequest> generationRequestArrayList = new ArrayList<>();
        generationRequestArrayList.add(generationRequest);

        Assertions.assertEquals(generationRequestArrayList, generationRequestService.getRequestsByStatus(Status.CREATED));
        assertTrue(generationRequestService.getRequestsByStatus(Status.GENERATED).isEmpty());
    }

}
