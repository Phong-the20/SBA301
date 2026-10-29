package fu.edu.sba301.slot35.repository;

import fu.edu.sba301.slot35.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends MongoRepository<Product, String> {

    // 1. Derived Equality Query
    List<Product> findByCategory(String category);

    // 2. Boolean Query
    List<Product> findByActiveTrue();

    // 3. Range Query
    List<Product> findByPriceBetween(double min, double max);

    // 4. String Search (case-insensitive substring)
    List<Product> findByNameContainingIgnoreCase(String keyword);

    // 5. AND Query
    List<Product> findByCategoryAndActiveTrue(String category);

    // 6. Static Sort
    List<Product> findByActiveTrueOrderByPriceDesc();

    // 7. Dynamic Sort
    List<Product> findAll(Sort sort);

    // 8. Database Paging
    Page<Product> findAll(Pageable pageable);

    // 9. Top/First Query
    List<Product> findTop3ByActiveTrueOrderByPriceDesc();

    // 10. JSON-based @Query with parameters
    @Query("{ 'active': true, 'stock': { $gte: ?0 } }")
    List<Product> findAvailableWithMinStock(int minStock);

    // 11. @Query with Field Projection (only load name and price)
    @Query(value = "{ 'active': true }", fields = "{ 'name': 1, 'price': 1 }")
    List<Product> findActiveNameAndPriceOnly();
}
