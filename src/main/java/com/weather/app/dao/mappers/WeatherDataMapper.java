package com.weather.app.dao.mappers;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.weather.app.models.UserAccount;
import com.weather.app.models.WeatherInfo;

public class WeatherDataMapper implements RowMapper<WeatherInfo> {

	@Override
	public WeatherInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
		WeatherInfo weatherInfo = new WeatherInfo();
		weatherInfo.setUsername(rs.getString("username"));
		weatherInfo.setLocation(rs.getString("location"));
		return weatherInfo;
	}

}
