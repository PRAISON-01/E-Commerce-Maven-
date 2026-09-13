package ng.Ecommerce.services;

import ng.Ecommerce.data.models.Product;
import ng.Ecommerce.data.models.StoreKeeper;
import ng.Ecommerce.data.repositories.ProductRepository;
import ng.Ecommerce.data.repositories.StoreKeeperRepository;
import ng.Ecommerce.dtos.requests.AddProductRequest;
import ng.Ecommerce.dtos.responses.AddProductResponse;
import ng.Ecommerce.exceptions.InvalidProductDataException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.Optional;

import static com.mongodb.assertions.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
public class InventoryServiceTest {

    @InjectMocks
    private InventoryServiceImpl inventoryService;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private StoreKeeperRepository storeKeeperRepository;

    private AddProductRequest addProductRequest;

    @BeforeEach
    public void startWithThis() {
        addProductRequest = new AddProductRequest();
    }

    @AfterEach
    public void tearDown() {
        productRepository.deleteAll();
        storeKeeperRepository.deleteAll();
    }
    private static Product createNewProduct() {

        Product test_product = new Product();

        test_product.setProductId("1");
        test_product.setName("test_product");
        test_product.setPrice(new BigDecimal("1400"));
        test_product.setDescription("test_description");
        test_product.setQuantity(20);

        return test_product;
    }

    private static StoreKeeper createNewStoreKeeper() {
        StoreKeeper storeKeeper = new StoreKeeper();

        storeKeeper.setName("test_name");
        storeKeeper.setEmail("test@gmail.com");
        storeKeeper.setPassword("test_password");
        storeKeeper.setId("1");
        storeKeeper.setLoggedIn(true);

        return storeKeeper;
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

        Product test_product = createNewProduct();

        addProductRequest.setProductId(test_product.getProductId());
        addProductRequest.setName(test_product.getName());
        addProductRequest.setDescription(test_product.getDescription());
        addProductRequest.setPrice(test_product.getPrice());
        addProductRequest.setQuantity(test_product.getQuantity());
        addProductRequest.setStorekeeperEmail("test@gmail.com");


        Mockito.when(storeKeeperRepository.findByEmail("test@gmail.com")).thenReturn(Optional.empty());
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                ()-> inventoryService.addProduct(addProductRequest)
        );

