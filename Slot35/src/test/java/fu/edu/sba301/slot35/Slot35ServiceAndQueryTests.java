package fu.edu.sba301.slot35;

import com.mongodb.client.result.UpdateResult;
import fu.edu.sba301.slot35.model.Product;
import fu.edu.sba301.slot35.repository.ProductRepository;
import fu.edu.sba301.slot35.service.IProductQueryService;
import fu.edu.sba301.slot35.service.IProductService;
import fu.edu.sba301.slot35.service.ProductQueryService;
import fu.edu.sba301.slot35.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class Slot35ServiceAndQueryTests {

    @Mock
    private ProductRepository repository;

    @Mock
    private MongoTemplate mongoTemplate;

    private IProductService productService;
    private IProductQueryService queryService;

    @BeforeEach
    void setUp() {
        productService = new ProductService(repository);
        queryService = new ProductQueryService(mongoTemplate);
    }

    @Test
    void testSaveValidProduct() {
        Product p = new Product(null, "Test Book", "BOOK", 29.99, true, 10);
        when(repository.save(p)).thenReturn(new Product("P1", "Test Book", "BOOK", 29.99, true, 10));

        Product saved = productService.save(p);
        assertNotNull(saved);
        assertEquals("P1", saved.getId());
        verify(repository, times(1)).save(p);
    }

    @Test
    void testSaveProductNegativePriceThrowsException() {
        Product bad = new Product(null, "Negative Price", "BOOK", -10.0, true, 5);
        assertThrows(IllegalArgumentException.class, () -> productService.save(bad));
        verify(repository, never()).save(any());
    }

    @Test
    void testSaveProductNegativeStockThrowsException() {
        Product bad = new Product(null, "Negative Stock", "BOOK", 10.0, true, -1);
        assertThrows(IllegalArgumentException.class, () -> productService.save(bad));
        verify(repository, never()).save(any());
    }

    @Test
    void testFindByIdSuccess() {
        Product p = new Product("P1", "Found Product", "TECH", 99.0, true, 5);
        when(repository.findById("P1")).thenReturn(Optional.of(p));

        Product found = productService.findById("P1");
        assertNotNull(found);
        assertEquals("Found Product", found.getName());
    }

    @Test
    void testDynamicQuerySearchReturnsMatchingList() {
        List<Product> expected = List.of(new Product("P101", "Spring Boot", "BOOK", 45.0, true, 20));
        when(mongoTemplate.find(any(Query.class), eq(Product.class))).thenReturn(expected);

        List<Product> result = queryService.searchDynamic("BOOK", "Spring", 30.0, 50.0, true, Sort.by("price").ascending(), 5);
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Spring Boot", result.get(0).getName());
        verify(mongoTemplate, times(1)).find(any(Query.class), eq(Product.class));
    }

    @Test
    void testDynamicQueryPagedReturnsPage() {
        List<Product> expected = List.of(new Product("P101", "Book 1", "BOOK", 30.0, true, 10));
        when(mongoTemplate.count(any(Query.class), eq(Product.class))).thenReturn(1L);
        when(mongoTemplate.find(any(Query.class), eq(Product.class))).thenReturn(expected);

        Page<Product> page = queryService.searchPaged("BOOK", null, null, null, true, PageRequest.of(0, 5));
        assertNotNull(page);
        assertEquals(1, page.getTotalElements());
        assertEquals(1, page.getContent().size());
    }

    @Test
    void testPartialUpdateStockSuccess() {
        UpdateResult mockResult = mock(UpdateResult.class);
        when(mockResult.getModifiedCount()).thenReturn(1L);
        when(mongoTemplate.updateFirst(any(Query.class), any(Update.class), eq(Product.class))).thenReturn(mockResult);

        long modified = queryService.partialUpdateStock("P1", 40);
        assertEquals(1L, modified);
        verify(mongoTemplate, times(1)).updateFirst(any(Query.class), any(Update.class), eq(Product.class));
    }
}
