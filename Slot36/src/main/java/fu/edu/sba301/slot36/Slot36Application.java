package fu.edu.sba301.slot36;

import fu.edu.sba301.slot36.model.Product;
import fu.edu.sba301.slot36.service.IProductService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class Slot36Application {

    public static void main(String[] args) {
        SpringApplication.run(Slot36Application.class, args);
    }

    @Bean
    public CommandLineRunner initData(IProductService productService) {
        return args -> {
            try {
                if (productService.findAll().isEmpty()) {
                    productService.save(new Product("P-01", "Mechanical Keyboard", "ACCESSORY", 79.9, true));
                    productService.save(new Product("P-02", "USB-C Multiport Hub", "ACCESSORY", 45.0, true));
                    productService.save(new Product("P-03", "UltraWide 34 Monitor", "DISPLAY", 399.0, true));
                    System.out.println("[Slot 36] Initialized 3 sample products in MongoDB.");
                }
            } catch (Exception ex) {
                System.out.println("[Slot 36] MongoDB server not active on localhost:27017: " + ex.getMessage());
            }
        };
    }
}
