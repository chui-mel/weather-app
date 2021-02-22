package com.fiona.weather.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fiona.weather.exception.CityNotFoundException;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Slf4j
@Service
public class OpenWeatherService {

	@Value("${openweather.api.url}")
	private String apiUrl;

	@Value("${openweather.api.token}")
	private String apiToken;

	private final static ObjectMapper MAPPER = new ObjectMapper();

	public String getWeather(String city, String country) {
		log.info("Fetching weather api for city: {} and country: {}", city, country);

		RestTemplate restTemplate = new RestTemplate();
		try {
			String response = restTemplate.getForObject(weatherUrl(city, country), String.class);
			return getWeatherDesc(response);
		} catch (HttpClientErrorException | HttpServerErrorException httpClientOrServerEx) {
			if (HttpStatus.NOT_FOUND.equals(httpClientOrServerEx.getStatusCode())) {
				throw new CityNotFoundException("Cannot find city: " + city);
			}
			throw new RuntimeException("Something wrong when accessing to OpenWeather API");
		}
	}

	@SneakyThrows
	private String getWeatherDesc(String response) {
		JsonNode weatherInfo = MAPPER.readTree(response);
		return weatherInfo.get("weather").get(0).get("description").asText();
	}

	private String weatherUrl(String city, String country) {
		return UriComponentsBuilder.fromHttpUrl(apiUrl)
				.queryParam("q", String.join(",", city, country))
				.queryParam("appid", apiToken)
				.toUriString();
	}
}
