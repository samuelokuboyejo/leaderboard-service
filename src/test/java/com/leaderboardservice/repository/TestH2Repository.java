package com.leaderboardservice.repository;

import com.leaderboardservice.model.Leaderboard;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TestH2Repository extends JpaRepository<Leaderboard, Integer> {
    Optional<Leaderboard> findByUsername(String username);
}
