package ng.Ecommerce.dtos.requests;

import lombok.Data;

@Data
public class DeleteProductRequest {
    private String productId;
    private String storekeeperEmail;
}
