package com.fiona.weather.model;

import lombok.Builder;
import lombok.Getter;

import javax.persistence.*;
import java.time.OffsetDateTime;

@Entity
@Builder
@Getter
@Table(name = "weather")
public class Weather {
	@Id
	@GeneratedValue( strategy = GenerationType.IDENTITY)
	@Column(name = "id", updatable = false, nullable = false)
	private Long id;

	@Column(unique = true, nullable = false)
	private String city;

	@Column
	private String country;

	@Column
	private String description;

	@Column
	private OffsetDateTime createdTime;

	@Column
	private OffsetDateTime updatedTime;
}
