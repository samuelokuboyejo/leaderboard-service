package com.leaderboardservice.controller;

import com.leaderboardservice.dto.SubmissionDto;
import com.leaderboardservice.service.LeaderboardService;
import com.leaderboardservice.utils.AppResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/leaderboard")
public class LeaderboardController {

    private final LeaderboardService leaderboardService;

    @PostMapping("/submit")
    public ResponseEntity<AppResponse>  submitScore(@RequestBody SubmissionDto dto){
        return ResponseEntity.ok(leaderboardService.submitScore(dto));
    }

    @GetMapping("/rank/{username}")
    public ResponseEntity<AppResponse>  getRank(@PathVariable String username){
        return ResponseEntity.ok(leaderboardService.getRank(username));
    }

    @GetMapping("/top")
    public ResponseEntity<Object>  getTopPlayers(){
        return ResponseEntity.ok(leaderboardService.getTopPlayers());
    }

}
