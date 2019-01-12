package com.general.entity;

import java.util.Arrays;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Response{
	private Results[] results;
	
	public Response() {}

	public Results[] getResults() {
		return results;
	}

	public void setResults(Results[] results) {
		this.results = results;
	}

	@Override
	public String toString() {
		return "Response [results=" + Arrays.toString(results) + "]";
	}
	
	
	
}