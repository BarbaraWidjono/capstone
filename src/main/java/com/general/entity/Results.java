package com.general.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Results{
	private Geometry geometry;
	
	public Results() {}
	
	public Geometry getGeometry() {
		return geometry;
	}
	
	public void setGeometry(Geometry geometry) {
		this.geometry = geometry;
	}
	
	@Override
	public String toString() {
		return "results:[{" + geometry + "}]";
	}
}