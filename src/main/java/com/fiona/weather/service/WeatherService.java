package com.fiona.weather.service;

import com.fiona.weather.dto.WeatherDto;
import com.fiona.weather.model.Weather;
import com.fiona.weather.repository.WeatherRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.concurrent.atomic.AtomicReference;

@Slf4j
@Service
@RequiredArgsConstructor
public class WeatherService {
	private final WeatherRepository weatherRepository;
	private final OpenWeatherService openWeatherService;

	public WeatherDto getWeather(String city, String country) {
		AtomicReference<Long> foundId = new AtomicReference<>();

		Weather updatedWeather = weatherRepository.findByCityAndCountry(city, country)
				.filter(weather -> {
					foundId.set(weather.getId());
					return weather.getUpdatedTime().isAfter(OffsetDateTime.now().minusMinutes(10L));
				}).orElseGet(() -> fetchFromApiAndSave(city, country, foundId.get()));

		return new WeatherDto(city, country, updatedWeather.getDescription());
	}

	private Weather fetchFromApiAndSave(String city, String country, Long foundId) {
		String weatherInfo = openWeatherService.getWeather(city, country);

		Weather updatedWeather =  Weather.builder()
				.id(foundId)
				.city(city)
				.country(country)
				.description(weatherInfo)
				.updatedTime(OffsetDateTime.now())
				.build();

		saveWeather(updatedWeather);
		return updatedWeather;
	}

	private void saveWeather(Weather weather) {
		try {
			weatherRepository.save(weather);
		} catch (Exception e) {
			log.error("Something wrong when saving to database " + e.getMessage(), e);
		}
	}
}
