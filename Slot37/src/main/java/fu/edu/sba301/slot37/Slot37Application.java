package fu.edu.sba301.slot37;

import fu.edu.sba301.slot37.model.Product;
import fu.edu.sba301.slot37.service.IProductService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class Slot37Application extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(Slot37Application.class, args);
    }

    @Bean
    public CommandLineRunner initData(IProductService productService) {
        return args -> {
            try {
                if (productService.count() == 0) {
                    productService.save(new Product("JSP-01", "Mechanical Keyboard", "ACCESSORY", 89.9, true));
                    productService.save(new Product("JSP-02", "USB-C Multiport Hub", "ACCESSORY", 45.0, true));
                    productService.save(new Product("JSP-03", "Curved Gaming Monitor", "DISPLAY", 320.0, true));
                    System.out.println("[Slot 37] Initialized 3 sample products in MongoDB.");
                }
            } catch (Exception ex) {
                System.out.println("[Slot 37] MongoDB server not active on localhost:27017: " + ex.getMessage());
            }
        };
    }
}
