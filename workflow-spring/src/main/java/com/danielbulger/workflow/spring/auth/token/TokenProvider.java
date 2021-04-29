package com.danielbulger.workflow.spring.auth.token;

import com.danielbulger.workflow.spring.auth.UserPrincipal;
import com.danielbulger.workflow.spring.config.TokenConfig;
import com.danielbulger.workflow.spring.model.http.Cookies;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.time.Duration;
import java.util.Date;

@Service
public class TokenProvider {

	private static final Logger LOG = LoggerFactory.getLogger(TokenProvider.class);

	private final TokenConfig tokenConfig;

	public TokenProvider(TokenConfig tokenConfig) {
		this.tokenConfig = tokenConfig;
	}

	public String createToken(Authentication authentication) {
		final UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();

		final Date now = new Date();
		final Date expiration = new Date(now.getTime() + tokenConfig.getExpiration());

		LOG.debug("Creating token now={},expires={}", now, expiration);

		return Jwts.builder()
			.setSubject(userPrincipal.getEmail())
			.setIssuedAt(new Date())
			.setExpiration(expiration)
			.signWith(SignatureAlgorithm.HS512, tokenConfig.getSecret())
			.compact();
	}

	public String getEmailFromToken(String token) {
		return Jwts.parser()
			.setSigningKey(tokenConfig.getSecret())
			.parseClaimsJws(token)
			.getBody()
			.getSubject();
	}

	public boolean validateToken(String token) {
		try {
			Jwts.parser()
				.setSigningKey(tokenConfig.getSecret())
				.parseClaimsJws(token);
			return true;
		} catch (Exception ex) {
			LOG.debug("Unable to validate token", ex);
			return false;
		}
	}

	public void createCookie(HttpServletResponse response, String token) {
		Cookies.add(
			response,
			tokenConfig.getCookie(),
			token,
			(int) Duration.ofMillis(tokenConfig.getExpiration()).toSeconds()
		);
	}
}
