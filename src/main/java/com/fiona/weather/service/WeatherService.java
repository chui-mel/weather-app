package com.fiona.weather.service;

import com.fiona.weather.dto.WeatherDto;
import org.springframework.stereotype.Service;

@Service
public class WeatherService {
	public WeatherDto getWeather(String city, String country) {
		return new WeatherDto(city, country, "cloud");
	}
}
