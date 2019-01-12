package com.general.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Location{
	private Double lat;
	private Double lng;	
	public Location() {}
	
	public Double getLat() {
		return lat;
	}
	
	public void setLat(Double lat) {
		this.lat = lat;
	}
	
	public Double getLng() {
		return lng;
	}
	
	public void setLng(Double lng) {
		this.lng = lng;
	}
	
	@Override
	public String toString() {
		return "location={" +
                "lat=" + lat +
                ", lng=" + lng +
                "}";
	}
}