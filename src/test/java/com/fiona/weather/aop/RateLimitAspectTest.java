package com.fiona.weather.aop;

import com.fiona.weather.exception.ExceedLimitationException;
import com.fiona.weather.exception.InvalidTokenException;
import com.fiona.weather.model.UserToken;
import com.fiona.weather.repository.TokenUsageRepository;
import com.fiona.weather.repository.UserTokenRepository;
import org.aspectj.lang.ProceedingJoinPoint;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.OffsetDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
class RateLimitAspectTest {
	@Mock
	private UserTokenRepository userTokenRepository;

	@Mock
	private TokenUsageRepository tokenUsageRepository;

	@InjectMocks
	private RateLimitAspect rateLimitAspect;

	@Test
	void notExceededLimit() throws Throwable {
		String validToken = "valid";

		UserToken userToken = UserToken.builder().id(1L).token(validToken).rateLimit(5).build();
		when(userTokenRepository.findByToken(validToken)).thenReturn(Optional.of(userToken));
		when(tokenUsageRepository.countByTokenIdAfter(eq(1L), any(OffsetDateTime.class))).thenReturn(4);

		ProceedingJoinPoint joinPoint = mock(ProceedingJoinPoint.class);
		when(joinPoint.getArgs()).thenReturn(new String[]{"city", "country", validToken});
		rateLimitAspect.exceededLimit(joinPoint);

		verify(tokenUsageRepository).save(any());
	}


	@Test
	void exceededLimit() {
		String validToken = "valid";

		UserToken userToken = UserToken.builder().id(1L).token(validToken).rateLimit(5).build();
		when(userTokenRepository.findByToken(validToken)).thenReturn(Optional.of(userToken));
		when(tokenUsageRepository.countByTokenIdAfter(eq(1L), any(OffsetDateTime.class))).thenReturn(5);

		ProceedingJoinPoint joinPoint = mock(ProceedingJoinPoint.class);
		when(joinPoint.getArgs()).thenReturn(new String[]{"city", "country", validToken});

		assertThrows(ExceedLimitationException.class,
				() -> rateLimitAspect.exceededLimit(joinPoint));
	}

	@Test
	void invalidToken() {
		String invalidToken = "invalid";
		when(userTokenRepository.findByToken(invalidToken)).thenReturn(Optional.empty());

		ProceedingJoinPoint joinPoint = mock(ProceedingJoinPoint.class);
		when(joinPoint.getArgs()).thenReturn(new String[]{"city", "country", invalidToken});

		assertThrows(InvalidTokenException.class,
				() -> rateLimitAspect.exceededLimit(joinPoint));
	}
}