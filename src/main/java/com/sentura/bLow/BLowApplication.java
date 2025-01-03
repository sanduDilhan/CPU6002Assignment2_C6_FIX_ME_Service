package com.sentura.bLow;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.core.Ordered;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.http.CacheControl;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.PostConstruct;
import java.io.File;
import java.util.Collections;
import java.util.concurrent.TimeUnit;

@SpringBootApplication
@EnableJpaRepositories(basePackages = { "com.sentura.bLow.*" })
public class BLowApplication implements WebMvcConfigurer {

	@Value("${archive.path}")
	private String archivePath;

	public static void main(String[] args) {
		SpringApplication.run(BLowApplication.class, args);
	}

	@PostConstruct
	private void createArchiveIfNotExists() {

		File directory = new File(archivePath);
		if (! directory.exists()){
			directory.mkdir();
		}
	}

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {

		// Register resource handler for images
		registry.addResourceHandler("/files/**").addResourceLocations("file:///"+archivePath+"/")
				.setCacheControl(CacheControl.maxAge(24, TimeUnit.HOURS).cachePublic());
	}

	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}

}
