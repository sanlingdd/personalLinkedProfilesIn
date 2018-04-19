package com.falcon.configuration;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

	@Autowired
	DataSource dataSource;

	@Autowired
	UserDetailsService userDetailsService;

	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers("/resources/**"); 
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {

		//http.csrf().disable().authorizeRequests().antMatchers("/js", "/css", "/images", "/fonts").permitAll();

		http.csrf().disable().authorizeRequests().antMatchers("/", "/home", "/about", "/index","/js/**", "/css/**", "/images/**", "/fonts/**").permitAll()
				.antMatchers("/admin/**").hasAnyRole("ROLE_ADMIN").antMatchers("/user/**").hasAnyRole("ROLE_USER")
				.anyRequest().authenticated().and().formLogin().loginPage("/login").permitAll().and().logout()
				.permitAll().and().exceptionHandling().accessDeniedPage("/404");

		/*
		 * http.authorizeRequests().antMatchers("/abc").access(
		 * "hasRole('ROLE_ADMIN')").anyRequest().permitAll().and()
		 * .formLogin().loginPage("/login").usernameParameter("username").
		 * passwordParameter("password").and()
		 * .logout().logoutSuccessUrl("/login?logout").and().exceptionHandling()
		 * .accessDeniedPage("/403").and() .csrf();
		 */
	}

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService).passwordEncoder(passwordencoder());
	}

	@Bean(name = "passwordEncoder")
	public PasswordEncoder passwordencoder() {
		return new BCryptPasswordEncoder();
	}

}