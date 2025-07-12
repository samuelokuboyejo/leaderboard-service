package com.leaderboardservice;

import com.leaderboardservice.dto.SubmissionDto;
import com.leaderboardservice.model.Leaderboard;
import com.leaderboardservice.repository.TestH2Repository;
import com.leaderboardservice.service.LeaderboardService;
import com.leaderboardservice.utils.AppResponse;
import com.leaderboardservice.utils.LeaderboardResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@EnableCaching
@ActiveProfiles("test")
@AutoConfigureTestDatabase
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class LeaderboardServiceApplicationTests {

	@LocalServerPort
	private int port;

	private String baseUrl = "http://localhost";

	private static RestTemplate restTemplate;

	@Autowired
	private LeaderboardService leaderboardService;

	@Autowired
	private TestH2Repository h2Repository;

	@BeforeAll
	public static void init(){
		restTemplate = new RestTemplate();
	}

	@BeforeEach
	void cleanUp() {
		h2Repository.deleteAll();
	}

	@BeforeEach
	public void setUp(){
		baseUrl = baseUrl.concat(":").concat(port + "");
	}

	@Test
	void testSubmitScore_ValidScore(){
		SubmissionDto submissionDto = new SubmissionDto("thanos", 120);
		AppResponse result = leaderboardService.submitScore(submissionDto);
		Assertions.assertEquals("Score submitted successfully", result.getResponseMessage());
		assertThat(h2Repository.findByUsername("thanos")).isPresent();
	}

	@Test
	void testGetTopPlayers() {
		h2Repository.save( Leaderboard.builder().username("sam").score(400).build());
		h2Repository.save(Leaderboard.builder().username("tim").score(600).build());
		h2Repository.save(Leaderboard.builder().username("alice").score(550).build());
		List<LeaderboardResponse> topPlayers = leaderboardService.getTopPlayers(3);

		Assertions.assertEquals("tim", topPlayers.get(0).getUsername());
		Assertions.assertEquals("alice", topPlayers.get(1).getUsername());
		Assertions.assertEquals(2,topPlayers.get(1).getRank());
	}

	@Test
	void testGetRank() {
		h2Repository.save( Leaderboard.builder().username("sam").score(400).build());
		h2Repository.save(Leaderboard.builder().username("tim").score(600).build());
		h2Repository.save(Leaderboard.builder().username("alice").score(550).build());

		AppResponse result = leaderboardService.getRank("tim");
		LeaderboardResponse data = (LeaderboardResponse) result.getData();

		Assertions.assertEquals("Rank displayed", result.getResponseMessage());
		Assertions.assertEquals("tim", data.getUsername());
		Assertions.assertEquals(1, data.getRank());

	}

}
