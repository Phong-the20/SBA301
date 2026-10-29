package fu.edu.sba301.slot33.service;

import com.mongodb.client.result.UpdateResult;
import fu.edu.sba301.slot33.model.Product;
import fu.edu.sba301.slot33.repository.ProductRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class ProductService implements IProductService {

    private final ProductRepository repository;
    private final MongoTemplate mongoTemplate;

    public ProductService(ProductRepository repository, MongoTemplate mongoTemplate) {
        this.repository = repository;
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public Product create(Product product) {
        validateBusinessRules(product);
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
    public Page<Product> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Override
    public List<Product> findByCategoryActive(String category) {
        return repository.findByCategoryAndActiveTrue(category);
    }

    @Override
    public List<Product> searchByName(String keyword) {
        if (keyword == null || keyword.isBlank()) {
            return repository.findAll();
        }
        return repository.findByNameContainingIgnoreCase(keyword.trim());
    }

    @Override
    public Product update(Product product) {
        if (product == null || product.getId() == null) {
            throw new IllegalArgumentException("Product ID is required for update");
        }
        if (!repository.existsById(product.getId())) {
            throw new IllegalArgumentException("Cannot update non-existent product ID: " + product.getId());
        }
        validateBusinessRules(product);
        return repository.save(product);
    }

    @Override
    public void deleteById(String id) {
        if (!repository.existsById(id)) {
            throw new IllegalArgumentException("Product not found with ID: " + id);
        }
        repository.deleteById(id);
    }

    @Override
    public long count() {
        return repository.count();
    }

    @Override
    public long increaseStock(String id, int amount) {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("Product ID cannot be blank");
        }
        Query query = Query.query(Criteria.where("id").is(id));
        Update update = new Update().inc("stock", amount);
        UpdateResult result = mongoTemplate.updateFirst(query, update, Product.class);
        return result.getModifiedCount();
    }

    @Override
    public List<Product> findLowStockActiveProducts(int threshold) {
        Query query = new Query();
        query.addCriteria(Criteria.where("active").is(true).and("stock").lt(threshold));
        return mongoTemplate.find(query, Product.class);
    }

    @Override
    public void validateBusinessRules(Product product) {
        if (product == null) {
            throw new IllegalArgumentException("Product cannot be null");
        }
        if (product.getName() == null || product.getName().isBlank()) {
            throw new IllegalArgumentException("Product name is required and cannot be blank");
        }
        if (product.getCategory() == null || product.getCategory().isBlank()) {
            throw new IllegalArgumentException("Product category is required");
        }
        if (product.getPrice() == null || product.getPrice().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Product price must be a valid non-negative number");
        }
        if (product.getStock() < 0) {
            throw new IllegalArgumentException("Product stock cannot be negative");
        }
    }
}
