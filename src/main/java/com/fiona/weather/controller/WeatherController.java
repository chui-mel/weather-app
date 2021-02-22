package com.fiona.weather.controller;

import com.fiona.weather.dto.WeatherDto;
import com.fiona.weather.exception.ExceedLimitationException;
import com.fiona.weather.service.RateLimitService;
import com.fiona.weather.service.WeatherService;
import com.fiona.weather.validator.ISO3166CountryCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotBlank;
import java.time.OffsetDateTime;

@Slf4j
@RestController
@RequestMapping("weather")
@RequiredArgsConstructor
@Validated
public class WeatherController {

	private final RateLimitService rateLimitService;
	private final WeatherService weatherService;

	@GetMapping
	public ResponseEntity<WeatherDto> getWeather(@RequestParam("city") @NotBlank String city,
																							 @RequestParam("country") @ISO3166CountryCode String country,
																							 @RequestParam("token") @NotBlank String token) {

		if (rateLimitService.exceededLimit(token, OffsetDateTime.now())) {
			throw new ExceedLimitationException();
		}
		return ResponseEntity.ok(weatherService.getWeather(city, country));
	}
}
