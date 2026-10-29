package fu.edu.sba301.slot32.service;

import fu.edu.sba301.slot32.model.Category;
import java.util.List;

public interface ICategoryService {
    Category createCategory(Category category);
    Category getById(String id);
    List<Category> getAll();
    Category updateCategory(Category category);
    void deleteCategory(String id);
}
