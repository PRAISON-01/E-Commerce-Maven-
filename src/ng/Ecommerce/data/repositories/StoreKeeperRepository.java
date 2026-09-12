package ng.Ecommerce.data.repositories;

import ng.Ecommerce.data.models.StoreKeeper;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface StoreKeeperRepository extends MongoRepository<StoreKeeper, String> {
    Optional<StoreKeeper> findByEmail(String email);
}
