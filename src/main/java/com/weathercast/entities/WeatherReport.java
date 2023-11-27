package com.weathercast.entities;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.weathercast.utils.OpenWeatherApi;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class WeatherReport {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	@Column(columnDefinition = "json")
	private String data;
	
	@OneToOne(mappedBy = "weatherReport", cascade = CascadeType.ALL)
	private Location location;
	
	private LocalDateTime updatedAt = LocalDateTime.now();
	
	 @JsonGetter("data")
    public Object getDataAsJson() {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readTree(data);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
	
	@JsonIgnore
	public boolean isStale() {
		Duration duration = Duration.between(this.getUpdatedAt(), LocalDateTime.now());
		return duration.toMinutes() >= OpenWeatherApi.THRESHOLD_IN_MINUTES;
	}
}
