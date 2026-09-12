package ng.Ecommerce.dtos.requests;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class AddProductRequest {
    private String productId;
    private String name;
    private String description;
    private BigDecimal price;
    private int quantity;
}
