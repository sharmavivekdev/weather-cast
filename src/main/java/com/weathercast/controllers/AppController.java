package com.weathercast.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.weathercast.entities.Coord;
import com.weathercast.entities.Location;
import com.weathercast.entities.WeatherReport;
import com.weathercast.services.WeatherReportService;


@Controller
public class AppController {
	@Autowired
	WeatherReportService weatherReportService;
	
	@GetMapping("/")
	public String index() {
		return "index";
	}
	@GetMapping("/weather")
	public ResponseEntity<WeatherReport> getWeatherData(@RequestParam Double lat, @RequestParam Double lon){
		Coord coord = new Coord(lat, lon);
		WeatherReport weatherReport = weatherReportService.getWeatherReport(coord);
		
		ObjectMapper objectMapper = new ObjectMapper();
		
		try {
			JsonNode jsonNode = objectMapper.readValue(weatherReport.getData(), JsonNode.class);
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return ResponseEntity.ok(weatherReport);
	}
	@GetMapping("/locations")
	public ResponseEntity<List<Location>> getLocationsList(@RequestParam String q, @RequestParam(required = false, defaultValue = "10") int limit){
		return ResponseEntity.ok(weatherReportService.getLocations(q, limit));
	}
}
