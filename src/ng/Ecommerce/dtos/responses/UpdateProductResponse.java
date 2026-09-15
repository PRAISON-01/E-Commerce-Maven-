package ng.Ecommerce.dtos.responses;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class UpdateProductResponse {
    private String productId;
    private String name;
    private String description;
    private BigDecimal price;
    private int quantity;

    private String email;
    private String message;
}
