package com.sentura.bLow.configuration;

import com.sentura.bLow.constants.AppConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

	@Autowired
	private UserDetailsService jwtUserDetailsService;

	@Autowired
	private JwtRequestFilter jwtRequestFilter;

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		// configure AuthenticationManager so that it knows from where to load
		// user for matching credentials
		// Use BCryptPasswordEncoder
		auth.userDetailsService(jwtUserDetailsService).passwordEncoder(passwordEncoder());
	}

	@Bean
	public WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurer() {
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				// This wildcard pattern matches any host from domain.com and url patterns like "https:microservice.division.domain.com/version1/some_endpoint"
				registry.addMapping("/**")
						.allowedMethods("*")
						.allowedHeaders("*")
						.allowedOriginPatterns("*")
						.allowCredentials(false)
						.maxAge(3600);
			}
		};
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

	@Override
	protected void configure(HttpSecurity httpSecurity) throws Exception {
		// We don't need CSRF for this example
		httpSecurity.cors().and().csrf().disable()
				// dont authenticate this particular request
				.authorizeRequests()
                .antMatchers("/api/v1/auth/signIn").permitAll()
				.antMatchers("/api/v1/auth/signUp").permitAll()
				.antMatchers("/api/v1/auth/forgetPassword/**").permitAll()
				.antMatchers("/api/v1/auth/resetPassword").permitAll()
				.antMatchers("/api/v1/auth/verifyEmail").permitAll()
				.antMatchers("/api/v1/auth/resendVerifyEmail/**").permitAll()
				.antMatchers("/api/v1/auth/sendCustomEmail/**").permitAll()
				.antMatchers("/api/v1/auth/createUserByAdmin").hasAuthority(AppConstants.ADMIN)
				.antMatchers("/api/v1/auth/UpdateExpireDateAdminCreateUser").hasAuthority(AppConstants.ADMIN)
				.antMatchers("/api/v1/mailchimp/subscription").permitAll()
				.antMatchers("/api/v1/fileUploader/save").permitAll()
				.antMatchers("/api/v1/recipeCategory/save").hasAuthority(AppConstants.ADMIN)
				.antMatchers("/api/v1/recipeCategory/update").hasAuthority(AppConstants.ADMIN)
				.antMatchers("/api/v1/ingredient/save").hasAuthority(AppConstants.ADMIN)
				.antMatchers("/api/v1/ingredient/update").hasAuthority(AppConstants.ADMIN)
				.antMatchers("/api/v1/recipe/save").hasAuthority(AppConstants.ADMIN)
//				.antMatchers("/payment/api/v1/add-upi-payment").hasAuthority(AppConstants.USER)
				.antMatchers("/api/v1/package/get-all/**").permitAll()
				.antMatchers("/api/v1/package/create-checkout-session/**").permitAll()
				.antMatchers("/api/v1/package/get-trial").hasAuthority(AppConstants.ADMIN)
				.antMatchers("/api/v1/package/get-all-deleted/*").hasAuthority(AppConstants.ADMIN)
				.antMatchers("/api/v1/package/save").hasAuthority(AppConstants.ADMIN)
				.antMatchers("/api/v1/package/update-trial").hasAuthority(AppConstants.ADMIN)
				.antMatchers("/api/v1/package/get-users").hasAuthority(AppConstants.ADMIN)
				.antMatchers("/api/v1/package/delete").hasAuthority(AppConstants.ADMIN)
				.antMatchers("/api/v1/package/active").hasAuthority(AppConstants.ADMIN)
				.antMatchers("/api/v1/adminPanel/dashboardCardValues").hasAuthority(AppConstants.ADMIN)
				.antMatchers("/api/v1/adminPanel/weeklyRevenueAmount").hasAuthority(AppConstants.ADMIN)
				.antMatchers("/api/v1/recipe/deleteRecipe/{recipeId}").hasAuthority(AppConstants.ADMIN)
				.antMatchers("/files/**").permitAll()
				.antMatchers("/uploads/**").permitAll()
//				.antMatchers("/sample/greetings").hasAuthority(Roles.ADMIN_ROLE)
				// all other requests need to be authenticated
				.anyRequest().authenticated().and().
				// make sure we use stateless session; session won't be used to
				// store user's state.
				exceptionHandling().authenticationEntryPoint(jwtAuthenticationEntryPoint).and().sessionManagement()
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS);

		// Add a filter to validate the tokens with every request
		httpSecurity.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
	}
}