package fu.edu.sba301.slot35.service;

import fu.edu.sba301.slot35.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;

public interface IProductQueryService {
    List<Product> searchDynamic(String category, String keyword, Double minPrice, Double maxPrice, Boolean active, Sort sort, Integer limit);
    Page<Product> searchPaged(String category, String keyword, Double minPrice, Double maxPrice, Boolean active, Pageable pageable);
    long partialUpdateStock(String id, int newStock);
}
