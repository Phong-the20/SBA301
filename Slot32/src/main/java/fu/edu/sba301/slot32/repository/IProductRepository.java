package fu.edu.sba301.slot32.repository;

import fu.edu.sba301.slot32.model.Product;
import java.util.List;
import java.util.Optional;

public interface IProductRepository {
    Product save(Product product);
    Optional<Product> findById(String id);
    List<Product> findAll();
    List<Product> findByActive(boolean active);
    List<Product> findByCategoryId(String categoryId);
    List<Product> findByHdmiSpec(boolean hasHdmi);
    List<Product> findByTag(String tag);
    boolean deleteById(String id);
    long count();
}
