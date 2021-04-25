package com.danielbulger.workflow.spring.config;

import com.danielbulger.workflow.spring.auth.CustomUserDetailsService;
import com.danielbulger.workflow.spring.auth.oauth2.CustomOAuth2UserService;
import com.danielbulger.workflow.spring.auth.oauth2.HttpCookieOAuth2AuthorizationRequestRepository;
import com.danielbulger.workflow.spring.auth.oauth2.OAuth2AuthenticationFailureHandler;
import com.danielbulger.workflow.spring.auth.oauth2.OAuth2AuthenticationSuccessHandler;
import com.danielbulger.workflow.spring.auth.token.TokenAuthFilter;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled=true, jsr250Enabled=true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	private final TokenAuthFilter tokenAuthFilter;
	private final CustomUserDetailsService userDetailsService;
	private final CustomOAuth2UserService userService;
	private final HttpCookieOAuth2AuthorizationRequestRepository authorizationRequestRepository;
	private final OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler;
	private final OAuth2AuthenticationFailureHandler oAuth2AuthenticationFailureHandler;

	public WebSecurityConfig(
		TokenAuthFilter tokenAuthFilter,
		CustomUserDetailsService userDetailsService,
		CustomOAuth2UserService userService,
		HttpCookieOAuth2AuthorizationRequestRepository authorizationRequestRepository,
		OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler,
		OAuth2AuthenticationFailureHandler oAuth2AuthenticationFailureHandler
	) {
		this.tokenAuthFilter = tokenAuthFilter;
		this.userDetailsService = userDetailsService;
		this.userService = userService;
		this.authorizationRequestRepository = authorizationRequestRepository;
		this.oAuth2AuthenticationSuccessHandler = oAuth2AuthenticationSuccessHandler;
		this.oAuth2AuthenticationFailureHandler = oAuth2AuthenticationFailureHandler;
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService);
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		// @formatter:off
		http
			.csrf()
			.and()
			.sessionManagement()
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
				.and()
			.authorizeRequests()
				.antMatchers("/**")
					.fullyAuthenticated()
//				.antMatchers("/**")
//					.permitAll()
				.and()
			.formLogin()
				.disable()
			.httpBasic()
				.disable()
			.oauth2Login()
//				.loginPage("/")
				.authorizationEndpoint()
					.authorizationRequestRepository(authorizationRequestRepository)
					.and()
				.userInfoEndpoint()
				.userService(userService)
					.and()
				.successHandler(oAuth2AuthenticationSuccessHandler)
				.failureHandler(oAuth2AuthenticationFailureHandler);
		// @formatter:on

		http.addFilterBefore(tokenAuthFilter, UsernamePasswordAuthenticationFilter.class);
	}
}
