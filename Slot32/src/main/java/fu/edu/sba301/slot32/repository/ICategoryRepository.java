package fu.edu.sba301.slot32.repository;

import fu.edu.sba301.slot32.model.Category;
import java.util.List;
import java.util.Optional;

public interface ICategoryRepository {
    Category save(Category category);
    Optional<Category> findById(String id);
    List<Category> findAll();
    boolean existsById(String id);
    boolean deleteById(String id);
    long count();
}
