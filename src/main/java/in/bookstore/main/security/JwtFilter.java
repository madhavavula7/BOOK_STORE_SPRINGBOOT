package in.bookstore.main.security;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import in.bookstore.main.entity.User;
import in.bookstore.main.repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

	@Autowired
	private JwtUtil jwtUtil;
	@Autowired
	private UserRepository repo;


	@Override
	protected void doFilterInternal(HttpServletRequest request,
	        HttpServletResponse response,
	        FilterChain filterChain)
	                throws ServletException, IOException {

	    String header = request.getHeader("Authorization");

	    if(header == null || !header.startsWith("Bearer ")){
	        filterChain.doFilter(request,response);
	        return;
	    }

	    String token = header.substring(7);
	    String email = jwtUtil.extractEmail(token);

	 // Inside your JwtFilter doFilterInternal
	    if(email != null && SecurityContextHolder.getContext().getAuthentication() == null){
	        User user = repo.findByEmail(email).orElseThrow();

	        // GET the role name and ADD the "ROLE_" prefix
	        // This is the industry standard for Spring Security
	        String userRole = "ROLE_" + user.getRole().name(); 

	        UsernamePasswordAuthenticationToken auth =
	                new UsernamePasswordAuthenticationToken(
	                        user.getEmail(),
	                        null,
	                        // Now this works for BOTH hasRole() and hasAuthority()
	                        List.of(new SimpleGrantedAuthority(userRole)) 
	                );

	        SecurityContextHolder.getContext().setAuthentication(auth);
	    }

	    filterChain.doFilter(request,response);
	}
}

