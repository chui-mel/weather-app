package com.fiona.weather.controller;

import com.fiona.weather.dto.WeatherDto;
import com.fiona.weather.service.WeatherService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotBlank;

@Slf4j
@RestController
@RequestMapping("weather")
@RequiredArgsConstructor
public class WeatherController {

	private final WeatherService weatherService;

	@GetMapping
	public ResponseEntity<WeatherDto> getWeather(@RequestParam("city") @NotBlank String city,
																							 @RequestParam ("country") @NotBlank String country) {
		return ResponseEntity.ok(weatherService.getWeather(city, country));
	}
}
