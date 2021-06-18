package com.weather.app.dao.daoInterfaces;

import java.util.ArrayList;

import com.weather.app.models.WeatherInfo;

public interface WeatherDao {

	public WeatherInfo addWeatherInfo(WeatherInfo weatherInfo);

	public ArrayList<WeatherInfo> getAllWeatherInfoData(String userName);

}
