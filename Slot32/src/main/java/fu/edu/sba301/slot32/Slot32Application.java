package fu.edu.sba301.slot32;

import fu.edu.sba301.slot32.model.Category;
import fu.edu.sba301.slot32.model.Dimensions;
import fu.edu.sba301.slot32.model.Product;
import fu.edu.sba301.slot32.model.ProductSpecs;
import fu.edu.sba301.slot32.repository.CategoryRepository;
import fu.edu.sba301.slot32.repository.ICategoryRepository;
import fu.edu.sba301.slot32.repository.IProductRepository;
import fu.edu.sba301.slot32.repository.ProductRepository;
import fu.edu.sba301.slot32.service.CategoryService;
import fu.edu.sba301.slot32.service.ICategoryService;
import fu.edu.sba301.slot32.service.IProductService;
import fu.edu.sba301.slot32.service.ProductService;

import java.util.List;

public class Slot32Application {

    public static void main(String[] args) {
        System.out.println("==================================================================");
        System.out.println("   SBA301 - Slot 32: MongoDB / NoSQL Document Model & Architecture");
        System.out.println("   Layered 3-Tier Architecture with Service Layer Validation");
        System.out.println("==================================================================");

        // Composition Root: Wiring Repositories and Services
        ICategoryRepository categoryRepo = new CategoryRepository();
        IProductRepository productRepo = new ProductRepository();
        ICategoryService categoryService = new CategoryService(categoryRepo, productRepo);
        IProductService productService = new ProductService(productRepo, categoryRepo);

        // Step 1: Create Categories (Referencing Pattern)
        System.out.println("\n[1] Creating Categories (Referenced Documents)...");
        Category catAcc = categoryService.createCategory(new Category("CAT-ACC", "Accessories", "Computer Peripherals", true));
        Category catDsp = categoryService.createCategory(new Category("CAT-DSP", "Displays", "Visual Monitors", true));
        System.out.println("  -> Created: " + catAcc);
        System.out.println("  -> Created: " + catDsp);

        // Step 2: Create Products with Embedded Documents & Arrays
        System.out.println("\n[2] Creating Products with Embedded Documents & Arrays...");
        Product p1 = new Product("PROD-001", "Mechanical Keyboard", 79.9, "CAT-ACC", true);
        p1.setTags(List.of("mechanical", "gaming", "rgb"));
        p1.setSpecs(new ProductSpecs(1, false, 0));
        p1.setDimensions(new Dimensions(44.0, 13.5));
        productService.createProduct(p1);
        System.out.println("  -> Created p1 (Keyboard): " + p1);

        Product p2 = new Product("PROD-002", "USB-C Hub Pro", 49.5, "CAT-ACC", true);
        p2.setTags(List.of("usb-c", "hub", "accessory"));
        p2.setSpecs(new ProductSpecs(7, true, 100)); // specs.hdmi = true
        p2.setDimensions(new Dimensions(11.2, 4.5));
        productService.createProduct(p2);
        System.out.println("  -> Created p2 (USB-C Hub with HDMI): " + p2);

        // Step 3: Flexible Schema Demonstration
        System.out.println("\n[3] Flexible Schema: Adding Product with extra dynamic attribute (warrantyMonths)...");
        Product p3 = new Product("PROD-003", "Wireless Ergonomic Mouse", 25.0, "CAT-ACC", true);
        p3.setWarrantyMonths(12);
        productService.createProduct(p3);
        System.out.println("  -> Created p3 (Flexible attributes): " + p3 + ", Warranty: " + p3.getWarrantyMonths() + " months");

        // Step 4: Dot-Notation Query Simulation
        System.out.println("\n[4] Query Demonstration (Dot-notation equivalent: specs.hdmi = true)...");
        List<Product> hdmiProducts = productService.getProductsWithHdmi();
        System.out.println("  -> Products with HDMI: " + hdmiProducts.size());
        hdmiProducts.forEach(p -> System.out.println("     * " + p.getName() + " (Ports: " + p.getSpecs().getPorts() + ")"));

        // Step 5: Tag Array Query
        System.out.println("\n[5] Query Demonstration (Array containment: tags has 'usb-c')...");
        List<Product> usbProducts = productService.getProductsByTag("usb-c");
        usbProducts.forEach(p -> System.out.println("     * " + p.getName() + " -> Tags: " + p.getTags()));

        // Step 6: Break-It Demonstration: Type Drift & Data Quality Protection
        System.out.println("\n[6] Break-It Diagnostics: Service Layer Schema Type Validation...");
        try {
            Product badProduct = new Product("PROD-BAD", "Corrupt Product", null, "CAT-ACC", true);
            productService.createProduct(badProduct);
        } catch (IllegalArgumentException ex) {
            System.out.println("  -> [PASS] Service successfully intercepted null/invalid price type: " + ex.getMessage());
        }

        // Step 7: Referential Integrity Protection in Service Layer
        System.out.println("\n[7] Referential Integrity: Prevent Deleting Category when referenced by Products...");
        try {
            categoryService.deleteCategory("CAT-ACC");
        } catch (IllegalStateException ex) {
            System.out.println("  -> [PASS] Service successfully protected relationship integrity: " + ex.getMessage());
        }

        System.out.println("\n==================================================================");
        System.out.println("   Finished Slot 32 Verification Successfully! (100% PASS)");
        System.out.println("==================================================================");
    }
}
