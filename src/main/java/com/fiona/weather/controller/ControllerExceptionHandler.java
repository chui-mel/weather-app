package com.fiona.weather.controller;

import com.fiona.weather.dto.WeatherErrorDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ControllerExceptionHandler {
	@ExceptionHandler(MissingServletRequestParameterException.class)
	public ResponseEntity<WeatherErrorDto> handleException(MissingServletRequestParameterException exception){
		log.warn("There is MissingServletRequestParameterException occurred");
		return new ResponseEntity(new WeatherErrorDto(exception.getMessage(), "Bad Request"), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<WeatherErrorDto> handleException(Exception exception){
		log.error("There is Exception occurred", exception);
		return new ResponseEntity(new WeatherErrorDto(exception.getMessage(), "error"), HttpStatus.SERVICE_UNAVAILABLE);
	}
}
