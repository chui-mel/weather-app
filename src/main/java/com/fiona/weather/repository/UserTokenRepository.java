package com.fiona.weather.repository;

import com.fiona.weather.model.UserToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserTokenRepository extends JpaRepository<UserToken, Long> {
	Optional<UserToken> findByToken(String token);
}
