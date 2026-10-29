package fu.edu.sba301.slot35.service;

import fu.edu.sba301.slot35.model.Product;
import java.util.List;

public interface IProductService {
    Product save(Product product);
    Product findById(String id);
    List<Product> findAll();
    void deleteById(String id);
    void validateProduct(Product product);
}
