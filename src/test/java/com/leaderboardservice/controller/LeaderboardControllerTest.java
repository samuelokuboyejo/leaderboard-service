package com.leaderboardservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.leaderboardservice.dto.SubmissionDto;
import com.leaderboardservice.model.Leaderboard;
import com.leaderboardservice.service.LeaderboardService;
import com.leaderboardservice.utils.AppResponse;
import com.leaderboardservice.utils.LeaderboardResponse;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import java.util.List;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@AutoConfigureMockMvc
@WebMvcTest(LeaderboardController.class)
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

    @Test
    void testGetRank() throws Exception {
        AppResponse mockResponse = AppResponse.builder()
                .responseMessage("Rank displayed")
                .data(LeaderboardResponse.builder()
                        .username("hulvey")
                        .score(300)
                        .rank(5)
                        .build())
                .build();

        Mockito.when(leaderboardService.getRank("hulvey")).thenReturn(mockResponse);

        mockMvc.perform(get("/hulvey/rank"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.responseMessage").value("Rank displayed"))
                .andExpect(jsonPath("$.data.username").value("hulvey"))
                .andExpect(jsonPath("$.data.rank").value(5));
    }

    @Test
    void testGetTopPlayers() throws Exception {
        List<LeaderboardResponse> mockList = List.of(
                new LeaderboardResponse("marc", 400, 1),
                new LeaderboardResponse("maxwell", 350, 2)
        );

        Mockito.when(leaderboardService.getTopPlayers(anyInt())).thenReturn(mockList);

        mockMvc.perform(get("/scores?size=2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].username").value("marc"))
                .andExpect(jsonPath("$[1].rank").value(2));
    }
}
