package in.bookstore.main.controller;

import org.springframework.beans.factory.annotation.Autowired;
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

	@Autowired
	private AuthService service;


//	Registers a new customer with the default USER role.
	@PostMapping("/register")
	public ResponseEntity<?> register(@RequestBody RegisterRequest req){
		return ResponseEntity.ok(service.register(req));
	}

//	 This end point handles both User and Admin login based on stored roles. 
	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody LoginRequest req){
		return ResponseEntity.ok(service.login(req));
	}
//	Requires a secondary verification (secret key) defined in application.properties.
	@PostMapping("/register-admin")
	public ResponseEntity<?> registerAdmin(@RequestBody RegisterRequest req){
	    return ResponseEntity.ok(service.registerAdmin(req));
	}
}
