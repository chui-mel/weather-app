package com.fiona.weather.aop;

import com.fiona.weather.exception.ExceedLimitationException;
import com.fiona.weather.exception.InvalidTokenException;
import com.fiona.weather.model.TokenUsage;
import com.fiona.weather.model.UserToken;
import com.fiona.weather.repository.TokenUsageRepository;
import com.fiona.weather.repository.UserTokenRepository;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;
import java.util.Optional;

@Aspect
@Component
@RequiredArgsConstructor
public class RateLimitAspect {

	private final UserTokenRepository userTokenRepository;
	private final TokenUsageRepository tokenUsageRepository;

	@Around("@annotation(RateLimit)")
	public Object exceededLimit(ProceedingJoinPoint joinPoint) throws Throwable {
		Object[] values = joinPoint.getArgs();
		String userToken = (String) values[2];

		Optional<UserToken> foundToken = userTokenRepository.findByToken(userToken);

		OffsetDateTime accessTime = OffsetDateTime.now();
		boolean exceeded =  foundToken.map(
				token -> {
					int count = tokenUsageRepository.countByTokenIdAfter(token.getId(), accessTime.minusHours(1L));
					if (count >= token.getRateLimit()) {
						return true;
					}
					tokenUsageRepository.save(TokenUsage.builder().token(token).accessTime(accessTime).build());
					return false;
				}
		).orElseThrow( () -> new InvalidTokenException("invalid token"));

		if (exceeded) {
			throw new ExceedLimitationException();
		}

		return joinPoint.proceed();
	}
}
