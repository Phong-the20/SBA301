package fu.edu.sba301.slot37.service;

import fu.edu.sba301.slot37.model.Product;
import fu.edu.sba301.slot37.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService implements IProductService {

    private final ProductRepository repository;

    public ProductService(ProductRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<Product> findAll() {
        return repository.findAll();
    }

    @Override
    public Product findById(String id) {
        return repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Product not found with ID: " + id));
    }

    @Override
    public Product save(Product product) {
        validateProduct(product);
        return repository.save(product);
    }

    @Override
    public void deleteById(String id) {
        if (!repository.existsById(id)) {
            throw new IllegalArgumentException("Cannot delete non-existent product ID: " + id);
        }
        repository.deleteById(id);
    }

    @Override
    public List<Product> search(String keyword) {
        if (keyword == null || keyword.isBlank()) {
            return repository.findAll();
        }
        return repository.findByNameContainingIgnoreCase(keyword.trim());
    }

    @Override
    public long count() {
        return repository.count();
    }

    @Override
    public long countActive() {
        return repository.countByActiveTrue();
    }

    @Override
    public void validateProduct(Product product) {
        if (product == null) {
            throw new IllegalArgumentException("Product cannot be null");
        }
        if (product.getName() == null || product.getName().isBlank()) {
            throw new IllegalArgumentException("Product name is required");
        }
        if (product.getCategory() == null || product.getCategory().isBlank()) {
            throw new IllegalArgumentException("Product category is required");
        }
        if (product.getPrice() < 0.0) {
            throw new IllegalArgumentException("Product price cannot be negative");
        }
    }
}
