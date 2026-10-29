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
import org.junit.jupiter.api.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class Slot32ServiceIntegrationTests {

    private ICategoryRepository categoryRepo;
    private IProductRepository productRepo;
    private ICategoryService categoryService;
    private IProductService productService;

    @BeforeEach
    void setUp() {
        categoryRepo = new CategoryRepository();
        productRepo = new ProductRepository();
        categoryService = new CategoryService(categoryRepo, productRepo);
        productService = new ProductService(productRepo, categoryRepo);
    }

    @Test
    @Order(1)
    void g01_createCategorySuccess() {
        Category cat = new Category("CAT-ACC", "Accessories", "Peripherals", true);
        Category created = categoryService.createCategory(cat);
        assertNotNull(created.getId());
        assertEquals("Accessories", created.getName());
    }

    @Test
    @Order(2)
    void g02_createProductWithEmbeddedSpecsAndDimensions() {
        categoryService.createCategory(new Category("CAT-ACC", "Accessories", "Peripherals", true));

        Product p = new Product("PROD-001", "Keyboard", 59.0, "CAT-ACC", true);
        p.setSpecs(new ProductSpecs(1, false, 0));
        p.setDimensions(new Dimensions(40.0, 12.0));
        p.setTags(List.of("usb", "input"));

        Product saved = productService.createProduct(p);
        assertNotNull(saved);
        assertNotNull(saved.getSpecs());
        assertNotNull(saved.getDimensions());
        assertEquals(2, saved.getTags().size());
    }

    @Test
    @Order(3)
    void g03_queryEmbeddedFieldDotNotation() {
        categoryService.createCategory(new Category("CAT-ACC", "Accessories", "Peripherals", true));

        Product p1 = new Product("PROD-001", "Mouse", 20.0, "CAT-ACC", true);
        p1.setSpecs(new ProductSpecs(0, false, 0));

        Product p2 = new Product("PROD-002", "Hub", 45.0, "CAT-ACC", true);
        p2.setSpecs(new ProductSpecs(4, true, 60)); // has HDMI

        productService.createProduct(p1);
        productService.createProduct(p2);

        List<Product> hdmiProducts = productService.getProductsWithHdmi();
        assertEquals(1, hdmiProducts.size());
        assertEquals("Hub", hdmiProducts.get(0).getName());
    }

    @Test
    @Order(4)
    void g04_serviceRejectsInvalidPriceTypeOrNegative() {
        categoryService.createCategory(new Category("CAT-ACC", "Accessories", "Peripherals", true));

        Product badPrice = new Product("PROD-BAD", "Test", -10.0, "CAT-ACC", true);
        assertThrows(IllegalArgumentException.class, () -> productService.createProduct(badPrice));

        Product nullPrice = new Product("PROD-BAD2", "Test2", null, "CAT-ACC", true);
        assertThrows(IllegalArgumentException.class, () -> productService.createProduct(nullPrice));
    }

    @Test
    @Order(5)
    void g05_serviceRejectsDanglingCategoryReference() {
        Product p = new Product("PROD-DANGLING", "Orphan", 20.0, "CAT-NONEXISTENT", true);
        assertThrows(IllegalArgumentException.class, () -> productService.createProduct(p));
    }

    @Test
    @Order(6)
    void g06_serviceProtectsReferentialIntegrityOnCategoryDelete() {
        categoryService.createCategory(new Category("CAT-ACC", "Accessories", "Peripherals", true));
        productService.createProduct(new Product("PROD-001", "Keyboard", 50.0, "CAT-ACC", true));

        assertThrows(IllegalStateException.class, () -> categoryService.deleteCategory("CAT-ACC"));
    }
}
