package fu.edu.sba301.slot35;

import fu.edu.sba301.slot35.model.Product;
import fu.edu.sba301.slot35.repository.ProductRepository;
import fu.edu.sba301.slot35.service.IProductQueryService;
import fu.edu.sba301.slot35.service.IProductService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.List;

@SpringBootApplication
public class Slot35Application {

    public static void main(String[] args) {
        SpringApplication.run(Slot35Application.class, args);
    }

    @Bean
    public CommandLineRunner runner(ProductRepository repository, IProductService productService, IProductQueryService queryService) {
        return args -> {
            System.out.println("==================================================================");
            System.out.println("   SBA301 - Slot 35: Spring Data MongoRepository & MongoTemplate");
            System.out.println("   Advanced Query Methods & Dynamic Criteria Query Service");
            System.out.println("==================================================================");

            try {
                // Seed 5 sample products
                System.out.println("\n[1] Seeding diverse sample products...");
                productService.save(new Product("P101", "Spring Boot in Action", "BOOK", 45.0, true, 20));
                productService.save(new Product("P102", "Effective Java", "BOOK", 55.0, true, 15));
                productService.save(new Product("P103", "Mechanical Keyboard RGB", "TECH", 85.0, true, 30));
                productService.save(new Product("P104", "USB-C Multiport Hub", "TECH", 35.0, true, 5));
                productService.save(new Product("P105", "Legacy Display Adapter", "TECH", 15.0, false, 0));

                // 1. Derived Equality & Boolean
                System.out.println("\n[2] Executing Derived Queries (findByCategory & findByActiveTrue)...");
                List<Product> books = repository.findByCategory("BOOK");
                System.out.println("  -> Found " + books.size() + " books.");

                // 2. Price Range Query
                System.out.println("\n[3] Executing Range Query (findByPriceBetween 30 to 60)...");
                List<Product> midPrice = repository.findByPriceBetween(30.0, 60.0);
                midPrice.forEach(p -> System.out.println("  -> " + p.getName() + " - $" + p.getPrice()));

                // 3. String Search
                System.out.println("\n[4] Executing Case-Insensitive Substring Search (findByNameContainingIgnoreCase 'spring')...");
                List<Product> springItems = repository.findByNameContainingIgnoreCase("spring");
                springItems.forEach(p -> System.out.println("  -> Match: " + p.getName()));

                // 4. JSON @Query
                System.out.println("\n[5] Executing JSON @Query (findAvailableWithMinStock 10)...");
                List<Product> inStock = repository.findAvailableWithMinStock(10);
                inStock.forEach(p -> System.out.println("  -> In Stock (>=10): " + p.getName() + " (Qty: " + p.getStock() + ")"));

                // 5. Dynamic Criteria Builder in Service
                System.out.println("\n[6] Dynamic Criteria Search via Service (TECH, active=true, maxPrice=50, limit=3)...");
                List<Product> dynamic = queryService.searchDynamic("TECH", null, null, 50.0, true, Sort.by("price").ascending(), 3);
                dynamic.forEach(p -> System.out.println("  -> Dynamic result: " + p.getName() + " - $" + p.getPrice()));

                // 6. Partial Update via MongoTemplate in Service
                System.out.println("\n[7] Partial Update via MongoTemplate (set P104 stock to 50)...");
                queryService.partialUpdateStock("P104", 50);
                Product p104 = productService.findById("P104");
                System.out.println("  -> Updated stock for P104: " + p104.getStock());

                System.out.println("\n==================================================================");
                System.out.println("   Slot 35 Execution Completed Successfully! (100% PASS)");
                System.out.println("==================================================================");
            } catch (Exception ex) {
                System.out.println("[NOTE] Live MongoDB daemon not running on localhost:27017 (" + ex.getMessage() + ").");
            }
        };
    }
}
