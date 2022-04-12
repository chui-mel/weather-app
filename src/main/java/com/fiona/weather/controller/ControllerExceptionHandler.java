package com.fiona.weather.controller;

import com.fiona.weather.dto.WeatherErrorDto;
import com.fiona.weather.exception.CityNotFoundException;
import com.fiona.weather.exception.ExceedLimitationException;
import com.fiona.weather.exception.InvalidTokenException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;

import javax.validation.ConstraintViolationException;

@Slf4j
@RestControllerAdvice
public class ControllerExceptionHandler {

	@ExceptionHandler({
			MissingServletRequestParameterException.class,
			ConstraintViolationException.class,
			InvalidTokenException.class })
	public ResponseEntity<WeatherErrorDto> handleBadRequestException(Exception exception){
		return new ResponseEntity(new WeatherErrorDto(exception.getMessage(), "Bad Request"), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(CityNotFoundException.class)
	public ResponseEntity<WeatherErrorDto> handleNotFoundException(CityNotFoundException exception){
		return new ResponseEntity(new WeatherErrorDto("City Not Found", "check your city and country"), HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(ExceedLimitationException.class)
	public ResponseEntity<WeatherErrorDto> handleExceedLimitException(ExceedLimitationException exception){
		return new ResponseEntity(new WeatherErrorDto("Exceed your limitation, please wait and retry", "Exceed limitation"), HttpStatus.TOO_MANY_REQUESTS);
	}

	@ExceptionHandler({
			HttpClientErrorException.class,
			Exception.class })
	public ResponseEntity<WeatherErrorDto> handleException(Exception exception){
		log.error("There is Exception occurred", exception);
		return new ResponseEntity(new WeatherErrorDto("Error happens in server side", "please contact admin"), HttpStatus.SERVICE_UNAVAILABLE);
	}
}
