package fu.edu.sba301.slot32.service;

import fu.edu.sba301.slot32.model.Product;
import fu.edu.sba301.slot32.repository.ICategoryRepository;
import fu.edu.sba301.slot32.repository.IProductRepository;

import java.util.List;

public class ProductService implements IProductService {
    private final IProductRepository productRepository;
    private final ICategoryRepository categoryRepository;

    public ProductService(IProductRepository productRepository, ICategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public Product createProduct(Product product) {
        validateSchema(product);
        return productRepository.save(product);
    }

    @Override
    public Product getById(String id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Product not found with ID: " + id));
    }

    @Override
    public List<Product> getAll() {
        return productRepository.findAll();
    }

    @Override
    public List<Product> getActiveProducts() {
        return productRepository.findByActive(true);
    }

    @Override
    public List<Product> getProductsByCategory(String categoryId) {
        return productRepository.findByCategoryId(categoryId);
    }

    @Override
    public List<Product> getProductsWithHdmi() {
        return productRepository.findByHdmiSpec(true);
    }

    @Override
    public List<Product> getProductsByTag(String tag) {
        return productRepository.findByTag(tag);
    }

    @Override
    public Product updateProduct(Product product) {
        if (product == null || product.getId() == null) {
            throw new IllegalArgumentException("Product and Product ID are required for update");
        }
        if (productRepository.findById(product.getId()).isEmpty()) {
            throw new IllegalArgumentException("Cannot update non-existent product: " + product.getId());
        }
        validateSchema(product);
        return productRepository.save(product);
    }

    @Override
    public void deleteProduct(String id) {
        if (!productRepository.deleteById(id)) {
            throw new IllegalArgumentException("Product not found with ID: " + id);
        }
    }

    @Override
    public void validateSchema(Product product) {
        if (product == null) {
            throw new IllegalArgumentException("Product cannot be null");
        }
        if (product.getName() == null || product.getName().isBlank()) {
            throw new IllegalArgumentException("Product name is required and cannot be blank");
        }
        if (product.getPrice() == null) {
            throw new IllegalArgumentException("Product price is required (cannot be null or string type drift)");
        }
        if (product.getPrice() < 0.0) {
            throw new IllegalArgumentException("Product price cannot be negative: " + product.getPrice());
        }
        // Relationship Validation: Check if category exists
        if (product.getCategoryId() != null && !product.getCategoryId().isBlank()) {
            if (!categoryRepository.existsById(product.getCategoryId())) {
                throw new IllegalArgumentException("Referenced Category ID does not exist: " + product.getCategoryId());
            }
        }
        // Nested dimensions validation
        if (product.getDimensions() != null) {
            if (product.getDimensions().getWidthCm() <= 0 || product.getDimensions().getHeightCm() <= 0) {
                throw new IllegalArgumentException("Product dimensions must be strictly positive");
            }
        }
    }
}
