package com.fiona.weather.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
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

	@SneakyThrows
	public String getWeather(String city, String country) {
		RestTemplate restTemplate = new RestTemplate();
		String response = restTemplate.getForObject(weatherUrl(city, country), String.class);

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
