package com.fiona.weather.service;

import com.fiona.weather.exception.CityNotFoundException;
import com.github.tomakehurst.wiremock.WireMockServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@ActiveProfiles("test")
class OpenWeatherServiceTest {

	private WireMockServer wireMockServer;

	@Autowired
	private OpenWeatherService service;

	@BeforeEach
	public void setup () {
		wireMockServer = new WireMockServer(9999);
		wireMockServer.start();
	}

	@AfterEach
	public void teardown () {
		wireMockServer.stop();
	}

	@Test
	void getWeather() {
		wireMockServer.stubFor(get(urlEqualTo("/weather?q=melbourne,au&appid=token"))
				.willReturn(aResponse().withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
						.withStatus(200)
						.withBody(" {" +
								"     \"coord\": {" +
								"       \"lon\": -0.13," +
								"       \"lat\": 51.51" +
								"     }," +
								"     \"weather\": [" +
								"       {" +
								"         \"id\": 300," +
								"         \"main\": \"Drizzle\"," +
								"         \"description\": \"light intensity drizzle\"," +
								"         \"icon\": \"09d\"" +
								"       }" +
								"     ]}")));


		String desc = service.getWeather("melbourne", "au");
		assertEquals("light intensity drizzle", desc);
	}

	@Test
	void throwErrorWhenErrorOccursToGetWeather() {
		wireMockServer.stubFor(get(urlEqualTo("/weather?q=melbourne,au&appid=token"))
				.willReturn(aResponse().withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
						.withStatus(400)));

		assertThrows(RuntimeException.class, () -> service.getWeather("melbourne", "au"));
	}

	@Test
	void throwErrorWhenCannotGetDescFromResponse() {
		wireMockServer.stubFor(get(urlEqualTo("/weather?q=melbourne,au&appid=token"))
				.willReturn(aResponse().withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
						.withStatus(200)
						.withBody(" {" +
										"     \"coord\": {" +
										"       \"lon\": -0.13," +
										"       \"lat\": 51.51" +
										"     }}")));

		assertThrows(RuntimeException.class, () -> service.getWeather("melbourne", "au"));
	}

	@Test
	void throwErrorWhenNotFountOccursToGetWeather() {
		wireMockServer.stubFor(get(urlEqualTo("/weather?q=melbourne,au&appid=token"))
				.willReturn(aResponse().withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
						.withStatus(404)));

		assertThrows(CityNotFoundException.class, () -> service.getWeather("melbourne", "au"));
	}
}