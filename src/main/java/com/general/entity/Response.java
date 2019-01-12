package com.general.entity;

import java.util.List;

import org.apache.logging.log4j.util.Strings;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Response {
	private List<Results> results;
	
	public Response() {}

	public List<Results> getResults() {
		return results;
	}

	public void setResults(List<Results> results) {
		this.results = results;
	}

	@Override
	public String toString() {
		return "Response [results=" + Strings.join(getResults(), ',') + "]";
	}
	
	
	
}