package fu.edu.sba301.assignment02.repository;

/**
 * Author: Tran The Phong
 * Student ID: CE190157
 * Assignment: 02 - FUNewsManagementSystem
 */

import fu.edu.sba301.assignment02.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {
    List<Category> findByStatus(Integer status);
    List<Category> findByCategoryNameContainingIgnoreCase(String name);
    boolean existsByCategoryNameIgnoreCase(String name);

    @Query(value = "SELECT * FROM category WHERE category_name COLLATE SQL_Latin1_General_CP1_CI_AI LIKE CONCAT('%', :name, '%')", nativeQuery = true)
    List<Category> searchByCategoryName(@Param("name") String name);
}