        assertEquals("User Not Found", exception.getMessage());
    }

    @Test
    public void userNotRegistered_addProduct_userNotLoggedIn() {

        StoreKeeper test_storeKeeper = createNewStoreKeeper();
        test_storeKeeper.setLoggedIn(false);

        Product test_product = createNewProduct();

        addProductRequest.setProductId(test_product.getProductId());
        addProductRequest.setName(test_product.getName());
        addProductRequest.setDescription(test_product.getDescription());
        addProductRequest.setPrice(test_product.getPrice());
        addProductRequest.setQuantity(test_product.getQuantity());
        addProductRequest.setStorekeeperEmail("test@gmail.com");


        Mockito.when(storeKeeperRepository.findByEmail("test@gmail.com")).thenReturn(Optional.of(test_storeKeeper));

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                ()-> inventoryService.addProduct(addProductRequest)
        );

        assertEquals(test_storeKeeper.getName() + " Not Logged In", exception.getMessage());
    }


    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"tes", " "})
    public void userNotRegistered_addProduct_InvalidProductName(String name) {

        StoreKeeper test_storeKeeper = createNewStoreKeeper();

        Product test_product = createNewProduct();

        addProductRequest.setProductId(test_product.getProductId());
        addProductRequest.setName(name);
        addProductRequest.setDescription(test_product.getDescription());
        addProductRequest.setPrice(test_product.getPrice());
        addProductRequest.setQuantity(test_product.getQuantity());
        addProductRequest.setStorekeeperEmail("test@gmail.com");


        Mockito.when(storeKeeperRepository.findByEmail("test@gmail.com")).thenReturn(Optional.of(test_storeKeeper));

        InvalidProductDataException exception = assertThrows(
                InvalidProductDataException.class,
                ()-> inventoryService.addProduct(addProductRequest)
        );

        assertEquals("Invalid Product Name", exception.getMessage());
    }



    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"tes", " "})
    public void userNotRegistered_addProduct_InvalidProductDescription(String description) {

        StoreKeeper test_storeKeeper = createNewStoreKeeper();

        Product test_product = createNewProduct();

        addProductRequest.setProductId(test_product.getProductId());
        addProductRequest.setName(test_product.getName());
        addProductRequest.setDescription(description);
        addProductRequest.setPrice(test_product.getPrice());
        addProductRequest.setQuantity(test_product.getQuantity());
        addProductRequest.setStorekeeperEmail("test@gmail.com");


        Mockito.when(storeKeeperRepository.findByEmail("test@gmail.com")).thenReturn(Optional.of(test_storeKeeper));

        InvalidProductDataException exception = assertThrows(
                InvalidProductDataException.class,
                ()-> inventoryService.addProduct(addProductRequest)
        );

        assertEquals("Invalid Product Description", exception.getMessage());
    }


    @ParameterizedTest
    @NullSource
    @CsvSource({"-10.00", "-1.55", "-0.01", "0"})
    public void userNotRegistered_addProduct_InvalidProductPrice(BigDecimal price) {

        StoreKeeper test_storeKeeper = createNewStoreKeeper();

        Product test_product = createNewProduct();

        addProductRequest.setProductId(test_product.getProductId());
        addProductRequest.setName(test_product.getName());
        addProductRequest.setDescription(test_product.getDescription());
        addProductRequest.setPrice(price);
        addProductRequest.setQuantity(test_product.getQuantity());
        addProductRequest.setStorekeeperEmail("test@gmail.com");


        Mockito.when(storeKeeperRepository.findByEmail("test@gmail.com")).thenReturn(Optional.of(test_storeKeeper));

        InvalidProductDataException exception = assertThrows(
                InvalidProductDataException.class,
                ()-> inventoryService.addProduct(addProductRequest)
        );

        assertEquals("Invalid Product Price", exception.getMessage());
    }


    @ParameterizedTest
    @ValueSource(ints = {-10, 0})
    public void userNotRegistered_addProduct_InvalidProductQuantity(int quantity ) {

        StoreKeeper test_storeKeeper = createNewStoreKeeper();

        Product test_product = createNewProduct();

        addProductRequest.setProductId(test_product.getProductId());
        addProductRequest.setName(test_product.getName());
        addProductRequest.setDescription(test_product.getDescription());
        addProductRequest.setPrice(test_product.getPrice());
        addProductRequest.setQuantity(quantity);
        addProductRequest.setStorekeeperEmail("test@gmail.com");


        Mockito.when(storeKeeperRepository.findByEmail("test@gmail.com")).thenReturn(Optional.of(test_storeKeeper));

        InvalidProductDataException exception = assertThrows(
                InvalidProductDataException.class,
                ()-> inventoryService.addProduct(addProductRequest)
        );

        assertEquals("Invalid Product Quantity", exception.getMessage());
    }

    @ParameterizedTest
    @ValueSource(strings = {"test_name",  "teST_NaMe", "    test_name      "})
    public void userNotRegistered_addProduct_ValidName(String name) {

        StoreKeeper test_storeKeeper = createNewStoreKeeper();

        Product test_product = createNewProduct();

        addProductRequest.setProductId(test_product.getProductId());
        addProductRequest.setName(name);
        addProductRequest.setDescription(test_product.getDescription());
        addProductRequest.setPrice(test_product.getPrice());
        addProductRequest.setQuantity(test_product.getQuantity());
        addProductRequest.setStorekeeperEmail("test@gmail.com");


        Mockito.when(storeKeeperRepository.findByEmail("test@gmail.com")).thenReturn(Optional.of(test_storeKeeper));


        Mockito.when(productRepository.save(any(Product.class))).thenReturn(test_product);

        AddProductResponse addProductResponse =  inventoryService.addProduct(addProductRequest);

        assertNotNull(addProductResponse);
        assertEquals("test_product", addProductResponse.getName());

        Mockito.verify(storeKeeperRepository, Mockito.times(1) ).findByEmail("test@gmail.com");
        Mockito.verify(productRepository, Mockito.times(1)).save(any(Product.class));
    }

    @ParameterizedTest
    @ValueSource(strings = {"test_description",  "teST_deSCriPtIon", "    test_description      "})
    public void userRegistered_addProduct_ValidDescription(String description) {

        StoreKeeper test_storeKeeper = createNewStoreKeeper();

        Product test_product = createNewProduct();

        addProductRequest.setProductId(test_product.getProductId());
        addProductRequest.setName(test_product.getName());
        addProductRequest.setDescription(description);
        addProductRequest.setPrice(test_product.getPrice());
        addProductRequest.setQuantity(test_product.getQuantity());
        addProductRequest.setStorekeeperEmail("test@gmail.com");


        Mockito.when(storeKeeperRepository.findByEmail("test@gmail.com")).thenReturn(Optional.of(test_storeKeeper));


        Mockito.when(productRepository.save(any(Product.class))).thenReturn(test_product);

        AddProductResponse addProductResponse =  inventoryService.addProduct(addProductRequest);

        assertNotNull(addProductResponse);
        assertEquals("test_description", addProductResponse.getDescription());

        Mockito.verify(storeKeeperRepository, Mockito.times(1) ).findByEmail("test@gmail.com");
        Mockito.verify(productRepository, Mockito.times(1)).save(any(Product.class));
    }

//    Update Product service


    @Test
    public void updateProduct_request_cannotBeNull() {

        InvalidProductDataException exception = assertThrows(
                InvalidProductDataException.class,
                ()-> inventoryService.addProduct(null)
        );

        assertEquals("Invalid input", exception.getMessage());
    }



}
