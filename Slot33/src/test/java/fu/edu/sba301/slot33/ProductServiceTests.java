package fu.edu.sba301.slot33;

import com.mongodb.client.result.UpdateResult;
import fu.edu.sba301.slot33.model.Product;
import fu.edu.sba301.slot33.repository.ProductRepository;
import fu.edu.sba301.slot33.service.IProductService;
import fu.edu.sba301.slot33.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTests {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private MongoTemplate mongoTemplate;

    private IProductService productService;

    @BeforeEach
    void setUp() {
        productService = new ProductService(productRepository, mongoTemplate);
    }

    @Test
    void testCreateValidProduct() {
        Product p = new Product(null, "Gaming Mouse", "Accessory", new BigDecimal("49.99"), 15, true, List.of("gaming"));
        when(productRepository.save(any(Product.class))).thenReturn(new Product("PROD-100", "Gaming Mouse", "Accessory", new BigDecimal("49.99"), 15, true, List.of("gaming")));

        Product saved = productService.create(p);
        assertNotNull(saved);
        assertEquals("PROD-100", saved.getId());
        verify(productRepository, times(1)).save(p);
    }

    @Test
    void testCreateProductInvalidPriceThrowsException() {
        Product badPrice = new Product(null, "Negative Price", "Tech", new BigDecimal("-5.00"), 10, true, null);
        assertThrows(IllegalArgumentException.class, () -> productService.create(badPrice));
        verify(productRepository, never()).save(any());
    }

    @Test
    void testCreateProductBlankNameThrowsException() {
        Product blankName = new Product(null, "   ", "Tech", new BigDecimal("10.00"), 10, true, null);
        assertThrows(IllegalArgumentException.class, () -> productService.create(blankName));
        verify(productRepository, never()).save(any());
    }

    @Test
    void testFindByIdSuccess() {
        Product p = new Product("PROD-1", "Monitor", "Display", new BigDecimal("250.00"), 5, true, null);
        when(productRepository.findById("PROD-1")).thenReturn(Optional.of(p));

        Product found = productService.findById("PROD-1");
        assertNotNull(found);
        assertEquals("Monitor", found.getName());
    }

    @Test
    void testFindByIdNotFoundThrowsException() {
        when(productRepository.findById("NON_EXIST")).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> productService.findById("NON_EXIST"));
    }

    @Test
    void testPartialUpdateIncreaseStock() {
        UpdateResult mockResult = mock(UpdateResult.class);
        when(mockResult.getModifiedCount()).thenReturn(1L);
        when(mongoTemplate.updateFirst(any(Query.class), any(Update.class), eq(Product.class))).thenReturn(mockResult);

        long modified = productService.increaseStock("PROD-1", 5);
        assertEquals(1L, modified);
        verify(mongoTemplate, times(1)).updateFirst(any(Query.class), any(Update.class), eq(Product.class));
    }
}
