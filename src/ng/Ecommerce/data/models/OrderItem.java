package ng.Ecommerce.data.models;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;

@Data
public class OrderItem {
    private String productId;
    private BigDecimal pricePurchased;
    private int quantity;
    private BigDecimal subtotal;

}
