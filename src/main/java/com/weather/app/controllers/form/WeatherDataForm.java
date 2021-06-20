package com.weather.app.controllers.form;

public class WeatherDataForm {
	String location;
	String temp;
	String description;
	String weather_Icon;
	String sunrisetime;
	String sunsettime;
	String userName;

	public WeatherDataForm() {
		super();
		// TODO Auto-generated constructor stub
	}

	public WeatherDataForm(String location, String temp, String description, String weather_Icon, String sunrisetime,
			String sunsettime, String userName) {
		super();
		this.location = location;
		this.temp = temp;
		this.description = description;
		this.weather_Icon = weather_Icon;
		this.sunrisetime = sunrisetime;
		this.sunsettime = sunsettime;
		this.userName = userName;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getSunrisetime() {
		return sunrisetime;
	}

	public void setSunrisetime(String sunrisetime) {
		this.sunrisetime = sunrisetime;
	}

	public String getSunsettime() {
		return sunsettime;
	}

	public void setSunsettime(String sunsettime) {
		this.sunsettime = sunsettime;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getTemp() {
		return temp;
	}

	public void setTemp(String temp) {
		this.temp = temp;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getWeather_Icon() {
		return weather_Icon;
	}

	public void setWeather_Icon(String weather_Icon) {
		this.weather_Icon = weather_Icon;
	}

	@Override
	public String toString() {
		return "WeatherDataForm [location=" + location + ", temp=" + temp + ", description=" + description
				+ ", weather_Icon=" + weather_Icon + ", sunrisetime=" + sunrisetime + ", sunsettime=" + sunsettime
				+ "]";
	}

}
