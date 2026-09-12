package ng.Ecommerce.services;

import ng.Ecommerce.dtos.requests.AddProductRequest;
import ng.Ecommerce.dtos.responses.AddProductResponse;
import ng.Ecommerce.exceptions.InvalidProductDataException;

public interface InventoryService {
    AddProductResponse addProduct(AddProductRequest addProductRequest) throws InvalidProductDataException;
//    void deleteProduct();
//    void getProduct();
//    void viewAllProducts();
}
