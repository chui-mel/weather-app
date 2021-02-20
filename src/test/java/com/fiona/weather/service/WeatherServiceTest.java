package com.fiona.weather.service;

import com.fiona.weather.dto.WeatherDto;
import com.fiona.weather.model.Weather;
import com.fiona.weather.repository.WeatherRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@SpringBootTest
class WeatherServiceTest {

	@Mock
	private WeatherRepository weatherRepository;

	@Mock
	private OpenWeatherService openWeatherService;

	@InjectMocks
	private WeatherService service;

	@Test
	void getWeather() {
		String city = "city";
		String country = "au";
		String description = "cloud";
		when(openWeatherService.getWeather(city, country)).thenReturn(description);

		WeatherDto weatherDto = service.getWeather(city, country);

		assertEquals(city, weatherDto.getCity());
		assertEquals(country, weatherDto.getCountry());
		assertEquals(description, weatherDto.getDescription());
	}

	@Test
	void getWeatherCorrectlyWhenSavingError() {
		String city = "city";
		String country = "au";
		String description = "cloud";
		when(openWeatherService.getWeather(city, country)).thenReturn(description);
    when(weatherRepository.save(any(Weather.class))).thenThrow(new RuntimeException("Error occurred"));

		WeatherDto weatherDto = service.getWeather(city, country);

		assertEquals(city, weatherDto.getCity());
		assertEquals(country, weatherDto.getCountry());
		assertEquals(description, weatherDto.getDescription());
	}

	@Test
	public void throwExceptionWhenFailedToFetchWeather() {
		String expectedMessage = "Error Occurred";
		when(openWeatherService.getWeather(anyString(), anyString())).thenThrow(new RuntimeException(expectedMessage));

		Exception exception = assertThrows(RuntimeException.class,
				() -> service.getWeather("city", "country"));

		String actualMessage = exception.getMessage();
		assertTrue(actualMessage.contains(expectedMessage));
	}
}