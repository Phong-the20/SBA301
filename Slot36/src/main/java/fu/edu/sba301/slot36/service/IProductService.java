package fu.edu.sba301.slot36.service;

import fu.edu.sba301.slot36.model.Product;
import java.util.List;

public interface IProductService {
    List<Product> findAll();
    Product findById(String id);
    Product save(Product product);
    void deleteById(String id);
    List<Product> search(String keyword);
    void validateProduct(Product product);
}
