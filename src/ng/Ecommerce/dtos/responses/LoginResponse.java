package ng.Ecommerce.dtos.responses;

import lombok.Data;

@Data
public class LoginResponse {
    private String name;
    private boolean isLoggedIn;
}
