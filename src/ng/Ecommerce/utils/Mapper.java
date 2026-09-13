package ng.Ecommerce.utils;

import ng.Ecommerce.data.models.Customer;
import ng.Ecommerce.data.models.Product;
import ng.Ecommerce.data.models.StoreKeeper;
import ng.Ecommerce.dtos.requests.AddProductRequest;
import ng.Ecommerce.dtos.requests.RegisterRequest;
import ng.Ecommerce.dtos.requests.UpdateProductRequest;
import ng.Ecommerce.dtos.responses.*;

public class Mapper {

    public static Customer mapToCustomer(RegisterRequest request) {
        Customer customer = new Customer();
        customer.setName(request.getName());
        customer.setEmail(request.getEmail());
        customer.setPassword(request.getPassword());
        return customer;
    }

    public static StoreKeeper mapToStoreKeeper(RegisterRequest request) {
        StoreKeeper storeKeeper = new StoreKeeper();
        storeKeeper.setName(request.getName());
        storeKeeper.setEmail(request.getEmail());
        storeKeeper.setPassword(request.getPassword());
        return storeKeeper;
    }

    public static RegisterResponse mapToRegisterResponse(Customer customer) {
        RegisterResponse response = new RegisterResponse();
        response.setName(customer.getName());
        response.setEmail(customer.getEmail());
        return response;
    }

    public static RegisterResponse mapToRegisterResponse(StoreKeeper storeKeeper) {
        RegisterResponse response = new RegisterResponse();
        response.setName(storeKeeper.getName());
        response.setEmail(storeKeeper.getEmail());
        return response;
    }

    public static LoginResponse mapToLoginResponse(Customer customer) {
        LoginResponse response = new LoginResponse();
        response.setName(customer.getName());
        response.setLoggedIn(customer.isLoggedIn());
        return response;
    }

    public static LoginResponse mapToLoginResponse(StoreKeeper storeKeeper) {
        LoginResponse response = new LoginResponse();
        response.setName(storeKeeper.getName());
        response.setLoggedIn(storeKeeper.isLoggedIn());
        return response;
    }

    public static LogoutResponse mapToLogoutResponse(Customer customer) {
        LogoutResponse response = new LogoutResponse();
        response.setEmail(customer.getEmail());
        response.setMessage("Logout successful");
        return response;
    }

    public static LogoutResponse mapToLogoutResponse(StoreKeeper storeKeeper) {
        LogoutResponse response = new LogoutResponse();
        response.setEmail(storeKeeper.getEmail());
        response.setMessage("Logout successful");
        return response;
    }

    public static Product map(AddProductRequest addProductRequest) {
        Product product = new Product();
        product.setProductId(addProductRequest.getProductId());
        product.setName(addProductRequest.getName());
        product.setDescription(addProductRequest.getDescription());
        product.setPrice(addProductRequest.getPrice());
        product.setQuantity(addProductRequest.getQuantity());
        return product;
    }

    public static AddProductResponse map(Product product) {
        AddProductResponse response = new AddProductResponse();

        response.setProductId(product.getProductId());
        response.setName(product.getName());
        response.setDescription(product.getDescription());
        response.setPrice(product.getPrice());
        response.setQuantity(product.getQuantity());
        return response;
    }

    public static Product map(UpdateProductRequest request, Product product) {
        product.setName(request.getName().toLowerCase().trim());
        product.setDescription(request.getDescription().toLowerCase().trim());
        product.setPrice(request.getPrice());
        product.setQuantity(request.getQuantity());
        product.setProductId(request.getProductId());

        return product;
    }

    public static UpdateProductResponse map(UpdateProductResponse response, Product product) {
        response.setMessage("Product Added Successfully");
        response.setName(product.getName());
        response.setDescription(product.getDescription());
        response.setQuantity(product.getQuantity());
        response.setProductId(product.getProductId());
        response.setPrice(product.getPrice());

        return response;
    }

}