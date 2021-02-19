package com.fiona.weather.controller;

import com.fiona.weather.dto.WeatherDto;
import com.fiona.weather.service.WeatherService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

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

	@Test
	public void getWeather() throws Exception {
		String city = "city";
		String country = "country";
		String desc = "cloud";

		when(weatherService.getWeather(city, country)).thenReturn(new WeatherDto(city, country, desc));

		mockMvc.perform(
				MockMvcRequestBuilders.get("/weather")
						.param("city", city)
						.param("country", country)
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
						.param("country", "country")
						.contentType(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.message").value("Required String parameter 'city' is not present"))
				.andExpect(jsonPath("$.details").value("Bad Request"));
	}
}