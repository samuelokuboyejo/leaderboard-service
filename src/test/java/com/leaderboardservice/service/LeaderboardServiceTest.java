package com.leaderboardservice.service;

import com.leaderboardservice.dto.SubmissionDto;
import com.leaderboardservice.utils.AppResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class LeaderboardServiceTest {
    @Autowired
    private LeaderboardService leaderboardService;

    @Test
    public void testSubmitScore_ValidScore(){
        SubmissionDto submissionDto = new SubmissionDto("vision", 120);
        AppResponse result = leaderboardService.submitScore(submissionDto);
        Assertions.assertEquals("Score submitted successfully", result.getResponseMessage());
    }


    
}
