package fu.edu.sba301.slot37;

import fu.edu.sba301.slot37.controller.HomeController;
import fu.edu.sba301.slot37.controller.ProductController;
import fu.edu.sba301.slot37.model.Product;
import fu.edu.sba301.slot37.service.IProductService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest({HomeController.class, ProductController.class})
class Slot37ControllerMvcTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IProductService service;

    @Test
    void testHomeControllerRendersDashboard() throws Exception {
        when(service.count()).thenReturn(10L);
        when(service.countActive()).thenReturn(8L);

        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("home"))
                .andExpect(model().attribute("pageTitle", "SBA301 - Slot 37 Mongo MVC & JSP"))
                .andExpect(model().attribute("productCount", 10L))
                .andExpect(model().attribute("activeCount", 8L));
    }

    @Test
    void testProductListRendersJspView() throws Exception {
        when(service.search(null)).thenReturn(List.of(
                new Product("P1", "Keyboard", "ACCESSORY", 50.0, true)
        ));

        mockMvc.perform(get("/products"))
                .andExpect(status().isOk())
                .andExpect(view().name("products/list"))
                .andExpect(model().attributeExists("products"));
    }

    @Test
    void testShowCreateFormContainsEmptyProductAndCategories() throws Exception {
        mockMvc.perform(get("/products/create"))
                .andExpect(status().isOk())
                .andExpect(view().name("products/form"))
                .andExpect(model().attributeExists("product"))
                .andExpect(model().attributeExists("categories"));
    }

    @Test
    void testSaveValidProductRedirectsWithFlash() throws Exception {
        when(service.save(any(Product.class))).thenReturn(new Product("P1", "Mouse", "ACCESSORY", 20.0, true));

        mockMvc.perform(post("/products/save")
                        .param("name", "Mouse")
                        .param("category", "ACCESSORY")
                        .param("price", "20.00")
                        .param("active", "true"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/products"))
                .andExpect(flash().attributeExists("successMessage"));

        verify(service, times(1)).save(any(Product.class));
    }

    @Test
    void testSaveInvalidProductReturnsFormWithBindingErrors() throws Exception {
        mockMvc.perform(post("/products/save")
                        .param("name", "")
                        .param("category", "")
                        .param("price", "-5.00"))
                .andExpect(status().isOk())
                .andExpect(view().name("products/form"))
                .andExpect(model().hasErrors())
                .andExpect(model().attributeExists("categories")); // Re-populated lookup

        verify(service, never()).save(any(Product.class));
    }

    @Test
    void testDeleteProductRedirects() throws Exception {
        doNothing().when(service).deleteById("P1");

        mockMvc.perform(post("/products/delete/P1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/products"))
                .andExpect(flash().attributeExists("successMessage"));

        verify(service, times(1)).deleteById("P1");
    }
}
