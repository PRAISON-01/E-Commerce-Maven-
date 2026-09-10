package ng.Ecommerce.dtos.responses;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class LogoutResponse {
    private String email;
    private String message;
}
