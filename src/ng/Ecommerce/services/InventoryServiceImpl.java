package ng.Ecommerce.services;

import ng.Ecommerce.data.models.Product;
import ng.Ecommerce.data.models.StoreKeeper;
import ng.Ecommerce.data.repositories.ProductRepository;
import ng.Ecommerce.data.repositories.StoreKeeperRepository;
import ng.Ecommerce.dtos.requests.AddProductRequest;
import ng.Ecommerce.dtos.responses.AddProductResponse;
import ng.Ecommerce.exceptions.InvalidProductDataException;
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
        validate(addProductRequest);
        StoreKeeper storeKeeper = storeKeeperRepository.findByEmail(addProductRequest.getStorekeeperEmail())
                .orElseThrow(() -> new IllegalArgumentException("User Not Found"));
        if(!storeKeeper.isLoggedIn()) throw new IllegalArgumentException(storeKeeper.getName() + " Not Logged In");

        Product product = map(addProductRequest);

        product = productRepository.save(product);


        AddProductResponse response = map(product);
        response.setMessage("Drug Added Successfully by " + addProductRequest.getStorekeeperEmail());
        return response;
    }
}
