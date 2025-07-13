package com.leaderboardservice.service;

import com.leaderboardservice.dto.SubmissionDto;
import com.leaderboardservice.model.Leaderboard;
import com.leaderboardservice.repository.LeaderboardRepo;
import com.leaderboardservice.utils.AppResponse;
import com.leaderboardservice.utils.LeaderboardResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class LeaderboardService {
    private final LeaderboardRepo repository;

    @CachePut(value = "score", key = "#dto.username")
    public AppResponse submitScore(SubmissionDto dto){
        Optional<Leaderboard> existingUser = repository.findByUsername(dto.getUsername());
        Leaderboard score = existingUser.map(Leaderboard -> {
            Leaderboard.setScore(dto.getScore());
            return Leaderboard;
        }).orElse(
                Leaderboard.builder()
                        .username(dto.getUsername())
                        .score(dto.getScore())
                        .submissionAt(LocalDateTime.now())
                        .build()
        );
        repository.save(score);
        String responseMessage = "Score submitted successfully";

        return AppResponse.builder()
                .responseMessage(responseMessage)
                .build();

    }


    @Cacheable(value = "topUsers")
    public List<LeaderboardResponse> getTopPlayers(int size) {
        List<Leaderboard> topUsers = repository.findTopN(PageRequest.of(0, size));
        List<LeaderboardResponse> responseList = new ArrayList<>();

        for (int i = 0; i < topUsers.size(); i++) {
            Leaderboard user = topUsers.get(i);
            LeaderboardResponse response = LeaderboardResponse.builder()
                    .username(user.getUsername())
                    .score(user.getScore())
                    .rank(i + 1)
                    .build();
            responseList.add(response);
        }

        return responseList;
    }


    @Cacheable(value = "score", key = "#username")
    public AppResponse getRank(String username){
        Optional<Leaderboard> user = repository.findByUsername(username);
        if (user.isEmpty()) {
            return AppResponse.builder()
                    .responseMessage("User not found")
                    .build();
        }
        int rank = repository.getRankForUser(username);

        return AppResponse.builder()
                .responseMessage("Rank displayed")
                .data(LeaderboardResponse.builder()
                        .username(user.get().getUsername())
                        .score(user.get().getScore())
                        .rank(rank)
                        .build())
                .build();
    }
}
