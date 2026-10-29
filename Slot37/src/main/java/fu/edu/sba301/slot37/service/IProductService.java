package fu.edu.sba301.slot37.service;

import fu.edu.sba301.slot37.model.Product;
import java.util.List;

public interface IProductService {
    List<Product> findAll();
    Product findById(String id);
    Product save(Product product);
    void deleteById(String id);
    List<Product> search(String keyword);
    long count();
    long countActive();
    void validateProduct(Product product);
}
