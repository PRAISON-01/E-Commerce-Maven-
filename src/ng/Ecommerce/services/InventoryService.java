package ng.Ecommerce.services;

import ng.Ecommerce.dtos.requests.AddProductRequest;
import ng.Ecommerce.dtos.requests.DeleteProductRequest;
import ng.Ecommerce.dtos.requests.UpdateProductRequest;
import ng.Ecommerce.dtos.responses.AddProductResponse;
import ng.Ecommerce.dtos.responses.DeleteProductResponse;
import ng.Ecommerce.dtos.responses.UpdateProductResponse;
import ng.Ecommerce.exceptions.InvalidProductDataException;

public interface InventoryService {
    AddProductResponse addProduct(AddProductRequest addProductRequest) throws InvalidProductDataException;
    UpdateProductResponse updateProduct(UpdateProductRequest updateProductRequest) throws InvalidProductDataException;
    DeleteProductResponse deleteProduct(DeleteProductRequest deleteProductRequest) throws InvalidProductDataException;
}
