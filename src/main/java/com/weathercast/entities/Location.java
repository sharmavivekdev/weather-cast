package com.weathercast.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(uniqueConstraints = {
        @UniqueConstraint(columnNames = {"coord_lat", "coord_lon", "address"})
})
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Location {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	@Embedded
	private Coord coord;

	@Column(nullable = true)
	private String city;

	@Column(nullable = true)
	private String state;
	
	@Column(nullable = true)
	private String country;
	
	@OneToOne
	@JoinColumn(name = "weather_report_id")
	@JsonIgnore
	private WeatherReport weatherReport;
}
