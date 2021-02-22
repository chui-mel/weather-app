package com.fiona.weather.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.OffsetDateTime;

@Entity
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "weather")
public class Weather {
	@Id
	@GeneratedValue( strategy = GenerationType.IDENTITY)
	@Column(name = "id", updatable = false, nullable = false)
	private Long id;

	@Column(nullable = false)
	private String city;

	@Column(nullable = false)
	private String country;

	@Column
	private String description;

	@Column
	private OffsetDateTime updatedTime;
}
