package com.leaderboardservice.controller;

import com.leaderboardservice.dto.SubmissionDto;
import com.leaderboardservice.service.LeaderboardService;
import com.leaderboardservice.utils.AppResponse;
import com.leaderboardservice.utils.ScoreResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class LeaderboardController {

    private final LeaderboardService leaderboardService;

    @PostMapping("/submissions")
    public ResponseEntity<ScoreResponse>  submitScore(@RequestBody SubmissionDto dto){
        ScoreResponse response = leaderboardService.submitScore(dto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping("/{username}/rank")
    public ResponseEntity<AppResponse>  getRank(@PathVariable String username){
        return ResponseEntity.ok(leaderboardService.getRank(username));
    }

    @GetMapping("/scores")
    public ResponseEntity<Object>  getTopPlayers(@RequestParam (defaultValue = "100") int size){
        return ResponseEntity.ok(leaderboardService.getTopPlayers(size));
    }

}
