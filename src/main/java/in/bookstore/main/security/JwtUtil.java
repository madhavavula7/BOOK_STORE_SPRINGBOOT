package in.bookstore.main.security;

import java.sql.Date;
import java.util.concurrent.TimeUnit;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtil {

	@Value("${jwt.secret}")
	private String secret;

	private SecretKey getKey(){
		return Keys.hmacShaKeyFor(secret.getBytes());
	}

	public String generateToken(String email) {
	    long now = System.currentTimeMillis();
	    
	    // Setting expiry for 24 hours (24 * 60 * 60 * 1000)
	    // Using TimeUnit makes the code much easier to read and less prone to math errors
	    long expiry = now + TimeUnit.HOURS.toMillis(24);

	    return Jwts.builder()
	            .setSubject(email)
	            .setIssuedAt(new Date(now))       
	            .setExpiration(new Date(expiry))  
	            .signWith(getKey())
	            .compact();
	}

	public String extractEmail(String token){
		return Jwts.parserBuilder()
				.setSigningKey(getKey())
				.build()
				.parseClaimsJws(token)
				.getBody()
				.getSubject();
	}
	private String generateInvoiceNumber(Long orderId) {
	    int year = java.time.LocalDate.now().getYear();
	    // Formats to: INV-2026-00001
	    return String.format("INV-%d-%05d", year, orderId);
	}
}