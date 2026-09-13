package ng.Ecommerce.services;

import ng.Ecommerce.data.models.Product;
import ng.Ecommerce.data.models.StoreKeeper;
import ng.Ecommerce.data.repositories.ProductRepository;
import ng.Ecommerce.data.repositories.StoreKeeperRepository;
import ng.Ecommerce.dtos.requests.AddProductRequest;
import ng.Ecommerce.dtos.requests.DeleteProductRequest;
import ng.Ecommerce.dtos.requests.UpdateProductRequest;
import ng.Ecommerce.dtos.responses.AddProductResponse;
import ng.Ecommerce.dtos.responses.DeleteProductResponse;
import ng.Ecommerce.dtos.responses.UpdateProductResponse;
import ng.Ecommerce.exceptions.InvalidProductDataException;
import ng.Ecommerce.exceptions.ProductNotFoundException;
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

    private UpdateProductRequest updateProductRequest;

    @BeforeEach
    public void startWithThis() {
        addProductRequest = new AddProductRequest();
        updateProductRequest = new UpdateProductRequest();
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
    public void UserRegisteredAndLoggedIn_addProduct_userNOtFound() {

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
    public void UserRegisteredAndLoggedIn_addProduct_userNotLoggedIn() {

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
    public void UserRegisteredAndLoggedIn_addProduct_InvalidProductName(String name) {

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
    public void UserRegisteredAndLoggedIn_addProduct_InvalidProductDescription(String description) {

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
    public void UserRegisteredAndLoggedIn_addProduct_InvalidProductPrice(BigDecimal price) {

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
    public void UserRegisteredAndLoggedIn_addProduct_InvalidProductQuantity(int quantity ) {

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
    public void UserRegisteredAndLoggedIn_addProduct_ValidName(String name) {

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
                ()-> inventoryService.updateProduct(null)
        );

        assertEquals("Invalid input", exception.getMessage());
    }

    @Test
    public void addProduct_updateProduct_userNotRegistered() {
        StoreKeeper test_storeKeeper = createNewStoreKeeper();

        Product test_product = createNewProduct();

        addProductRequest.setProductId(test_product.getProductId());
        addProductRequest.setName(test_product.getName());
        addProductRequest.setDescription(test_product.getDescription());
        addProductRequest.setPrice(test_product.getPrice());
        addProductRequest.setQuantity(test_product.getQuantity());
        addProductRequest.setStorekeeperEmail("test@gmail.com");


        Mockito.when(storeKeeperRepository.findByEmail("test@gmail.com")).thenReturn(Optional.of(test_storeKeeper));


        Mockito.when(productRepository.save(any(Product.class))).thenReturn(test_product);

        AddProductResponse addProductResponse =  inventoryService.addProduct(addProductRequest);

        assertNotNull(addProductResponse);
        assertEquals("test_description", addProductResponse.getDescription());

        UpdateProductRequest updateProductRequest = new UpdateProductRequest();



        updateProductRequest.setProductId("1");
        updateProductRequest.setName("new_test_name");
        updateProductRequest.setDescription("new_test_description");
        updateProductRequest.setPrice(new BigDecimal("1600"));
        updateProductRequest.setQuantity(40);
        updateProductRequest.setStorekeeperEmail("new_test@gmail.com");


        Mockito.when(storeKeeperRepository.findByEmail("new_test@gmail.com")).thenReturn(Optional.empty());
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                ()-> inventoryService.updateProduct(updateProductRequest)
        );

        assertEquals("User Not Found", exception.getMessage());
    }


    @Test
    public void addProduct_updateProduct_userNotLoggedIn() {
        StoreKeeper test_storeKeeper = createNewStoreKeeper();


        Product test_product = createNewProduct();

        addProductRequest.setProductId(test_product.getProductId());
        addProductRequest.setName(test_product.getName());
        addProductRequest.setDescription(test_product.getDescription());
        addProductRequest.setPrice(test_product.getPrice());
        addProductRequest.setQuantity(test_product.getQuantity());
        addProductRequest.setStorekeeperEmail("test@gmail.com");


        Mockito.when(storeKeeperRepository.findByEmail("test@gmail.com")).thenReturn(Optional.of(test_storeKeeper));


        Mockito.when(productRepository.save(any(Product.class))).thenReturn(test_product);

        AddProductResponse addProductResponse =  inventoryService.addProduct(addProductRequest);

        assertNotNull(addProductResponse);
        assertEquals("test_description", addProductResponse.getDescription());

        UpdateProductRequest updateProductRequest = new UpdateProductRequest();

        test_storeKeeper.setLoggedIn(false);


        updateProductRequest.setProductId("1");
        updateProductRequest.setName("new_test_name");
        updateProductRequest.setDescription("new_test_description");
        updateProductRequest.setPrice(new BigDecimal("1600"));
        updateProductRequest.setQuantity(40);
        updateProductRequest.setStorekeeperEmail("test@gmail.com");


        Mockito.when(storeKeeperRepository.findByEmail("test@gmail.com")).thenReturn(Optional.of(test_storeKeeper));
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                ()-> inventoryService.updateProduct(updateProductRequest)
        );

        assertEquals("test_name Not Logged In", exception.getMessage());
    }


    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"   ", "tes"})
    public void addProduct_updateProduct_invalidUpdateName(String name) {
        StoreKeeper test_storeKeeper = createNewStoreKeeper();


        Product test_product = createNewProduct();

        addProductRequest.setProductId(test_product.getProductId());
        addProductRequest.setName(test_product.getName());
        addProductRequest.setDescription(test_product.getDescription());
        addProductRequest.setPrice(test_product.getPrice());
        addProductRequest.setQuantity(test_product.getQuantity());
        addProductRequest.setStorekeeperEmail("test@gmail.com");


        Mockito.when(storeKeeperRepository.findByEmail("test@gmail.com")).thenReturn(Optional.of(test_storeKeeper));


        Mockito.when(productRepository.save(any(Product.class))).thenReturn(test_product);

        AddProductResponse addProductResponse =  inventoryService.addProduct(addProductRequest);

        assertNotNull(addProductResponse);
        assertEquals("test_description", addProductResponse.getDescription());

        UpdateProductRequest updateProductRequest = new UpdateProductRequest();

        updateProductRequest.setProductId("1");
        updateProductRequest.setName(name);
        updateProductRequest.setDescription("new_test_description");
        updateProductRequest.setPrice(new BigDecimal("1600"));
        updateProductRequest.setQuantity(40);
        updateProductRequest.setStorekeeperEmail("test@gmail.com");

//        Mockito.when(storeKeeperRepository.findByEmail("test@gmail.com")).thenReturn(Optional.of(test_storeKeeper));
//        Mockito.when(productRepository.findById("1")).thenReturn(Optional.of(test_product));


        InvalidProductDataException  exception = assertThrows(
                InvalidProductDataException.class,
                ()-> inventoryService.updateProduct(updateProductRequest)
        );

        assertEquals("Invalid Product Name", exception.getMessage());
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"   ", "tes"})
    public void addProduct_updateProduct_invalidUpdateDescription(String description) {
        StoreKeeper test_storeKeeper = createNewStoreKeeper();


        Product test_product = createNewProduct();

        addProductRequest.setProductId(test_product.getProductId());
        addProductRequest.setName(test_product.getName());
        addProductRequest.setDescription(test_product.getDescription());
        addProductRequest.setPrice(test_product.getPrice());
        addProductRequest.setQuantity(test_product.getQuantity());
        addProductRequest.setStorekeeperEmail("test@gmail.com");


        Mockito.when(storeKeeperRepository.findByEmail("test@gmail.com")).thenReturn(Optional.of(test_storeKeeper));


        Mockito.when(productRepository.save(any(Product.class))).thenReturn(test_product);

        AddProductResponse addProductResponse =  inventoryService.addProduct(addProductRequest);

        assertNotNull(addProductResponse);
        assertEquals("test_description", addProductResponse.getDescription());

        UpdateProductRequest updateProductRequest = new UpdateProductRequest();

        updateProductRequest.setProductId("1");
        updateProductRequest.setName("new_test_name");
        updateProductRequest.setDescription(description);
        updateProductRequest.setPrice(new BigDecimal("1600"));
        updateProductRequest.setQuantity(40);
        updateProductRequest.setStorekeeperEmail("test@gmail.com");

        InvalidProductDataException  exception = assertThrows(
                InvalidProductDataException.class,
                ()-> inventoryService.updateProduct(updateProductRequest)
        );

        assertEquals("Invalid Product Description", exception.getMessage());
    }

    @ParameterizedTest
    @NullSource
    @CsvSource({"-10000", "0", "-0.01"})
    public void addProduct_updateProduct_invalidUpdatePrice(BigDecimal price) {
        StoreKeeper test_storeKeeper = createNewStoreKeeper();


        Product test_product = createNewProduct();

        addProductRequest.setProductId(test_product.getProductId());
        addProductRequest.setName(test_product.getName());
        addProductRequest.setDescription(test_product.getDescription());
        addProductRequest.setPrice(test_product.getPrice());
        addProductRequest.setQuantity(test_product.getQuantity());
        addProductRequest.setStorekeeperEmail("test@gmail.com");


        Mockito.when(storeKeeperRepository.findByEmail("test@gmail.com")).thenReturn(Optional.of(test_storeKeeper));


        Mockito.when(productRepository.save(any(Product.class))).thenReturn(test_product);

        AddProductResponse addProductResponse =  inventoryService.addProduct(addProductRequest);

        assertNotNull(addProductResponse);
        assertEquals("test_description", addProductResponse.getDescription());

        UpdateProductRequest updateProductRequest = new UpdateProductRequest();

        updateProductRequest.setProductId("1");
        updateProductRequest.setName("new_test_name");
        updateProductRequest.setDescription("new_test_description");
        updateProductRequest.setPrice(price);
        updateProductRequest.setQuantity(40);
        updateProductRequest.setStorekeeperEmail("test@gmail.com");

        InvalidProductDataException  exception = assertThrows(
                InvalidProductDataException.class,
                ()-> inventoryService.updateProduct(updateProductRequest)
        );

        assertEquals("Invalid Product Price", exception.getMessage());
    }



    @ParameterizedTest
    @ValueSource(ints= {-10000, 0, -1})
    public void addProduct_updateProduct_invalidUpdateQuantity(int quantity) {
        StoreKeeper test_storeKeeper = createNewStoreKeeper();


        Product test_product = createNewProduct();

        addProductRequest.setProductId(test_product.getProductId());
        addProductRequest.setName(test_product.getName());
        addProductRequest.setDescription(test_product.getDescription());
        addProductRequest.setPrice(test_product.getPrice());
        addProductRequest.setQuantity(test_product.getQuantity());
        addProductRequest.setStorekeeperEmail("test@gmail.com");


        Mockito.when(storeKeeperRepository.findByEmail("test@gmail.com")).thenReturn(Optional.of(test_storeKeeper));


        Mockito.when(productRepository.save(any(Product.class))).thenReturn(test_product);

        AddProductResponse addProductResponse =  inventoryService.addProduct(addProductRequest);

        assertNotNull(addProductResponse);
        assertEquals("test_description", addProductResponse.getDescription());

        UpdateProductRequest updateProductRequest = new UpdateProductRequest();

        updateProductRequest.setProductId("1");
        updateProductRequest.setName("new_test_name");
        updateProductRequest.setDescription("new_test_description");
        updateProductRequest.setPrice(new BigDecimal("1600"));
        updateProductRequest.setQuantity(quantity);
        updateProductRequest.setStorekeeperEmail("test@gmail.com");

        InvalidProductDataException  exception = assertThrows(
                InvalidProductDataException.class,
                ()-> inventoryService.updateProduct(updateProductRequest)
        );

        assertEquals("Invalid Product Quantity", exception.getMessage());
    }


    @ParameterizedTest
    @ValueSource(strings = {"new_test_name", "New_Test_NAmE", "             new_test_name \n \t"})
    public void addProduct_updateProduct_withValidName(String name) {
        StoreKeeper test_storeKeeper = createNewStoreKeeper();


        Product test_product = createNewProduct();

        addProductRequest.setProductId(test_product.getProductId());
        addProductRequest.setName(test_product.getName());
        addProductRequest.setDescription(test_product.getDescription());
        addProductRequest.setPrice(test_product.getPrice());
        addProductRequest.setQuantity(test_product.getQuantity());
        addProductRequest.setStorekeeperEmail("test@gmail.com");


        Mockito.when(storeKeeperRepository.findByEmail("test@gmail.com")).thenReturn(Optional.of(test_storeKeeper));


        Mockito.when(productRepository.save(any(Product.class))).thenReturn(test_product);

        AddProductResponse addProductResponse =  inventoryService.addProduct(addProductRequest);

        assertNotNull(addProductResponse);
        assertEquals("test_description", addProductResponse.getDescription());
        assertEquals("1", addProductResponse.getProductId());


        UpdateProductRequest updateProductRequest = new UpdateProductRequest();


        updateProductRequest.setProductId(addProductResponse.getProductId());
        updateProductRequest.setName(name);
        updateProductRequest.setDescription("new_test_description");
        updateProductRequest.setPrice(new BigDecimal("1600"));
        updateProductRequest.setQuantity(40);
        updateProductRequest.setStorekeeperEmail("test@gmail.com");

        Mockito.when(storeKeeperRepository.findByEmail("test@gmail.com")).thenReturn(Optional.of(test_storeKeeper));
        Mockito.when(productRepository.findById("1")).thenReturn(Optional.of(test_product));


        UpdateProductResponse response = inventoryService.updateProduct(updateProductRequest);

        assertNotNull(response);
        assertEquals("new_test_name", response.getName());

    }

    @ParameterizedTest
    @ValueSource(strings = {"new_test_description", "New_Test_DescrIPtiOn", "             new_test_description \n \t"})
    public void addProduct_updateProduct_withValidDescription(String description) {
        StoreKeeper test_storeKeeper = createNewStoreKeeper();


        Product test_product = createNewProduct();

        addProductRequest.setProductId(test_product.getProductId());
        addProductRequest.setName(test_product.getName());
        addProductRequest.setDescription(test_product.getDescription());
        addProductRequest.setPrice(test_product.getPrice());
        addProductRequest.setQuantity(test_product.getQuantity());
        addProductRequest.setStorekeeperEmail("test@gmail.com");


        Mockito.when(storeKeeperRepository.findByEmail("test@gmail.com")).thenReturn(Optional.of(test_storeKeeper));


        Mockito.when(productRepository.save(any(Product.class))).thenReturn(test_product);

        AddProductResponse addProductResponse =  inventoryService.addProduct(addProductRequest);

        assertNotNull(addProductResponse);
        assertEquals("test_description", addProductResponse.getDescription());
        assertEquals("1", addProductResponse.getProductId());


        UpdateProductRequest updateProductRequest = new UpdateProductRequest();


        updateProductRequest.setProductId(addProductResponse.getProductId());
        updateProductRequest.setName("new_test_name");
        updateProductRequest.setDescription(description);
        updateProductRequest.setPrice(new BigDecimal("1600"));
        updateProductRequest.setQuantity(40);
        updateProductRequest.setStorekeeperEmail("test@gmail.com");

        Mockito.when(storeKeeperRepository.findByEmail("test@gmail.com")).thenReturn(Optional.of(test_storeKeeper));
        Mockito.when(productRepository.findById("1")).thenReturn(Optional.of(test_product));


        UpdateProductResponse response = inventoryService.updateProduct(updateProductRequest);

        assertNotNull(response);
        assertEquals("new_test_description", response.getDescription());

    }

//    Delete Drug Service

    @Test
    public void deleteDrug_null_request() {
        InvalidProductDataException exception = assertThrows(
                InvalidProductDataException.class,
                ()-> inventoryService.deleteProduct(null)
        );

        assertEquals("Invalid Request", exception.getMessage());
    }

    @Test
    public void userNotRegistered_cannotDeleteDrug() {
        DeleteProductRequest request = new DeleteProductRequest();
        request.setProductId("1");
        request.setStorekeeperEmail("unknown@gmail.com");

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                ()-> inventoryService.deleteProduct(request)
        );

        assertEquals("Storekeeper Not Found", exception.getMessage());


    }


    @Test
    public void userRegistered_notLoggedIn_cannotDelete() {
        StoreKeeper storeKeeper = createNewStoreKeeper();

        storeKeeper.setLoggedIn(false);


        Mockito.when(storeKeeperRepository.findByEmail("test@gmail.com")).thenReturn(Optional.of(storeKeeper));
        DeleteProductRequest request = new DeleteProductRequest();
        request.setProductId("1");
        request.setStorekeeperEmail("test@gmail.com");

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                ()-> inventoryService.deleteProduct(request)
        );

        assertEquals("test_name Not Logged In", exception.getMessage());

        Mockito.verify(productRepository, Mockito.never()).deleteById(any(String.class));

    }

    @Test
    public void verifiedStoreKeeper_invalidProductId() {
        StoreKeeper storeKeeper = createNewStoreKeeper();

        Mockito.when(storeKeeperRepository.findByEmail("test@gmail.com")).thenReturn(Optional.of(storeKeeper));
        DeleteProductRequest request = new DeleteProductRequest();
        request.setProductId("unknownID");
        request.setStorekeeperEmail("test@gmail.com");

        ProductNotFoundException exception = assertThrows(
                ProductNotFoundException.class,
                ()-> inventoryService.deleteProduct(request)
        );

        assertEquals("Product Does Not Exist", exception.getMessage());

        Mockito.verify(productRepository, Mockito.never()).deleteById(any(String.class));

    }

    @Test
    public void verifiedStoreKeeper_validProductid() {

        StoreKeeper storeKeeper = createNewStoreKeeper();
        Product product = createNewProduct();


        Mockito.when(storeKeeperRepository.findByEmail("test@gmail.com")).thenReturn(Optional.of(storeKeeper));

        Mockito.when(productRepository.findById("1")).thenReturn(Optional.of(product));

        DeleteProductRequest request = new DeleteProductRequest();
        request.setProductId("1");
        request.setStorekeeperEmail("test@gmail.com");


        DeleteProductResponse response = inventoryService.deleteProduct(request);

        assertNotNull(response);
        assertEquals("Product Deleted Successfully", response.getMessage());

        Mockito.verify(productRepository, Mockito.times(1)).deleteById(any(String.class));

    }





}
