package fu.edu.sba301.slot35.service;

import fu.edu.sba301.slot35.model.Product;
import fu.edu.sba301.slot35.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService implements IProductService {

    private final ProductRepository repository;

    public ProductService(ProductRepository repository) {
        this.repository = repository;
    }

    @Override
    public Product save(Product product) {
        validateProduct(product);
        return repository.save(product);
    }

    @Override
    public Product findById(String id) {
        return repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Product not found with ID: " + id));
    }

    @Override
    public List<Product> findAll() {
        return repository.findAll();
    }

    @Override
    public void deleteById(String id) {
        if (!repository.existsById(id)) {
            throw new IllegalArgumentException("Product not found with ID: " + id);
        }
        repository.deleteById(id);
    }

    @Override
    public void validateProduct(Product product) {
        if (product == null) {
            throw new IllegalArgumentException("Product cannot be null");
        }
        if (product.getName() == null || product.getName().isBlank()) {
            throw new IllegalArgumentException("Product name is required and cannot be blank");
        }
        if (product.getCategory() == null || product.getCategory().isBlank()) {
            throw new IllegalArgumentException("Product category is required");
        }
        if (product.getPrice() < 0.0) {
            throw new IllegalArgumentException("Product price cannot be negative");
        }
        if (product.getStock() < 0) {
            throw new IllegalArgumentException("Product stock cannot be negative");
        }
    }
}
