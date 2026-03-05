package in.bookstore.main.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import in.bookstore.main.dto.LoginRequest;
import in.bookstore.main.dto.RegisterRequest;
import in.bookstore.main.service.AuthService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AuthController {

	private final AuthService service;

	public AuthController(AuthService service) {
		super();
		this.service = service;
	}

	@PostMapping("/register")
	public ResponseEntity<?> register(@RequestBody RegisterRequest req){
		return ResponseEntity.ok(service.register(req));
	}

	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody LoginRequest req){
		return ResponseEntity.ok(service.login(req));
	}
	
	
}
