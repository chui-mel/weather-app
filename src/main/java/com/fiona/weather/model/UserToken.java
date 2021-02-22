package com.fiona.weather.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "user_token")
public class UserToken {
	@Id
	@GeneratedValue( strategy = GenerationType.IDENTITY)
	@Column(name = "id", updatable = false, nullable = false)
	private Long id;

	@Column(unique = true, nullable = false)
	private String token;

	@Column(nullable = false)
	private int rateLimit;

	@OneToMany(mappedBy = "token", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private List<TokenUsage> usages;
}
