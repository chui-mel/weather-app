package com.fiona.weather.repository;

import com.fiona.weather.model.Weather;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WeatherRepository extends JpaRepository<Weather, Long> {
	Optional<Weather> findByCityAndCountry(String city, String country);
}
