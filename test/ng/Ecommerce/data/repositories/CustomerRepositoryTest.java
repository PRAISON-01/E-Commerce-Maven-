package ng.Ecommerce.data.repositories;

import ng.Ecommerce.data.models.Customer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.mongodb.test.autoconfigure.DataMongoTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataMongoTest
public class CustomerRepositoryTest {

    @Autowired
    private CustomerRepository customerRepository;

    @BeforeEach
    void setUp() {
        customerRepository.deleteAll();
    }

    @Test
    void findByEmail_returnsCustomer_whenEmailExists() {
        Customer customer = new Customer();
        customer.setName("Jane Doe");
        customer.setEmail("jane.doe@example.com");
        customer.setPassword("password123");
        customer.setLoggedIn(false);
        customerRepository.save(customer);

        Optional<Customer> found = customerRepository.findByEmail("jane.doe@example.com");

        assertThat(found).isPresent();
        assertThat(found.get().getName()).isEqualTo("Jane Doe");
        assertThat(found.get().getEmail()).isEqualTo("jane.doe@example.com");
    }

    @Test
    void findByEmail_returnsEmpty_whenEmailDoesNotExist() {
        Optional<Customer> found = customerRepository.findByEmail("nonexistent@example.com");

        assertThat(found).isNotPresent();
    }
}