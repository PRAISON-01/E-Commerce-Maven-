package ng.Ecommerce.services;

import ng.Ecommerce.data.repositories.CustomerRepository;
import ng.Ecommerce.data.repositories.StoreKeeperRepository;
import ng.Ecommerce.dtos.requests.LoginRequest;
import ng.Ecommerce.dtos.requests.LogoutRequest;
import ng.Ecommerce.dtos.requests.RegisterRequest;
import ng.Ecommerce.dtos.responses.LoginResponse;
import ng.Ecommerce.dtos.responses.LogoutResponse;
import ng.Ecommerce.dtos.responses.RegisterResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
public class AuthServiceTest {

    @Autowired
    private AuthService authService;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private StoreKeeperRepository storeKeeperRepository;

    @BeforeEach
    void setUp() {
        customerRepository.deleteAll();
        storeKeeperRepository.deleteAll();
    }

    private RegisterRequest buildRegisterRequest(String name, String email, String password) {
        RegisterRequest request = new RegisterRequest();
        request.setName(name);
        request.setEmail(email);
        request.setPassword(password);
        return request;
    }

    @Test
    void registerCustomer_savesAndReturnsResponse_whenRequestIsValid() {
        RegisterRequest request = buildRegisterRequest("Jane Doe", "jane@example.com", "password123");

        RegisterResponse response = authService.registerCustomer(request);

        assertThat(response.getName()).isEqualTo("Jane Doe");
        assertThat(response.getEmail()).isEqualTo("jane@example.com");
        assertThat(customerRepository.findByEmail("jane@example.com")).isPresent();
    }

    @Test
    void registerCustomer_throws_whenNameIsMissing() {
        RegisterRequest request = buildRegisterRequest(null, "jane@example.com", "password123");

        assertThatThrownBy(() -> authService.registerCustomer(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Name is required");
    }

    @Test
    void registerCustomer_throws_whenEmailAlreadyExists() {
        RegisterRequest first = buildRegisterRequest("Jane Doe", "jane@example.com", "password123");
        authService.registerCustomer(first);

        RegisterRequest duplicate = buildRegisterRequest("Jane Copy", "jane@example.com", "otherPass");

        assertThatThrownBy(() -> authService.registerCustomer(duplicate))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Email already exists");
    }

    @Test
    void registerStoreKeeper_savesAndReturnsResponse_whenRequestIsValid() {
        RegisterRequest request = buildRegisterRequest("John Smith", "john@example.com", "securePass1");

        RegisterResponse response = authService.registerStoreKeeper(request);

        assertThat(response.getName()).isEqualTo("John Smith");
        assertThat(response.getEmail()).isEqualTo("john@example.com");
        assertThat(storeKeeperRepository.findByEmail("john@example.com")).isPresent();
    }

    @Test
    void registerStoreKeeper_throws_whenEmailAlreadyExists() {
        RegisterRequest first = buildRegisterRequest("John Smith", "john@example.com", "securePass1");
        authService.registerStoreKeeper(first);

        RegisterRequest duplicate = buildRegisterRequest("John Copy", "john@example.com", "otherPass");

        assertThatThrownBy(() -> authService.registerStoreKeeper(duplicate))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Email already exists");
    }

    @Test
    void loginCustomer_succeedsAndSetsLoggedInTrue_whenCredentialsAreCorrect() {
        authService.registerCustomer(buildRegisterRequest("Jane Doe", "jane@example.com", "password123"));

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("jane@example.com");
        loginRequest.setPassword("password123");

        LoginResponse response = authService.loginCustomer(loginRequest);

        assertThat(response.getName()).isEqualTo("Jane Doe");
        assertThat(response.isLoggedIn()).isTrue();
        assertThat(customerRepository.findByEmail("jane@example.com").get().isLoggedIn()).isTrue();
    }

    @Test
    void loginCustomer_throws_whenPasswordIsWrong() {
        authService.registerCustomer(buildRegisterRequest("Jane Doe", "jane@example.com", "password123"));

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("jane@example.com");
        loginRequest.setPassword("wrongPassword");

        assertThatThrownBy(() -> authService.loginCustomer(loginRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Invalid email or password");
    }

    @Test
    void loginCustomer_throws_whenEmailDoesNotExist() {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("nobody@example.com");
        loginRequest.setPassword("password123");

        assertThatThrownBy(() -> authService.loginCustomer(loginRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Invalid email or password");
    }

    @Test
    void loginStoreKeeper_succeedsAndSetsLoggedInTrue_whenCredentialsAreCorrect() {
        authService.registerStoreKeeper(buildRegisterRequest("John Smith", "john@example.com", "securePass1"));

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("john@example.com");
        loginRequest.setPassword("securePass1");

        LoginResponse response = authService.loginStoreKeeper(loginRequest);

        assertThat(response.getName()).isEqualTo("John Smith");
        assertThat(response.isLoggedIn()).isTrue();
        assertThat(storeKeeperRepository.findByEmail("john@example.com").get().isLoggedIn()).isTrue();
    }

    @Test
    void loginStoreKeeper_throws_whenPasswordIsWrong() {
        authService.registerStoreKeeper(buildRegisterRequest("John Smith", "john@example.com", "securePass1"));

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("john@example.com");
        loginRequest.setPassword("wrongPassword");

        assertThatThrownBy(() -> authService.loginStoreKeeper(loginRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Invalid email or password");
    }

    @Test
    void logoutCustomer_succeedsAndSetsLoggedInFalse() {
        authService.registerCustomer(buildRegisterRequest("Jane Doe", "jane@example.com", "password123"));
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("jane@example.com");
        loginRequest.setPassword("password123");
        authService.loginCustomer(loginRequest);

        LogoutRequest logoutRequest = new LogoutRequest();
        logoutRequest.setEmail("jane@example.com");

        LogoutResponse response = authService.logoutCustomer(logoutRequest);

        assertThat(response.getEmail()).isEqualTo("jane@example.com");
        assertThat(response.getMessage()).isEqualTo("Logout successful");
        assertThat(customerRepository.findByEmail("jane@example.com").get().isLoggedIn()).isFalse();
    }

    @Test
    void logoutCustomer_throws_whenCustomerNotFound() {
        LogoutRequest logoutRequest = new LogoutRequest();
        logoutRequest.setEmail("nobody@example.com");

        assertThatThrownBy(() -> authService.logoutCustomer(logoutRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Customer not found");
    }

    @Test
    void logoutStoreKeeper_succeedsAndSetsLoggedInFalse() {
        authService.registerStoreKeeper(buildRegisterRequest("John Smith", "john@example.com", "securePass1"));
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("john@example.com");
        loginRequest.setPassword("securePass1");
        authService.loginStoreKeeper(loginRequest);

        LogoutRequest logoutRequest = new LogoutRequest();
        logoutRequest.setEmail("john@example.com");

        LogoutResponse response = authService.logoutStoreKeeper(logoutRequest);

        assertThat(response.getEmail()).isEqualTo("john@example.com");
        assertThat(response.getMessage()).isEqualTo("Logout successful");
        assertThat(storeKeeperRepository.findByEmail("john@example.com").get().isLoggedIn()).isFalse();
    }

    @Test
    void logoutStoreKeeper_throws_whenStoreKeeperNotFound() {
        LogoutRequest logoutRequest = new LogoutRequest();
        logoutRequest.setEmail("nobody@example.com");

        assertThatThrownBy(() -> authService.logoutStoreKeeper(logoutRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Store keeper not found");
    }
}