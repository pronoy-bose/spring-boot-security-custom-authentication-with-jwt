package com.example.security;

import static com.example.security.SecurityConstants.SIGN_UP_URL;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

	@Autowired
	private CustomAuthenticationProvider authProvider;

	@Override
	protected void configure(HttpSecurity http) throws Exception {

		http.csrf().disable().authenticationProvider(authProvider).authorizeRequests()
				.antMatchers(HttpMethod.POST, SIGN_UP_URL).permitAll().anyRequest().authenticated().and()
				.addFilter(new JWTAuthenticationFilter(authenticationManager()))
				.addFilter(new JWTAuthorizationFilter(authenticationManager()))
				// this disables session creation on Spring Security
				.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
		        .logout().clearAuthentication(true);

	}

	@Autowired
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.authenticationProvider(authProvider);
	}
}
