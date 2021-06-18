package com.weather.app.controllers.form.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.weather.app.controllers.form.Registrationform;
import com.weather.app.dao.daoInterfaces.UserDao;
import com.weather.app.models.UserAccount;

@Component
public class RegistrationFormValidation implements Validator {

	@Autowired
	UserDao userDao;

	@Override
	public boolean supports(Class<?> clazz) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void validate(Object target, Errors errors) {
		Registrationform webForm = (Registrationform) target;
		UserAccount userAccount = userDao.getUserByUserName(webForm.getUserName());
		System.out.println(userAccount);
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password", "NotEmpty");
		if (webForm.getPassword().length() < 8 || webForm.getPassword().length() > 32) {
			errors.rejectValue("password", "Size.webForm.password");
		}
		if (!webForm.getPassword().equals(webForm.getConfirmPassword()))
			errors.rejectValue("confirmPassword", "Diff.webForm.confimPassword");
		if(userAccount.getUserName()!=null)
			errors.rejectValue("userName", "Exist.webForm.userName");
	}

}
