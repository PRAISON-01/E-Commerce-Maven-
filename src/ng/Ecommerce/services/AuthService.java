package ng.Ecommerce.services;

import ng.Ecommerce.dtos.requests.LoginRequest;
import ng.Ecommerce.dtos.requests.LogoutRequest;
import ng.Ecommerce.dtos.requests.RegisterRequest;
import ng.Ecommerce.dtos.responses.LoginResponse;
import ng.Ecommerce.dtos.responses.LogoutResponse;
import ng.Ecommerce.dtos.responses.RegisterResponse;

public interface AuthService {
    RegisterResponse registerCustomer(RegisterRequest request);
    RegisterResponse registerStoreKeeper(RegisterRequest request);
    LoginResponse loginCustomer(LoginRequest request);
    LoginResponse loginStoreKeeper(LoginRequest request);
    LogoutResponse logoutCustomer(LogoutRequest request);
    LogoutResponse logoutStoreKeeper(LogoutRequest request);
}