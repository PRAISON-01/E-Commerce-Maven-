package ng.Ecommerce.services;

import ng.Ecommerce.data.models.Product;
import ng.Ecommerce.data.repositories.ProductRepository;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class SalesServiceTest {

    @InjectMocks
    private SalesServiceImpl salesService;

    @Mock
    private ProductRepository productRepository;



}
