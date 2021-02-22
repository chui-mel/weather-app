package com.fiona.weather.service;

import com.fiona.weather.exception.InvalidTokenException;
import com.fiona.weather.model.UserToken;
import com.fiona.weather.repository.TokenUsageRepository;
import com.fiona.weather.repository.UserTokenRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.OffsetDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
class RateLimitServiceTest {

	@Mock
	private UserTokenRepository userTokenRepository;

	@Mock
	private TokenUsageRepository tokenUsageRepository;

	@InjectMocks
	private RateLimitService service;

	@Test
	void notExceededLimit() {
		String validToken = "valid";
		OffsetDateTime accessTime = OffsetDateTime.now();

		UserToken userToken = UserToken.builder().id(1L).token(validToken).rateLimit(5).build();
		when(userTokenRepository.findByToken(validToken)).thenReturn(Optional.of(userToken));
		when(tokenUsageRepository.countByTokenIdAfter(1L, accessTime.minusHours(1L))).thenReturn(4);

		boolean exceeded = service.exceededLimit(validToken, accessTime);

		assertFalse(exceeded);
		verify(tokenUsageRepository).save(any());
	}


	@Test
	void exceededLimit() {
		String validToken = "valid";
		OffsetDateTime accessTime = OffsetDateTime.now();

		UserToken userToken = UserToken.builder().id(1L).token(validToken).rateLimit(5).build();
		when(userTokenRepository.findByToken(validToken)).thenReturn(Optional.of(userToken));
		when(tokenUsageRepository.countByTokenIdAfter(1L, accessTime.minusHours(1L))).thenReturn(5);

		boolean exceeded = service.exceededLimit(validToken, accessTime);

		assertTrue(exceeded);
		verify(tokenUsageRepository, times(0)).save(any());
	}

	@Test
	void invalidToken() {
		String invalidToken = "invalid";
		when(userTokenRepository.findByToken(invalidToken)).thenReturn(Optional.empty());

		assertThrows(InvalidTokenException.class,
				() -> service.exceededLimit(invalidToken, OffsetDateTime.now()));
	}
}