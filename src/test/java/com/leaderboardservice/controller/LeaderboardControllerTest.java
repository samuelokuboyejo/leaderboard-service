package com.leaderboardservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.leaderboardservice.dto.SubmissionDto;
import com.leaderboardservice.service.LeaderboardService;
import com.leaderboardservice.utils.AppResponse;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.hamcrest.Matchers.is;



import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class LeaderboardControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private LeaderboardService leaderboardService;

    @Test
    public void testSubmitScore() throws Exception {
        SubmissionDto submissionDto = new SubmissionDto("vision", 120);
        AppResponse expectedResponse = AppResponse.builder()
                .responseMessage("Score submitted successfully")
                .build();

        Mockito.when(leaderboardService.submitScore(Mockito.any()))
                .thenReturn(expectedResponse);

        mockMvc.perform(post("/submissions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(submissionDto))
        ).andExpect(status().isOk())
                .andExpect(jsonPath("$.responseMessage", is("Score submitted successfully")));
    }
}
