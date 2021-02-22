package com.fiona.weather.repository;

import com.fiona.weather.model.TokenUsage;
import com.fiona.weather.model.UserToken;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.OffsetDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class TokenUsageRepositoryTest {

	@Autowired
	private TokenUsageRepository repository;

	@Autowired
	private UserTokenRepository userTokenRepository;

	private String token = "tokenForTest";

	private int rateLimit = 10;

	private Long generatedId = null;

	@BeforeEach
	void setUp() {
		generatedId = userTokenRepository.save(UserToken.builder()
				.token(token).rateLimit(rateLimit)
				.build()).getId();
	}

	@AfterEach
	void tearDown() {
		userTokenRepository.deleteById(generatedId);
	}

	@Test
	void countTokenUsageAfter() {
		Optional<UserToken> userToken = userTokenRepository.findByToken(token);
		repository.save(TokenUsage.builder().token(userToken.get()).accessTime(OffsetDateTime.now()).build());

		int count = repository.countByTokenIdAfter(generatedId, OffsetDateTime.now().minusHours(1));

		assertEquals(1, count);
	}
}