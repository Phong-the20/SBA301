package fu.edu.sba301.slot32.repository;

import fu.edu.sba301.slot32.model.Category;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class CategoryRepository implements ICategoryRepository {
    private final Map<String, Category> storage = new ConcurrentHashMap<>();

    @Override
    public Category save(Category category) {
        if (category.getId() == null || category.getId().isBlank()) {
            category.setId("CAT-" + UUID.randomUUID().toString().substring(0, 6).toUpperCase());
        }
        storage.put(category.getId(), category);
        return category;
    }

    @Override
    public Optional<Category> findById(String id) {
        return Optional.ofNullable(storage.get(id));
    }

    @Override
    public List<Category> findAll() {
        return new ArrayList<>(storage.values());
    }

    @Override
    public boolean existsById(String id) {
        return storage.containsKey(id);
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
