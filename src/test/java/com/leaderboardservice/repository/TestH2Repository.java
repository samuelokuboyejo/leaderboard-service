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

    @Query(value = """
        SELECT COUNT(*) + 1 FROM Leaderboards
        WHERE score > (
            SELECT score FROM Leaderboards WHERE username = :username
        )
    """, nativeQuery = true)
    Integer getRankForUser(@Param("username") String username);



    @Query("SELECT u FROM Leaderboard u ORDER BY u.score DESC")
    List<Leaderboard> findTopN(Pageable pageable);
}
