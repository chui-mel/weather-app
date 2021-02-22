package com.fiona.weather.service;

import com.fiona.weather.exception.InvalidTokenException;
import com.fiona.weather.model.TokenUsage;
import com.fiona.weather.model.UserToken;
import com.fiona.weather.repository.TokenUsageRepository;
import com.fiona.weather.repository.UserTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RateLimitService {

	private final UserTokenRepository userTokenRepository;
	private final TokenUsageRepository tokenUsageRepository;

	public boolean exceededLimit(String userToken, OffsetDateTime accessTime) {
		Optional<UserToken> foundToken = userTokenRepository.findByToken(userToken);

		return foundToken.map(
				token -> {
					int count = tokenUsageRepository.countByTokenIdAfter(token.getId(), accessTime.minusHours(1L));
					if (count >= token.getRateLimit()) {
						return true;
					}
					tokenUsageRepository.save(TokenUsage.builder().token(token).accessTime(accessTime).build());
					return false;
				}
		).orElseThrow( () -> new InvalidTokenException("invalid token"));
	}
}
