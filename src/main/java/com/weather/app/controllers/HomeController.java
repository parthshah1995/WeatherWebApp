package com.weather.app.controllers;

import javax.validation.Valid;

import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.validation.ObjectError;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.weather.app.controllers.form.Registrationform;
import com.weather.app.controllers.form.WeatherDataForm;
import com.weather.app.controllers.form.validator.RegistrationFormValidation;
import com.weather.app.dao.daoInterfaces.UserDao;
import com.weather.app.dao.daoInterfaces.WeatherDao;
//import javax.validation.Valid;
import com.weather.app.models.UserAccount;
import com.weather.app.models.WeatherInfo;

@SpringBootApplication
@EnableScheduling
@Controller
public class HomeController {

	@Autowired
	private UserDao userDao;

	@Autowired
	private WeatherDao weatherDao;

	private WeatherDataForm currentWeatherData;

	private ArrayList<WeatherDataForm> weatherDataFormList = new ArrayList<WeatherDataForm>();

	@Autowired
	private RegistrationFormValidation registrationValidation;

	private int count = 1;

	@GetMapping("/registration")
	public String registration(Registrationform userForm, BindingResult bindingResult, Model model) {
		model.addAttribute("userAccount", userForm);
		return "registration";
	}

	@RequestMapping(value = { "/home", "/loadWeather", "/saveWeather" }, method = RequestMethod.GET)
	public String getHomePage(UserAccount userAccount, Model model, Principal principal) {
		userAccount = new UserAccount();
		userAccount.setUserName(principal.getName());
		WeatherDataForm weatherDataForm = new WeatherDataForm();
		System.out.println("List:" + weatherDataFormList);
		List<WeatherDataForm> weatherDataForms;
		if (weatherDataFormList.isEmpty())
			weatherDataForms = displayAllSavedWeatherLocationsData(principal.getName(), true);
		else
			weatherDataForms = displayAllSavedWeatherLocationsData(principal.getName(), false);
		model.addAttribute("weatherDataList", weatherDataForms);
		model.addAttribute("weatherDetail", weatherDataForm);
		model.addAttribute("weatherData", weatherDataForm);
		model.addAttribute("user", userAccount);
		return "home";
	}

	@RequestMapping(value = "/loadWeather", method = RequestMethod.POST)
	public String fetchWeather(WeatherDataForm weatherDataForm, Model model, Principal principal) {
		weatherDataForm = retriveWeatherData(weatherDataForm.getLocation());
		model.addAttribute("weatherDetail", weatherDataForm);
		UserAccount userAccount = new UserAccount();
		userAccount.setUserName(principal.getName());
		currentWeatherData = weatherDataForm;
		List<WeatherDataForm> weatherDataForms;
		if (weatherDataFormList.isEmpty())
			weatherDataForms = displayAllSavedWeatherLocationsData(principal.getName(), true);
		else
			weatherDataForms = displayAllSavedWeatherLocationsData(principal.getName(), false);
		weatherDataForm = new WeatherDataForm();
		model.addAttribute("weatherData", weatherDataForm);
		model.addAttribute("user", userAccount);
		return "home";
	}

	@RequestMapping(value = "/saveWeather", method = RequestMethod.POST)
	public String saveWeather(WeatherDataForm weatherDataForm, Model model, Principal principal) {

		WeatherInfo weatherInfo = new WeatherInfo();
		weatherInfo.setLocation(weatherDataForm.getLocation());
		weatherInfo.setUsername(principal.getName());
		weatherDao.addWeatherInfo(weatherInfo);

		List<WeatherDataForm> weatherDataForms = displayAllSavedWeatherLocationsData(principal.getName(), true);

		weatherDataForm = currentWeatherData;
		model.addAttribute("weatherDetail", weatherDataForm);
		UserAccount userAccount = new UserAccount();

		userAccount.setUserName(principal.getName());
		weatherDataForm = new WeatherDataForm();
		model.addAttribute("weatherData", weatherDataForm);
		model.addAttribute("user", userAccount);
		return "home";
	}

	@Scheduled(fixedRate = 5000)
	private void test() {
		System.out.println(count);
		count++;
	}

