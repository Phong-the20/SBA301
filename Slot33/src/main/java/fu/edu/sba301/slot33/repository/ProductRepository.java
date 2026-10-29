package fu.edu.sba301.slot33.repository;

import fu.edu.sba301.slot33.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends MongoRepository<Product, String> {

    List<Product> findByCategoryAndActiveTrue(String category);

    List<Product> findByNameContainingIgnoreCase(String keyword);

    Page<Product> findByCategory(String category, Pageable pageable);
}
