package fu.edu.sba301.slot37.repository;

import fu.edu.sba301.slot37.model.Product;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends MongoRepository<Product, String> {
    List<Product> findByNameContainingIgnoreCase(String keyword);
    long countByActiveTrue();
}
