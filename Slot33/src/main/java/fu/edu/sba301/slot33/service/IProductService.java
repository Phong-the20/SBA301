package fu.edu.sba301.slot33.service;

import fu.edu.sba301.slot33.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IProductService {
    Product create(Product product);
    Product findById(String id);
    List<Product> findAll();
    Page<Product> findAll(Pageable pageable);
    List<Product> findByCategoryActive(String category);
    List<Product> searchByName(String keyword);
    Product update(Product product);
    void deleteById(String id);
    long count();
    long increaseStock(String id, int amount);
    List<Product> findLowStockActiveProducts(int threshold);
    void validateBusinessRules(Product product);
}
