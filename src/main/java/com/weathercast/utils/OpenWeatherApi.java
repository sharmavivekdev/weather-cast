package com.weathercast.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.weathercast.entities.Coord;


@Component
public class OpenWeatherApi {
	public static final int THRESHOLD_IN_MINUTES = 30;
	final String apiUrl = "https://api.openweathermap.org";
	
	@Value("${openweather.api.key}")
	String apiKey;
	
	RestTemplate restTemplate = new RestTemplate();
	
	public String getWeatherApiUrl(double lat, double lon) {
		return apiUrl + "/data/2.5/weather?appid=" + apiKey + "&lat=" + lat + "&lon=" + lon;
	}
	public String getGeoDirectApiUrl(String q, int limit) {
		return apiUrl + "/geo/1.0/direct?appid=" + apiKey + "&q=" + q + "&limit=" + limit;
	}
	public String getGeoReverseApiUrl(double lat, double lon, int limit) {
		return apiUrl + "/geo/1.0/reverse?appid=" + apiKey + "&lat=" + lat + "&lon=" + lon + "&limit=" + limit;
	}
	public <T> T getWeatherReport(double lat, double lon, Class<T> responseType) {
		String url = getWeatherApiUrl(lat, lon);
		return restTemplate.getForObject(url, responseType);
	}
	public <T> T getGeolocationByAddress(String address, int limit, Class<T> responseType) {
		String url = getGeoDirectApiUrl(address, limit);
		return restTemplate.getForObject(url, responseType); 
	}
	public <T> T getGeolocationByCoord(double lat, double lon, int limit, Class<T> responseType) {
		String url = getGeoReverseApiUrl(lat, lon, limit);
		return restTemplate.getForObject(url, responseType); 
	}
	public Coord getCoord(String address, int limit) {
		JsonNode response = getGeolocationByAddress(address, limit, JsonNode.class);
		
		Coord coord = new Coord(
				response.get(0).get("lat").asDouble(), 
				response.get(0).get("lon").asDouble()
			);
		
		return coord; 
	}
	
	
}
