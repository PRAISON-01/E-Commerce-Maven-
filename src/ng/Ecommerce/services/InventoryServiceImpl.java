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
import ng.Ecommerce.utils.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

import static ng.Ecommerce.utils.Mapper.map;
import static ng.Ecommerce.utils.Validator.validate;

@Service
public class InventoryServiceImpl implements InventoryService {

    @Autowired
    private StoreKeeperRepository  storeKeeperRepository;
    @Autowired
    private ProductRepository productRepository;

    @Override
    public AddProductResponse addProduct(AddProductRequest addProductRequest) throws InvalidProductDataException {
        if(addProductRequest == null) throw new InvalidProductDataException("Invalid input");

        StoreKeeper storeKeeper = storeKeeperRepository.findByEmail(addProductRequest.getStorekeeperEmail().trim().toLowerCase())
                .orElseThrow(() -> new IllegalArgumentException("User Not Found"));

        validate(addProductRequest);
        if(!storeKeeper.isLoggedIn()) throw new IllegalArgumentException(storeKeeper.getName() + " Not Logged In");

        Product product = map(addProductRequest);

        product = productRepository.save(product);


        AddProductResponse response = map(product);
        response.setMessage("Drug Added Successfully by " + addProductRequest.getStorekeeperEmail());
        return response;
    }

    @Override
    public UpdateProductResponse updateProduct(UpdateProductRequest updateProductRequest) throws InvalidProductDataException {
        if(updateProductRequest == null) throw new InvalidProductDataException("Invalid input");
        validate(updateProductRequest);
        StoreKeeper storeKeeper = storeKeeperRepository.findByEmail(updateProductRequest.getStorekeeperEmail().trim().toLowerCase())
                .orElseThrow(() -> new IllegalArgumentException("User Not Found"));

        if(!storeKeeper.isLoggedIn()) throw new IllegalArgumentException(storeKeeper.getName() + " Not Logged In");

        Product product =   productRepository.findById(updateProductRequest.getProductId())
                .orElseThrow(()-> new ProductNotFoundException("Product Not Found"));

        map(updateProductRequest, product);

        Product savedProduct = productRepository.save(product);

        UpdateProductResponse response = new UpdateProductResponse();
        return map(response, savedProduct);
    }

    @Override
    public DeleteProductResponse deleteProduct(DeleteProductRequest deleteProductRequest) throws InvalidProductDataException {
        if(deleteProductRequest == null) throw new InvalidProductDataException("Invalid Request");

        StoreKeeper storeKeeper = storeKeeperRepository.findByEmail(deleteProductRequest.getStorekeeperEmail().toLowerCase().trim())
                .orElseThrow(() -> new IllegalArgumentException("Storekeeper Not Found"));
        if(!storeKeeper.isLoggedIn()) throw new IllegalArgumentException(storeKeeper.getName() + " Not Logged In");

        productRepository.findById(deleteProductRequest.getProductId())
                .orElseThrow(()-> new ProductNotFoundException("Product Does Not Exist"));

        productRepository.deleteById(deleteProductRequest.getProductId());

        DeleteProductResponse response= new DeleteProductResponse();

        response.setMessage("Product Deleted Successfully");
        return response;
    }
}
