package br.com.gusapi.config;

import br.com.gusapi.security.jwt.JwtConfigurer;
import br.com.gusapi.security.jwt.JwtTokenProvider;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import java.util.Map;

@Configuration
public class SecurityConfig {

	@Autowired
	private JwtTokenProvider tokenProvider;


	@Bean
	PasswordEncoder encoder() {
		Map<String, PasswordEncoder> encoders = new HashedMap();
		Pbkdf2PasswordEncoder pbkdf2PasswordEncoder = new Pbkdf2PasswordEncoder("", 16, 310000, Pbkdf2PasswordEncoder.SecretKeyFactoryAlgorithm.PBKDF2WithHmacSHA256);
		encoders.put("pbkdf2", pbkdf2PasswordEncoder);
		DelegatingPasswordEncoder passwordEncoder = new DelegatingPasswordEncoder("pbkdf2", encoders);
		passwordEncoder.setDefaultPasswordEncoderForMatches(pbkdf2PasswordEncoder);
		return passwordEncoder;
	}

	@Bean
	AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
		return authenticationConfiguration.getAuthenticationManager();
	}

	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		return http
			.httpBasic().disable()
			.csrf().disable()
			.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
			.authorizeHttpRequests(
				authorizeHttpRequests -> authorizeHttpRequests
					.requestMatchers("/auth/**", "/swagger-ui/**", "/v3/api-docs/**").permitAll()
					.requestMatchers("/api/v1/**").authenticated()
					.requestMatchers("/users").denyAll()
			)
			.cors()
			.and()
			.apply(new JwtConfigurer(tokenProvider))
			.and()
			.build();
	}
}
