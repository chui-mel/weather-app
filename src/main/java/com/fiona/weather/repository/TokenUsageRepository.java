package com.fiona.weather.repository;

import com.fiona.weather.model.TokenUsage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;

@Repository
public interface TokenUsageRepository extends JpaRepository<TokenUsage, Long> {

	@Query(value = "SELECT COUNT(*) " +
			"FROM token_usage " +
			"WHERE token_id = :tokenId and access_time > :accessTime",
			nativeQuery = true)
	int countByTokenIdAfter(@Param("tokenId") Long token, @Param("accessTime") OffsetDateTime accessTime);
}
