package ng.Ecommerce.data.models;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Document("orders")
public class  Order {
    @Id
    private String orderId;
    private String customerId;
    private LocalDateTime orderDate = LocalDateTime.now();
    private int quantity;
    private OrderStatus status =OrderStatus.PENDING;
    private List<OrderItem> items = new ArrayList<>();

}
