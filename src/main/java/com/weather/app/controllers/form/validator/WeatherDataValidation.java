package com.weather.app.controllers.form.validator;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.weather.app.controllers.form.WeatherDataForm;

@Component
public class WeatherDataValidation implements Validator {

	@Override
	public boolean supports(Class<?> clazz) {
		// TODO Auto-generated method stub
		return false;

	}

	@Override
	public void validate(Object target, Errors errors) {
		WeatherDataForm weatherDataForm = (WeatherDataForm) target;
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "location", "NotEmpty");

	}

}
