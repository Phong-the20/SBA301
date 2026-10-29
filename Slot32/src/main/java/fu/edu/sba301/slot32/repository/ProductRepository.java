package fu.edu.sba301.slot32.repository;

import fu.edu.sba301.slot32.model.Product;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class ProductRepository implements IProductRepository {
    private final Map<String, Product> storage = new ConcurrentHashMap<>();

    @Override
    public Product save(Product product) {
        if (product.getId() == null || product.getId().isBlank()) {
            product.setId("PROD-" + UUID.randomUUID().toString().substring(0, 6).toUpperCase());
        }
        storage.put(product.getId(), product);
        return product;
    }

    @Override
    public Optional<Product> findById(String id) {
        return Optional.ofNullable(storage.get(id));
    }

    @Override
    public List<Product> findAll() {
        return new ArrayList<>(storage.values());
    }

    @Override
    public List<Product> findByActive(boolean active) {
        return storage.values().stream()
                .filter(p -> p.isActive() == active)
                .collect(Collectors.toList());
    }

    @Override
    public List<Product> findByCategoryId(String categoryId) {
        return storage.values().stream()
                .filter(p -> Objects.equals(p.getCategoryId(), categoryId))
                .collect(Collectors.toList());
    }

    @Override
    public List<Product> findByHdmiSpec(boolean hasHdmi) {
        return storage.values().stream()
                .filter(p -> p.getSpecs() != null && p.getSpecs().isHdmi() == hasHdmi)
                .collect(Collectors.toList());
    }

    @Override
    public List<Product> findByTag(String tag) {
        return storage.values().stream()
                .filter(p -> p.getTags() != null && p.getTags().contains(tag))
                .collect(Collectors.toList());
    }

    @Override
    public boolean deleteById(String id) {
        return storage.remove(id) != null;
    }

    @Override
    public long count() {
        return storage.size();
    }
}
