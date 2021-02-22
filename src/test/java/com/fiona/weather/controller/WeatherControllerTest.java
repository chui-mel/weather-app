package com.fiona.weather.controller;

import com.fiona.weather.dto.WeatherDto;
import com.fiona.weather.exception.InvalidTokenException;
import com.fiona.weather.service.RateLimitService;
import com.fiona.weather.service.WeatherService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.OffsetDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class WeatherControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private WeatherService weatherService;

	@MockBean
	private RateLimitService rateLimitService;

	@Test
	public void getWeather() throws Exception {
		String city = "city";
		String country = "AU";
		String desc = "cloud";
		String token = "testToken";

		when(weatherService.getWeather(city, country)).thenReturn(new WeatherDto(city, country, desc));
		when(rateLimitService.exceededLimit(anyString(), any(OffsetDateTime.class))).thenReturn(false);

		mockMvc.perform(
				MockMvcRequestBuilders.get("/weather")
						.param("city", city)
						.param("country", country)
						.param("token", token)
						.contentType(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.city").value(city))
				.andExpect(jsonPath("$.country").value(country))
				.andExpect(jsonPath("$.description").value(desc));
	}

	@Test
	public void shouldReturnErrorIfMissCity() throws Exception {
		mockMvc.perform(
				MockMvcRequestBuilders.get("/weather")
						.param("country", "AU")
						.param("token", "token")
						.contentType(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.message").value("Required String parameter 'city' is not present"))
				.andExpect(jsonPath("$.details").value("Bad Request"));
	}


	@Test
	public void shouldReturnErrorIfCityIsEmpty() throws Exception {
		mockMvc.perform(
				MockMvcRequestBuilders.get("/weather")
						.param("city", "")
						.param("country", "AU")
						.param("token", "token")
						.contentType(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.message").value("getWeather.city: must not be blank"))
				.andExpect(jsonPath("$.details").value("Bad Request"));
	}

	@Test
	public void shouldReturnErrorIfCountryIsWrongFormat() throws Exception {
		mockMvc.perform(
				MockMvcRequestBuilders.get("/weather")
						.param("city", "city")
						.param("country", "country")
						.param("token", "token")
						.contentType(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.message").value("getWeather.country: Country should follow ISO 3166-1 alpha-2 standard"))
				.andExpect(jsonPath("$.details").value("Bad Request"));
	}

	@Test
	public void shouldReturnErrorIfExceedLimitation() throws Exception {
		when(rateLimitService.exceededLimit(anyString(), any(OffsetDateTime.class))).thenReturn(true);

		mockMvc.perform(
				MockMvcRequestBuilders.get("/weather")
						.param("city", "city")
						.param("country", "au")
						.param("token", "token")
						.contentType(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(status().isTooManyRequests())
				.andExpect(jsonPath("$.message").value("Exceed your limitation, please wait and retry"))
				.andExpect(jsonPath("$.details").value("Exceed limitation"));
	}

	@Test
	public void shouldReturnErrorIfInvalidToken() throws Exception {
		when(rateLimitService.exceededLimit(anyString(), any(OffsetDateTime.class))).thenThrow(InvalidTokenException.class);

		mockMvc.perform(
				MockMvcRequestBuilders.get("/weather")
						.param("city", "city")
						.param("country", "au")
						.param("token", "token")
						.contentType(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(status().isBadRequest());
	}
}