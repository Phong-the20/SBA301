package fu.edu.sba301.slot35.service;

import com.mongodb.client.result.UpdateResult;
import fu.edu.sba301.slot35.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProductQueryService implements IProductQueryService {

    private final MongoTemplate mongoTemplate;

    public ProductQueryService(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    private Query buildDynamicQuery(String category, String keyword, Double minPrice, Double maxPrice, Boolean active) {
        Query query = new Query();
        List<Criteria> criteriaList = new ArrayList<>();

        if (category != null && !category.isBlank()) {
            criteriaList.add(Criteria.where("category").is(category.trim()));
        }

        if (keyword != null && !keyword.isBlank()) {
            criteriaList.add(Criteria.where("name").regex(keyword.trim(), "i"));
        }

        if (minPrice != null && maxPrice != null) {
            criteriaList.add(Criteria.where("price").gte(minPrice).lte(maxPrice));
        } else if (minPrice != null) {
            criteriaList.add(Criteria.where("price").gte(minPrice));
        } else if (maxPrice != null) {
            criteriaList.add(Criteria.where("price").lte(maxPrice));
        }

        if (active != null) {
            criteriaList.add(Criteria.where("active").is(active));
        }

        if (!criteriaList.isEmpty()) {
            query.addCriteria(new Criteria().andOperator(criteriaList.toArray(new Criteria[0])));
        }

        return query;
    }

    @Override
    public List<Product> searchDynamic(String category, String keyword, Double minPrice, Double maxPrice, Boolean active, Sort sort, Integer limit) {
        Query query = buildDynamicQuery(category, keyword, minPrice, maxPrice, active);

        if (sort != null) {
            query.with(sort);
        }
        if (limit != null && limit > 0) {
            query.limit(limit);
        }

        return mongoTemplate.find(query, Product.class);
    }

    @Override
    public Page<Product> searchPaged(String category, String keyword, Double minPrice, Double maxPrice, Boolean active, Pageable pageable) {
        Query query = buildDynamicQuery(category, keyword, minPrice, maxPrice, active);
        long total = mongoTemplate.count(query, Product.class);

        if (pageable != null) {
            query.with(pageable);
        }

        List<Product> content = mongoTemplate.find(query, Product.class);
        return new PageImpl<>(content, pageable != null ? pageable : Pageable.unpaged(), total);
    }

    @Override
    public long partialUpdateStock(String id, int newStock) {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("Product ID cannot be blank");
        }
        if (newStock < 0) {
            throw new IllegalArgumentException("Stock cannot be negative");
        }

        Query query = Query.query(Criteria.where("id").is(id));
        Update update = new Update().set("stock", newStock);

        UpdateResult result = mongoTemplate.updateFirst(query, update, Product.class);
        return result.getModifiedCount();
    }
}
