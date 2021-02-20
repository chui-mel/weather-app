package com.fiona.weather.service;

import com.fiona.weather.dto.WeatherDto;
import com.fiona.weather.model.Weather;
import com.fiona.weather.repository.WeatherRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class WeatherService {
	private final WeatherRepository weatherRepository;
	private final OpenWeatherService openWeatherService;

	public WeatherDto getWeather(String city, String country) {
		Weather weather = fetchFromApi(city, country);
		saveWeather(weather);
		return new WeatherDto(city, country, weather.getDescription());
	}

	private Weather fetchFromApi(String city, String country) {
		String weatherInfo = openWeatherService.getWeather(city, country);

		return Weather.builder()
				.city(city)
				.country(country)
				.description(weatherInfo)
				.createdTime(OffsetDateTime.now())
				.updatedTime(OffsetDateTime.now())
				.build();
	}

	private void saveWeather(Weather weather) {
		try {
			weatherRepository.save(weather);
		} catch (Exception e) {
			log.error("Something wrong when saving to database " + e.getMessage(), e);
		}
	}
}
