//package in.bookstore.main.security;
//
//import java.util.Arrays;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.http.HttpMethod;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.web.SecurityFilterChain;
//import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
//import org.springframework.web.cors.CorsConfiguration;
//import org.springframework.web.cors.CorsConfigurationSource;
//import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
//
//import in.bookstore.main.controller.BookController;
//
//@Configuration
//@EnableWebSecurity
//public class SecurityConfig {
//
//	@SuppressWarnings("unused")
//	private final BookController bookController;
//
//
//	@Autowired
//	private JwtFilter jwtFilter;
//
//	SecurityConfig(BookController bookController) {
//		this.bookController = bookController;
//	}
//
//	@Bean 
//	SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//		http
//		.cors(cors -> cors.configurationSource(corsConfigurationSource()))
//		.csrf(csrf -> csrf.disable())
//		.authorizeHttpRequests(auth -> auth
//			    .requestMatchers("/api/auth/**").permitAll()
//			    .requestMatchers(HttpMethod.GET, "/api/books/**").permitAll()
//			    .requestMatchers(HttpMethod.POST, "/api/orders").hasAuthority("CUSTOMER")
//			    .requestMatchers(HttpMethod.GET, "/api/orders/my").hasAuthority("CUSTOMER")
//			    .requestMatchers(HttpMethod.GET, "/api/orders/**").hasAuthority("ADMIN")
//			    .requestMatchers(HttpMethod.PUT, "/api/orders/**").hasAuthority("ADMIN")
//			    .anyRequest().authenticated()
//			)
//		.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
//
//		return http.build();
//	}
//
//	@Bean
//	public CorsConfigurationSource corsConfigurationSource() {
//		CorsConfiguration configuration = new CorsConfiguration();
//		configuration.setAllowedOrigins(Arrays.asList("http://localhost:5173","https://boot-store-frontend.vercel.app"));
//		configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
//		configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type", "Accept"));
//		configuration.setAllowCredentials(true);
//
//		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//		source.registerCorsConfiguration("/**", configuration);
//		return source;
//	}
//
//	@Bean
//	PasswordEncoder encoder(){
//		return new BCryptPasswordEncoder();
//	}
//}
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

    private final BookController bookController;

    @Autowired
    private JwtFilter jwtFilter;

    public SecurityConfig(BookController bookController) {
        this.bookController = bookController;
    }

    @Bean 
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                // 1. ALWAYS permit OPTIONS requests for CORS preflight
                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                
                // 2. Public endpoints
                .requestMatchers("/api/auth/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/books/**").permitAll()
                
                // 3. Protected endpoints
                .requestMatchers(HttpMethod.POST, "/api/orders").hasAuthority("CUSTOMER")
                .requestMatchers(HttpMethod.GET, "/api/orders/my").hasAuthority("CUSTOMER")
                .requestMatchers(HttpMethod.GET, "/api/orders/**").hasAuthority("ADMIN")
                .requestMatchers(HttpMethod.PUT, "/api/orders/**").hasAuthority("ADMIN")
                
                // 4. Everything else needs a login
                .anyRequest().authenticated()
            )
            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        
        // Ensure this list exactly matches your frontend URLs
        configuration.setAllowedOrigins(Arrays.asList(
            "http://localhost:5173",
            "https://boot-store-frontend.vercel.app"
        ));
        
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type", "Accept", "X-Requested-With"));
        configuration.setAllowCredentials(true);
        configuration.setMaxAge(3600L); // Cache the CORS response for 1 hour

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public PasswordEncoder encoder(){
        return new BCryptPasswordEncoder();
    }
}