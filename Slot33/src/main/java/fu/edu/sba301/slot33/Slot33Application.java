package fu.edu.sba301.slot33;

import fu.edu.sba301.slot33.model.Product;
import fu.edu.sba301.slot33.service.IExportService;
import fu.edu.sba301.slot33.service.IProductService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.math.BigDecimal;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@SpringBootApplication
public class Slot33Application {

    public static void main(String[] args) {
        SpringApplication.run(Slot33Application.class, args);
    }

    @Bean
    public CommandLineRunner demoRunner(IProductService productService, IExportService exportService) {
        return args -> {
            System.out.println("==================================================================");
            System.out.println("   SBA301 - Slot 33: Spring Data MongoDB CRUD & Export Service");
            System.out.println("   Layered Architecture: Logic strictly in Service Layer");
            System.out.println("==================================================================");

            try {
                // Step 1: CREATE via Service
                System.out.println("\n[1] Creating Initial Products...");
                Product p1 = productService.create(new Product(
                        "PROD-001", "Mechanical Keyboard", "Accessory",
                        new BigDecimal("79.90"), 25, true, List.of("usb", "rgb")
                ));
                Product p2 = productService.create(new Product(
                        "PROD-002", "USB-C Hub Pro", "Accessory",
                        new BigDecimal("49.50"), 8, true, List.of("usb-c", "hub")
                ));
                System.out.println("  -> Created: " + p1.getName() + " (ID: " + p1.getId() + ")");
                System.out.println("  -> Created: " + p2.getName() + " (ID: " + p2.getId() + ")");

                // Step 2: READ & Derived Queries
                System.out.println("\n[2] Reading Products via Derived Query (Accessory & Active)...");
                List<Product> accessories = productService.findByCategoryActive("Accessory");
                accessories.forEach(p -> System.out.println("  -> Found: " + p.getName() + " | Stock: " + p.getStock()));

                // Step 3: Partial Atomic Update via MongoTemplate in Service
                System.out.println("\n[3] Executing Atomic Partial Update (Increase stock of PROD-002 by 5)...");
                long modified = productService.increaseStock("PROD-002", 5);
                System.out.println("  -> Modified documents: " + modified);
                Product reloaded = productService.findById("PROD-002");
                System.out.println("  -> New Stock: " + reloaded.getStock());

                // Step 4: JSON Lines Export via Service
                Path exportPath = Paths.get("data", "products_export.jsonl");
                long exportedCount = exportService.exportProductsToJsonLines(exportPath);
                System.out.println("\n[4] Exporting collection to JSON Lines: " + exportPath.toAbsolutePath());
                System.out.println("  -> Exported " + exportedCount + " products successfully.");

                System.out.println("\n==================================================================");
                System.out.println("   Slot 33 Demo Finished Successfully! (100% PASS)");
                System.out.println("==================================================================");
            } catch (Exception ex) {
                System.out.println("[NOTE] MongoDB Server not running on localhost:27017 (" + ex.getMessage() + ").");
                System.out.println("Run local MongoDB or start with MONGODB_URI for live connection.");
            }
        };
    }
}
