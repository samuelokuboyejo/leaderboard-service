package com.leaderboardservice.repository;

import com.leaderboardservice.model.UserScore;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LeaderboardRepo extends JpaRepository<UserScore, Integer> {

    Optional<UserScore> findByUsername(String username);
     List<UserScore> findTop100ByOrderByScoreDesc();
}
