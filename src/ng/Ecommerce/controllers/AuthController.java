package ng.Ecommerce.controllers;

import ng.Ecommerce.dtos.requests.LoginRequest;
import ng.Ecommerce.dtos.requests.LogoutRequest;
import ng.Ecommerce.dtos.requests.RegisterRequest;
import ng.Ecommerce.dtos.responses.ApiResponse;
import ng.Ecommerce.dtos.responses.LoginResponse;
import ng.Ecommerce.dtos.responses.LogoutResponse;
import ng.Ecommerce.dtos.responses.RegisterResponse;
import ng.Ecommerce.services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.*;

@RestController
public class AuthController {
    @Autowired
    private AuthService authService;
    @PostMapping("/customer/register")
    public ResponseEntity<?> registerCustomer(@RequestBody RegisterRequest request){
        try{
            RegisterResponse response = authService.registerCustomer(request);
            return new ResponseEntity<>(new ApiResponse(true, response), CREATED);
        }
        catch(Exception e){
            return new ResponseEntity<>(new ApiResponse(false, e.getMessage()), BAD_REQUEST);
        }
    }


    @PostMapping("/storekeeper/register")
    public ResponseEntity<?> registerStoreKeeper(@RequestBody RegisterRequest request){
        try{
            RegisterResponse response = authService.registerStoreKeeper(request);
            return new ResponseEntity<>(new ApiResponse(true, response), CREATED);
        }
        catch(Exception e){
            return new ResponseEntity<>(new ApiResponse(false, e.getMessage()), BAD_REQUEST);
        }
    }

    @PostMapping("/customer/login")
    public ResponseEntity<?> loginCustomer(@RequestBody LoginRequest request) {
        try {
            LoginResponse response = authService.loginCustomer(request);
            return new ResponseEntity<>(new ApiResponse(true, response), OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new ApiResponse(false, e.getMessage()), BAD_REQUEST);
        }
    }

    @PostMapping("/storekeeper/login")
    public ResponseEntity<?> loginStoreKeeper(@RequestBody LoginRequest request) {
        try {
            LoginResponse response = authService.loginStoreKeeper(request);
            return new ResponseEntity<>(new ApiResponse(true, response), OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new ApiResponse(false, e.getMessage()), BAD_REQUEST);
        }
    }

    @PostMapping("/customer/logout")
    public ResponseEntity<?> logoutCustomer(@RequestBody LogoutRequest request) {
        try {
            LogoutResponse response = authService.logoutCustomer(request);
            return new ResponseEntity<>(new ApiResponse(true, response), OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new ApiResponse(false, e.getMessage()), BAD_REQUEST);
        }
    }

    @PostMapping("/storekeeper/logout")
    public ResponseEntity<?> logoutStoreKeeper(@RequestBody LogoutRequest request) {
        try {
            LogoutResponse response = authService.logoutStoreKeeper(request);
            return new ResponseEntity<>(new ApiResponse(true, response), OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new ApiResponse(false, e.getMessage()), BAD_REQUEST);
        }
    }
}
