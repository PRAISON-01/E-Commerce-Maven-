package ng.Ecommerce.data.models;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document
public class StoreKeeper {
    @Id
    private String id;
    private String name;
    private String email;
    private String password;
    private boolean isLoggedIn;
}
