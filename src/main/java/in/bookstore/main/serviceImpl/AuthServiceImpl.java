package in.bookstore.main.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
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
import org.springframework.beans.factory.annotation.Value;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

	@Autowired
	private UserRepository repo;
	@Autowired
	private PasswordEncoder encoder;
	@Autowired
	private JwtUtil jwt;
	
	@Value("${OWNER_SECRET_KEY}")
    private String adminSecretKey;


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
	
	@Override
	public String registerAdmin(RegisterRequest req) {
	    if (adminSecretKey == null || req.getSecretKey() == null || !req.getSecretKey().equals(adminSecretKey)) {
	        throw new RuntimeException("Unauthorized: Invalid Admin Secret Key");
	    }

	    if(repo.findByEmail(req.getEmail()).isPresent())
	        throw new RuntimeException("Email already exists");

	    User user = new User();
	    user.setName(req.getName());
	    user.setEmail(req.getEmail());
	    user.setPassword(encoder.encode(req.getPassword()));
	    user.setRole(Role.ADMIN);

	    repo.save(user);
	    return "Admin Registered Successfully";
	}
	
	@Override
	public String adminLogin(LoginRequest req) {
	    User user = repo.findByEmail(req.getEmail())
	            .orElseThrow(() -> new RuntimeException("Admin account not found"));
	    if (!encoder.matches(req.getPassword(), user.getPassword())) {
	        throw new RuntimeException("Invalid Password");
	    }
	    if (!user.getRole().equals(Role.ADMIN)) {
	        throw new RuntimeException("Access Denied: You do not have Admin privileges.");
	    }
	    return jwt.generateToken(user.getEmail());
	}

}

