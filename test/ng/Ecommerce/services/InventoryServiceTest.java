package ng.Ecommerce.services;

import ng.Ecommerce.data.models.Product;
import ng.Ecommerce.data.models.StoreKeeper;
import ng.Ecommerce.data.repositories.ProductRepository;
import ng.Ecommerce.dtos.requests.AddProductRequest;
import ng.Ecommerce.exceptions.InvalidProductDataException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class InventoryServiceTest {

    @Autowired
    private InventoryService inventoryService;


    @Autowired
    private ProductRepository productRepository;

    private AddProductRequest addProductRequest;

    @BeforeEach
    public void startWithThis() {
        inventoryService = new InventoryServiceImpl();
        addProductRequest = new AddProductRequest();


    }

    @AfterEach
    public void tearDown() {
        productRepository.deleteAll();
    }
    private static Product create_new_product() {
        Product test_product = new Product();

        test_product.setProductId("1");
        test_product.setName("test_product");
        test_product.setPrice(new BigDecimal("1400"));
        test_product.setDescription("test_description");
        test_product.setQuantity(20);

        return test_product;
    }

    @Test
    public void addProduct_request_cannotBeNull() {
        InvalidProductDataException exception = assertThrows(
                InvalidProductDataException.class,
                ()-> inventoryService.addProduct(null)
        );

        assertEquals("Invalid input", exception.getMessage());
    }


    @Test
    public void userNotRegistered_addProduct_userNOtFound() {
        Product test_product = create_new_product();

        addProductRequest.setProductId(test_product.getProductId());
        addProductRequest.setName(test_product.getName());
        addProductRequest.setDescription(test_product.getDescription());
        addProductRequest.setPrice(test_product.getPrice());
        addProductRequest.setQuantity(test_product.getQuantity());


        InvalidProductDataException exception = assertThrows(
                InvalidProductDataException.class,
                ()-> inventoryService.addProduct(addProductRequest)
        );

        assertEquals("Invalid input", exception.getMessage());
    }
}
