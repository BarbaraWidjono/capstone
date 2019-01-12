package com.general.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Geometry{
	private Location location;
	
	public Geometry() {}
	
	public Location getLocation() {
		return location;
	}
	
	public void setLocation(Location location) {
		this.location = location;
	}
	
	@Override
	public String toString() {
		return "geometry{" +
                "location=" + location +
                "}";
	}
}