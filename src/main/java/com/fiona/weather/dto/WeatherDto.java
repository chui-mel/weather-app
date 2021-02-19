package com.fiona.weather.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WeatherDto {
	private String city;
	private String country;
	private String description;
}
