package com.in28minutes.springboot.web.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
	// Create User - in28Minutes/dummy
	@Autowired
	public void configureGlobalSecurity(AuthenticationManagerBuilder auth) throws Exception {
//        auth.inMemoryAuthentication().withUser("nikhil").password("hello")
//                .roles("USER", "ADMIN");
		// Solution 1 – Add password storage format, for plain text, add {noop}
		auth.inMemoryAuthentication().withUser("nikhil").password("{noop}hello").roles("USER").and().withUser("nik")
				.password("{noop}hello").roles("ADMIN");
	}

	/**
	 * This below method will give a default login page. permit any one to login
	 * page, but when they want to go to any todo page check for the user role and
	 * credentials
	 */
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests().antMatchers("/login").permitAll().antMatchers("/", "/*todo*/**")
				.access("hasRole('USER')").and().formLogin();
	}
}