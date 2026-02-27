package in.bookstore.main.serviceImpl;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import in.bookstore.main.dto.LoginRequest;
import in.bookstore.main.dto.RegisterRequest;
import in.bookstore.main.entity.Role;
import in.bookstore.main.entity.User;
import in.bookstore.main.repository.UserRepository;
import in.bookstore.main.security.JwtUtil;
import in.bookstore.main.service.AuthService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

	private final UserRepository repo;
	private final PasswordEncoder encoder;
	private final JwtUtil jwt;

	public AuthServiceImpl(UserRepository repo, PasswordEncoder encoder, JwtUtil jwt){
		this.repo = repo;
		this.encoder = encoder;
		this.jwt = jwt;
	}


	@Override
	public String register(RegisterRequest req){

		if(repo.findByEmail(req.getEmail()).isPresent())
			throw new RuntimeException("Email already exists");

		User user = new User();
		user.setName(req.getName());
		user.setEmail(req.getEmail());
		user.setPassword(encoder.encode(req.getPassword()));
		user.setRole(Role.CUSTOMER);

		repo.save(user);

		return "User Registered Successfully";
	}

	@Override
	public String login(LoginRequest req){

		User user = repo.findByEmail(req.getEmail())
				.orElseThrow(() -> new RuntimeException("User not found"));

		if(!encoder.matches(req.getPassword(), user.getPassword()))
			throw new RuntimeException("Invalid Password");

		return jwt.generateToken(user.getEmail());
	}

}

