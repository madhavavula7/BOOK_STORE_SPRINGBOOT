package in.bookstore.main.service;

import in.bookstore.main.dto.LoginRequest;
import in.bookstore.main.dto.RegisterRequest;

public interface AuthService {
	String register(RegisterRequest req);
	String login(LoginRequest req);
	public String registerAdmin(RegisterRequest req);
	public String adminLogin(LoginRequest req);
}
