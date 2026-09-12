package ng.Ecommerce.services;

import ng.Ecommerce.data.models.StoreKeeper;
import ng.Ecommerce.data.repositories.StoreKeeperRepository;
import ng.Ecommerce.dtos.requests.AddProductRequest;
import ng.Ecommerce.dtos.responses.AddProductResponse;
import ng.Ecommerce.exceptions.InvalidProductDataException;
import org.springframework.stereotype.Service;

@Service
public class InventoryServiceImpl implements InventoryService {

    private final StoreKeeperRepository storeKeeperRepository ;

    public InventoryServiceImpl(StoreKeeperRepository storeKeeperRepository) {
        this.storeKeeperRepository = storeKeeperRepository;
    }

    @Override
    public AddProductResponse addProduct(AddProductRequest addProductRequest) throws InvalidProductDataException {
        StoreKeeper
        if(addProductRequest == null) throw new InvalidProductDataException("Invalid input");
        return null;
    }
}
