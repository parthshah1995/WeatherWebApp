package com.weather.app.security;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	@Autowired
	private DataSource dataSource;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.exceptionHandling().accessDeniedPage("/403");
		http.authorizeRequests().antMatchers("/", "/login", "/registration").permitAll()
				.antMatchers("/resources/**", "/static/**").permitAll().antMatchers("/styles/**").permitAll()
				.antMatchers("/assets/**").permitAll().antMatchers("/home").hasAnyRole("USER").anyRequest()
				.authenticated().and().formLogin().failureUrl("/login?error").defaultSuccessUrl("/home")
				.loginPage("/login").permitAll().and().logout()
				.logoutRequestMatcher(new AntPathRequestMatcher("/logout")).logoutSuccessUrl("/login?Logout")
				.permitAll().invalidateHttpSession(true).deleteCookies("JSESSIONID").permitAll();
		;
	}

	@Override
	public void configure(AuthenticationManagerBuilder auth) throws Exception {

//		auth.inMemoryAuthentication().withUser("user").password(new BCryptPasswordEncoder().encode("user")).roles("USER");
		auth.jdbcAuthentication().dataSource(dataSource).passwordEncoder(passwordEncoder)
				.usersByUsernameQuery("select username,password,1 from public.user_profile where username=?")
				.authoritiesByUsernameQuery(
						"SELECT public.authority.username, public.authority.role from  public.authority  where public.authority.username = ?");
	}

	@Bean(name = "passwordEncoder")
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}