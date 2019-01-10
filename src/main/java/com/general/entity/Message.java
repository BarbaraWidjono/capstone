package com.general.entity;

public class Message{
	private long id;
	private String resource;
	private String address;
	private String resourcenumber;
	private String info;
	private String usernumber;
	
	public long getId() {
		return id;
	}
	
	public void setId(long id) {
		this.id = id;
	}
	
	public String getResource() {
		return resource;
	}
	
	public void setResource(String resource) {
		this.resource = resource;
	}
	
	public String getAddress() {
		return address;
	}
	
	public void setAddress(String address) {
		this.address = address;
	}
	
	public String getResourcenumber() {
		return resourcenumber;
	}
	
	public void setResourcenumber(String resourcenumber) {
		this.resourcenumber = resourcenumber;
	}
	
	public String getInfo() {
		return info;
	}
	
	public void setInfo(String info) {
		this.info = info;
	}
	
	public String getUsernumber() {
		return usernumber;
	}
	
	public void setUsernumber(String usernumber) {
		this.usernumber = usernumber;
	}
}