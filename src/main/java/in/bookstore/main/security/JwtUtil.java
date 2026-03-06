package in.bookstore.main.security;

import java.sql.Date;

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

	public String generateToken(String email){
        long now = System.currentTimeMillis();
        long expiry = now + (1000L * 60 * 60 * 24);

        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date(now))       // When it was created
                .setExpiration(new Date(expiry))  // When it expires
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