package com.sentura.bLow.configuration;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

	@Autowired
	private JwtUserDetailsService jwtUserDetailsService;

	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	@Value("${jwt.tokenRefreshProcessEndDaysAfterTokenIssued}")
	private int tokenRefreshProcessEndDaysAfterTokenIssued;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws ServletException, IOException {

		final String requestTokenHeader = request.getHeader("Authorization");

		String username = null;
		String jwtToken = null;
		// JWT Token is in the form "Bearer token". Remove Bearer word and get only the Token
		if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
			jwtToken = requestTokenHeader.substring(7);
			try {
				username = jwtTokenUtil.getUsernameFromToken(jwtToken);
			} catch (IllegalArgumentException e) {
				System.out.println("Unable to get JWT Token");
			} catch (ExpiredJwtException e) {

				String isRefreshToken = request.getHeader("isRefreshToken");
				String requestURL = request.getRequestURL().toString();
				Date tokenRefreshProcessExpireDate = getTokenRefreshProcessExpireDate(jwtToken);

				// allow for Refresh Token creation if following conditions are true.
				if (isRefreshToken != null && isRefreshToken.equals("true") && requestURL.contains("refreshToken")) {
					try{
						if (new Date().compareTo(tokenRefreshProcessExpireDate) >= 0) {
							throw new ExpiredJwtException(e.getHeader(),e.getClaims(),"JWT Token has expired");
						} else {
							allowForRefreshToken(e, request);
						}
					}catch (ExpiredJwtException ex) {
						System.out.println("JWT Token has expired");
					}
				} else {
					request.setAttribute("exception", e);
					System.out.println("JWT Token has expired");
				}
			}
		} else {
			logger.warn("JWT Token does not begin with Bearer String");
		}

		//Once we get the token validate it.
		if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

			UserDetails userDetails = this.jwtUserDetailsService.loadUserByUsername(username);

			// if token is valid configure Spring Security to manually set authentication
			if (jwtTokenUtil.validateToken(jwtToken, userDetails)) {

				UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
						userDetails, null, userDetails.getAuthorities());
				usernamePasswordAuthenticationToken
						.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				// After setting the Authentication in the context, we specify
				// that the current user is authenticated. So it passes the Spring Security Configurations successfully.
				SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
			}
		}
		chain.doFilter(request, response);
	}

	private void allowForRefreshToken(ExpiredJwtException ex, HttpServletRequest request) {

		// create a UsernamePasswordAuthenticationToken with null values.
		UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
				null, null, null);
		// After setting the Authentication in the context, we specify
		// that the current user is authenticated. So it passes the
		// Spring Security Configurations successfully.
		SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
		// Set the claims so that in controller we will be using it to create
		// new JWT
		request.setAttribute("claims", ex.getClaims());

	}

	//set the defined date length to token issued date, to get the refresh process cancelling date
	private Date getTokenRefreshProcessExpireDate(String token) {

		String decodeToken = new String(Base64.getDecoder().decode(token.split("\\.")[1]), StandardCharsets.UTF_8);
		JsonObject json = new Gson().fromJson(decodeToken,JsonObject.class);
		int noOfDays = tokenRefreshProcessEndDaysAfterTokenIssued; //i.e two weeks
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date(json.get("iat").getAsLong()*1000));
		calendar.add(Calendar.DAY_OF_YEAR, noOfDays);
		return calendar.getTime();
	}

}