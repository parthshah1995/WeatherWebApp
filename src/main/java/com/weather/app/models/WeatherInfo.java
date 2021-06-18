package com.weather.app.models;

public class WeatherInfo {
	int id;
	String username;
	String location;

	public WeatherInfo() {
		super();
		// TODO Auto-generated constructor stub
	}

	public WeatherInfo(int id, String username, String location) {
		super();
		this.id = id;
		this.username = username;
		this.location = location;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}
}