	private List<WeatherDataForm> displayAllSavedWeatherLocationsData(String userName, boolean isNewLocationAddded) {

		if (isNewLocationAddded) {
			ArrayList<WeatherInfo> weatherInfoList = weatherDao.getAllWeatherInfoData(userName);
			ArrayList<String> weatherLocationList = new ArrayList<>();
			for (WeatherInfo weatherInfo : weatherInfoList) {
				weatherLocationList.add(weatherInfo.getLocation());
			}
			weatherLocationList = new ArrayList<>(new LinkedHashSet<>(weatherLocationList));
			for (String weatherInfoLocation : weatherLocationList) {
				System.out.println(weatherInfoLocation);
				WeatherDataForm weatherDataForm = retriveWeatherData(weatherInfoLocation);
				weatherDataFormList.add(weatherDataForm);
			}
		}
		return weatherDataFormList;
	}

	@RequestMapping(value = "/registration", method = RequestMethod.POST)
	public String setupUser(@Valid Registrationform userForm, final BindingResult result, Model model,
			Principal principal) {
		registrationValidation.validate(userForm, result);
		if (result.hasErrors()) {
			List<ObjectError> errors = result.getAllErrors();
			for (ObjectError e : errors) {
				if (e.getCode().equals("Exist.webForm.userName"))
					model.addAttribute("UserExist", true);
				if (e.getCode().equals("Size.webForm.password")) {
					model.addAttribute("PasswordShort", true);
				} else if (e.getCode().equals("Diff.webForm.confimPassword")) {
					model.addAttribute("PasswordNoMatch", true);
				}
			}
			model.addAttribute("userAccount", userForm);
			return "registration";
		} else {
			UserAccount userAccount = new UserAccount();
			userAccount.setUserName(userForm.getUserName());
			userAccount.setPassword(encodePassword(userForm.getPassword()));
			userDao.createUserAccount(userAccount);
			autoLogin(userAccount);
			model.addAttribute("user", userAccount);
			return "redirect:/home";
		}
	}

	@RequestMapping(value = "/403", method = RequestMethod.GET)
	public String accessDenied(Model model, Principal principal) {
		String username = principal.getName();
		model.addAttribute("message", "Sorry " + username + " You don't have privilages to view this page!!!");
		return "error/403";
	}

	private void autoLogin(UserAccount userAccount) {
		Authentication auth = new UsernamePasswordAuthenticationToken(userAccount.getUserName(), null,
				getGrantedAuthorities(userAccount));
		SecurityContextHolder.getContext().setAuthentication(auth);
	}

	private Collection<GrantedAuthority> getGrantedAuthorities(UserAccount uesAccount) {
		return AuthorityUtils.commaSeparatedStringToAuthorityList("ROLE_USER");
	}

	private String encodePassword(String rawPassword) {
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		String encryptedPassword = passwordEncoder.encode(rawPassword);
		return encryptedPassword;
	}

	private WeatherDataForm retriveWeatherData(String location) {
		WeatherDataForm weatherDataForm = new WeatherDataForm();
		RestTemplate restTemplate = new RestTemplate();
		String openWeatherUrl = "https://api.openweathermap.org/data/2.5/weather?q=" + location
				+ "&appid=1a67079b992838eccae13964ea96a882&units=imperial";
		String openWeatherData = restTemplate.getForObject(openWeatherUrl, String.class);
		@SuppressWarnings("deprecation")
		JsonObject jsonObject = new JsonParser().parse(openWeatherData).getAsJsonObject();
		JsonArray weatherArrayData = ((JsonArray) jsonObject.get("weather"));
//		System.out.println(openWeatherData);
//		System.out.println(jsonObject);
		weatherDataForm.setLocation(location);
		Date sunriseTime = new Date(Long.parseLong(jsonObject.getAsJsonObject("sys").get("sunrise").toString()) * 1000);
		Date sunsetTime = new Date(Long.parseLong(jsonObject.getAsJsonObject("sys").get("sunset").toString()) * 1000);
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm a");
		weatherDataForm.setTemp(jsonObject.getAsJsonObject("main").get("temp").toString());
		weatherDataForm.setDescription((weatherArrayData.get(0)).getAsJsonObject().get("description").toString());
		String iconPath = "http://openweathermap.org/img/wn/"
				+ (weatherArrayData.get(0)).getAsJsonObject().get("icon").getAsString() + "@2x.png";
		weatherDataForm.setWeather_Icon(iconPath);
		weatherDataForm.setSunrisetime(sdf.format(sunriseTime));
		weatherDataForm.setSunsettime(sdf.format(sunsetTime));
		return weatherDataForm;
	}
}
