package com.danielbulger.workflow.spring.auth.token;

import com.danielbulger.workflow.spring.auth.CustomUserDetailsService;
import com.danielbulger.workflow.spring.config.TokenConfig;
import com.danielbulger.workflow.spring.http.Cookies;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class TokenAuthFilter extends OncePerRequestFilter {

	private static final Logger LOG = LoggerFactory.getLogger(TokenAuthFilter.class);

	private final TokenConfig tokenConfig;
	private final TokenProvider tokenProvider;
	private final CustomUserDetailsService userDetailsService;

	public TokenAuthFilter(
		TokenConfig tokenConfig, TokenProvider tokenProvider, CustomUserDetailsService userDetailsService
	) {
		this.tokenConfig = tokenConfig;
		this.tokenProvider = tokenProvider;
		this.userDetailsService = userDetailsService;
	}

	@Override
	protected void doFilterInternal(
		HttpServletRequest request,
		HttpServletResponse response,
		FilterChain filterChain
	) throws ServletException, IOException {
		try {
			final String jwt = getJwtFromRequest(request, response);

			if (tokenProvider.validateToken(jwt)) {
				final String email = tokenProvider.getEmailFromToken(jwt);
				final UserDetails userDetails = userDetailsService.loadUserByUsername(email);
				final UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
					userDetails, null, userDetails.getAuthorities()
				);
				authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				SecurityContextHolder.getContext().setAuthentication(authenticationToken);

				LOG.debug("JWT parsed for {}", email);
			}
		} catch (IllegalArgumentException ex) {
			LOG.debug("No JWT token found");
		} catch (Exception ex) {
			LOG.error("Unable to parse JWT token", ex);
		}
		filterChain.doFilter(request, response);
	}

	private String getJwtFromRequest(
		HttpServletRequest request, HttpServletResponse response
	) throws IllegalArgumentException {
		final Cookie cookie = Cookies.get(request, tokenConfig.getCookie())
			.orElseThrow(IllegalArgumentException::new);

		return cookie.getValue();
	}
}
