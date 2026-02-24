package in.bookstore.main.security;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import in.bookstore.main.controller.BookController;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	@SuppressWarnings("unused")
	private final BookController bookController;


	@Autowired
	private JwtFilter jwtFilter;

	SecurityConfig(BookController bookController) {
		this.bookController = bookController;
	}

	//    trying new 
	@Bean 
	SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http
		// This is the most important line to fix the UI error
		.cors(cors -> cors.configurationSource(corsConfigurationSource()))
		.csrf(csrf -> csrf.disable())
		.authorizeHttpRequests(auth -> auth
			    .requestMatchers("/api/auth/**").permitAll()
			    .requestMatchers(HttpMethod.GET, "/api/books/**").permitAll()
			    .requestMatchers(HttpMethod.POST, "/api/orders").hasAuthority("CUSTOMER")
			    .requestMatchers(HttpMethod.GET, "/api/orders/my").hasAuthority("CUSTOMER")
			    .requestMatchers(HttpMethod.GET, "/api/orders/**").hasAuthority("ADMIN")
			    .requestMatchers(HttpMethod.PUT, "/api/orders/**").hasAuthority("ADMIN")
			    .anyRequest().authenticated()
			)
		.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

		return http.build();
	}

	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration configuration = new CorsConfiguration();
		configuration.setAllowedOrigins(Arrays.asList("http://localhost:5173"));
		configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
		configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type", "Accept"));
		configuration.setAllowCredentials(true);

		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);
		return source;
	}

	@Bean
	PasswordEncoder encoder(){
		return new BCryptPasswordEncoder();
	}
}
