package com.weathercast.services;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.weathercast.entities.Coord;
import com.weathercast.entities.Location;
import com.weathercast.entities.WeatherReport;
import com.weathercast.repositories.WeatherReportRepository;
import com.weathercast.utils.OpenWeatherApi;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class WeatherReportService {
	@Autowired
	public WeatherReportRepository weatherReportRepository;
	
	@Autowired
	OpenWeatherApi openWeatherApi;
	
	private WeatherReport refreshWeatherReport(WeatherReport weatherReport, Location location) {
		log.debug("fetching weather data...");
		
		JsonNode jsonNode = openWeatherApi.getWeatherReport(location.getCoord().getLatitude(), location.getCoord().getLongitude(), JsonNode.class);
		
		weatherReport.setData(jsonNode.toString());
		weatherReport.setUpdatedAt(LocalDateTime.now());
		
		location.setWeatherReport(weatherReport);
		weatherReport.setLocation(location);
		
		return saveWeatherReport(weatherReport);
	}
	private WeatherReport refreshWeatherReport(WeatherReport weatherReport, Coord coord) {
		if(weatherReport == null) weatherReport = new WeatherReport();
		
		JsonNode jsonNode = openWeatherApi.getGeolocationByCoord(coord.getLatitude(), coord.getLongitude(), 1, JsonNode.class).get(0);
		
		Location location = weatherReport.getLocation();
		
		if(location == null) {
			location = new Location();
			
			if(jsonNode.has("name")) location.setCity(jsonNode.get("name").asText());
			if(jsonNode.has("state")) location.setState(jsonNode.get("state").asText());
			if(jsonNode.has("country")) location.setCountry(jsonNode.get("country").asText());

			location.setCoord(coord);
			location.setWeatherReport(weatherReport);
		}
		
		return refreshWeatherReport(weatherReport, location);
	}
	public WeatherReport saveWeatherReport(WeatherReport weatherReport) {
		return weatherReportRepository.save(weatherReport);
	}
	public List<Location> getLocations(String search, int limit){
		List<Location> locations = new ArrayList<>();
		
		JsonNode jsonNode = openWeatherApi.getGeolocationByAddress(search, limit, JsonNode.class);
		
		if(jsonNode.isArray()) {
			for(JsonNode locationNode : jsonNode) {
				Location location = new Location();
				if(locationNode.has("name")) location.setCity(locationNode.get("name").asText());
				if(locationNode.has("state")) location.setState(locationNode.get("state").asText());
				if(locationNode.has("country")) location.setCountry(locationNode.get("country").asText());
				location.setCoord(new Coord(locationNode.get("lat").asDouble(), locationNode.get("lon").asDouble()));
				locations.add(location);
			}
		}
		
		return locations;
	}
	public WeatherReport getWeatherReport(Coord coord) {
		WeatherReport weatherReport = weatherReportRepository.findByLocationCoord(coord);
		
		if(weatherReport == null || weatherReport.isStale()) weatherReport = refreshWeatherReport(weatherReport, coord);
		
		return weatherReport;
	}
}
