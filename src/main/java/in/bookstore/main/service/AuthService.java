package in.bookstore.main.service;

import in.bookstore.main.dto.ApiResponse;
import in.bookstore.main.dto.AuthResponse;
import in.bookstore.main.dto.LoginRequest;
import in.bookstore.main.dto.RegisterRequest;

public interface AuthService {

    String register(RegisterRequest req);
    String registerAdmin(RegisterRequest req);


    ApiResponse<AuthResponse> login(LoginRequest req);
    ApiResponse<AuthResponse> adminLogin(LoginRequest req);
}
