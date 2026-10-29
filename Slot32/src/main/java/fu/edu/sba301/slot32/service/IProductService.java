package fu.edu.sba301.slot32.service;

import fu.edu.sba301.slot32.model.Product;
import java.util.List;

public interface IProductService {
    Product createProduct(Product product);
    Product getById(String id);
    List<Product> getAll();
    List<Product> getActiveProducts();
    List<Product> getProductsByCategory(String categoryId);
    List<Product> getProductsWithHdmi();
    List<Product> getProductsByTag(String tag);
    Product updateProduct(Product product);
    void deleteProduct(String id);
    void validateSchema(Product product);
}
