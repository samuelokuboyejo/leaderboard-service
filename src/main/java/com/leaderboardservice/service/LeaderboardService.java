package com.leaderboardservice.service;

import com.leaderboardservice.dto.SubmissionDto;
import com.leaderboardservice.model.UserScore;
import com.leaderboardservice.repository.LeaderboardRepo;
import com.leaderboardservice.utils.AppResponse;
import com.leaderboardservice.utils.LeaderboardResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class LeaderboardService {
    private final LeaderboardRepo repository;

    @CachePut(value = "score", key = "#dto.username")
    public AppResponse submitScore(SubmissionDto dto){
        Optional<UserScore> existingUser = repository.findByUsername(dto.getUsername());
        UserScore score = existingUser.map(userScore -> {
            userScore.setScore(dto.getScore());
            return userScore;
        }).orElse(
                UserScore.builder()
                        .username(dto.getUsername())
                        .score(dto.getScore())
                        .date(LocalDateTime.now())
                        .build()
        );
        repository.save(score);
        String responseMessage = "Score submitted successfully";

        return AppResponse.builder()
                .responseMessage(responseMessage)
                .build();

    }


    @Cacheable
    public List<LeaderboardResponse> getTopPlayers() {
        List<UserScore> topUsers = repository.findTop100ByOrderByScoreDesc();

        int[] rank = {1};
        return topUsers.stream()
                .map(user -> LeaderboardResponse.builder()
                        .username(user.getUsername())
                        .score(user.getScore())
                        .rank(rank[0]++)
                        .build())
                .collect(Collectors.toList());
    }


    @Cacheable(value = "score", key = "#username")
    public AppResponse getRank(String username){
        System.out.println(" getRank called with: " + username);
        List<UserScore> sorted = repository.findAll()
                .stream()
                .sorted(Comparator.comparingInt(UserScore::getScore).reversed())
                .collect(Collectors.toList());

        Map<String, LeaderboardResponse> rankMap = new HashMap<>();
        int rank = 1;

        for (UserScore user : sorted) {
            rankMap.put(user.getUsername().toLowerCase(), LeaderboardResponse.builder()
                    .username(user.getUsername())
                    .score(user.getScore())
                    .rank(rank++)
                    .build());
        }
            LeaderboardResponse result = rankMap.get(username.toLowerCase());

            if (result != null) {
                return AppResponse.builder()
                        .responseMessage("Rank displayed")
                        .data(result)
                        .build();
            } else {
                return AppResponse.builder()
                        .responseMessage("User not found")
                        .build();
            }
    }

    }
