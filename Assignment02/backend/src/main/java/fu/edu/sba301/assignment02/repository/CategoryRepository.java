package fu.edu.sba301.assignment02.repository;

/**
 * Author: Tran The Phong
 * Student ID: CE190157
 * Assignment: 02 - FUNewsManagementSystem
 */

import fu.edu.sba301.assignment02.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {
    List<Category> findByStatus(Integer status);
    List<Category> findByCategoryNameContainingIgnoreCase(String name);
    boolean existsByCategoryNameIgnoreCase(String name);
}

