package ng.Ecommerce.data.repositories;

import ng.Ecommerce.data.models.StoreKeeper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.mongodb.test.autoconfigure.DataMongoTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataMongoTest
public class StoreKeeperRepositoryTest {

    @Autowired
    private StoreKeeperRepository storeKeeperRepository;

    @BeforeEach
    void setUp() {
        storeKeeperRepository.deleteAll();
    }

    @Test
    void findByEmail_returnsStoreKeeper_whenEmailExists() {
        StoreKeeper storeKeeper = new StoreKeeper();
        storeKeeper.setName("John Smith");
        storeKeeper.setEmail("john.smith@example.com");
        storeKeeper.setPassword("securePass1");
        storeKeeper.setLoggedIn(false);
        storeKeeperRepository.save(storeKeeper);

        Optional<StoreKeeper> found = storeKeeperRepository.findByEmail("john.smith@example.com");

        assertThat(found).isPresent();
        assertThat(found.get().getName()).isEqualTo("John Smith");
        assertThat(found.get().getEmail()).isEqualTo("john.smith@example.com");
    }

    @Test
    void findByEmail_returnsEmpty_whenEmailDoesNotExist() {
        Optional<StoreKeeper> found = storeKeeperRepository.findByEmail("nobody@example.com");

        assertThat(found).isNotPresent();
    }
}