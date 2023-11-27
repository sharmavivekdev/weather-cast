package com.weathercast.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.weathercast.entities.Coord;
import com.weathercast.entities.WeatherReport;

public interface WeatherReportRepository extends JpaRepository<WeatherReport, Long>{
	WeatherReport findByLocationCoord(Coord coord);
}
