package com.fiona.weather.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class WeatherErrorDto {
	private String message;
	private String details;
}
