package com.weather.app.controllers.form.validator;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.weather.app.controllers.form.WeatherDataForm;
import com.weather.app.dao.daoInterfaces.WeatherDao;
import com.weather.app.models.WeatherInfo;

@Component
public class WeatherDataValidation implements Validator {

	@Autowired
	WeatherDao weatherDao;

	@Override
	public boolean supports(Class<?> clazz) {
		// TODO Auto-generated method stub
		return false;

	}

	@Override
	public void validate(Object target, Errors errors) {
		WeatherDataForm weatherDataForm = (WeatherDataForm) target;
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "location", "NotEmpty");
		ArrayList<WeatherInfo> weatherInfoList = weatherDao.getAllWeatherInfoData(weatherDataForm.getUserName());
		for (WeatherInfo weatherInfoDto : weatherInfoList) {
			if (weatherInfoDto.getLocation().equalsIgnoreCase(weatherDataForm.getLocation())) {
				errors.rejectValue("location", "Exist Location");
			}
		}
	}

}
