package ng.Ecommerce.services;

import ng.Ecommerce.data.models.Customer;
import ng.Ecommerce.data.models.StoreKeeper;
import ng.Ecommerce.data.repositories.CustomerRepository;
import ng.Ecommerce.data.repositories.StoreKeeperRepository;
import ng.Ecommerce.dtos.requests.LoginRequest;
import ng.Ecommerce.dtos.requests.LogoutRequest;
import ng.Ecommerce.dtos.requests.RegisterRequest;
import ng.Ecommerce.dtos.responses.LoginResponse;
import ng.Ecommerce.dtos.responses.LogoutResponse;
import ng.Ecommerce.dtos.responses.RegisterResponse;
import ng.Ecommerce.utils.Mapper;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    private final CustomerRepository customerRepository;
    private final StoreKeeperRepository storeKeeperRepository;

    public AuthServiceImpl(
            CustomerRepository customerRepository,
            StoreKeeperRepository storeKeeperRepository
    ) {
        this.customerRepository = customerRepository;
        this.storeKeeperRepository = storeKeeperRepository;
    }

    @Override
    public RegisterResponse registerCustomer(RegisterRequest request) {
        if (request.getName() == null || request.getName().isBlank()) {
            throw new IllegalArgumentException("Name is required");
        }
        if (request.getEmail() == null || request.getEmail().isBlank()) {
            throw new IllegalArgumentException("Email is required");
        }
        if (request.getPassword() == null || request.getPassword().isBlank()) {
            throw new IllegalArgumentException("Password is required");
        }

        if (customerRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email already exists");
        }

        Customer customer = Mapper.mapToCustomer(request);
        Customer savedCustomer = customerRepository.save(customer);
        return Mapper.mapToRegisterResponse(savedCustomer);
    }

    @Override
    public RegisterResponse registerStoreKeeper(RegisterRequest request) {
        if (request.getName() == null || request.getName().isBlank()) {
            throw new IllegalArgumentException("Name is required");
        }
        if (request.getEmail() == null || request.getEmail().isBlank()) {
            throw new IllegalArgumentException("Email is required");
        }
        if (request.getPassword() == null || request.getPassword().isBlank()) {
            throw new IllegalArgumentException("Password is required");
        }

        if (storeKeeperRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email already exists");
        }

        StoreKeeper storeKeeper = Mapper.mapToStoreKeeper(request);
        StoreKeeper savedStoreKeeper = storeKeeperRepository.save(storeKeeper);
        return Mapper.mapToRegisterResponse(savedStoreKeeper);
    }

    @Override
    public LoginResponse loginCustomer(LoginRequest request) {
        if (request.getEmail() == null || request.getEmail().isBlank()) {
            throw new IllegalArgumentException("Email is required");
        }
        if (request.getPassword() == null || request.getPassword().isBlank()) {
            throw new IllegalArgumentException("Password is required");
        }

        Customer customer = customerRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("Invalid email or password"));

        if (!customer.getPassword().equals(request.getPassword())) {
            throw new IllegalArgumentException("Invalid email or password");
        }

        customer.setLoggedIn(true);
        Customer savedCustomer = customerRepository.save(customer);
        return Mapper.mapToLoginResponse(savedCustomer);
    }

    @Override
    public LoginResponse loginStoreKeeper(LoginRequest request) {
        if (request.getEmail() == null || request.getEmail().isBlank()) {
            throw new IllegalArgumentException("Email is required");
        }
        if (request.getPassword() == null || request.getPassword().isBlank()) {
            throw new IllegalArgumentException("Password is required");
        }

        StoreKeeper storeKeeper = storeKeeperRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("Invalid email or password"));

        if (!storeKeeper.getPassword().equals(request.getPassword())) {
            throw new IllegalArgumentException("Invalid email or password");
        }

        storeKeeper.setLoggedIn(true);
        StoreKeeper savedStoreKeeper = storeKeeperRepository.save(storeKeeper);
        return Mapper.mapToLoginResponse(savedStoreKeeper);
    }

    @Override
    public LogoutResponse logoutCustomer(LogoutRequest request) {
        if (request.getEmail() == null || request.getEmail().isBlank()) {
            throw new IllegalArgumentException("Email is required");
        }

        Customer customer = customerRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("Customer not found"));

        customer.setLoggedIn(false);
        Customer savedCustomer = customerRepository.save(customer);
        return Mapper.mapToLogoutResponse(savedCustomer);
    }

    @Override
    public LogoutResponse logoutStoreKeeper(LogoutRequest request) {
        if (request.getEmail() == null || request.getEmail().isBlank()) {
            throw new IllegalArgumentException("Email is required");
        }

        StoreKeeper storeKeeper = storeKeeperRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("Store keeper not found"));

        storeKeeper.setLoggedIn(false);
        StoreKeeper savedStoreKeeper = storeKeeperRepository.save(storeKeeper);
        return Mapper.mapToLogoutResponse(savedStoreKeeper);
    }
}