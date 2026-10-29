package fu.edu.sba301.slot36;

import fu.edu.sba301.slot36.controller.GlobalMvcExceptionHandler;
import fu.edu.sba301.slot36.controller.ProductController;
import fu.edu.sba301.slot36.exception.ProductNotFoundException;
import fu.edu.sba301.slot36.model.Product;
import fu.edu.sba301.slot36.service.IProductService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductController.class)
@Import(GlobalMvcExceptionHandler.class)
class ProductControllerMvcTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IProductService service;

    @Test
    void testGetProductsListRendersView() throws Exception {
        when(service.search(null)).thenReturn(List.of(
                new Product("P1", "Keyboard", "ACCESSORY", 50.0, true)
        ));

        mockMvc.perform(get("/products"))
                .andExpect(status().isOk())
                .andExpect(view().name("products/list"))
                .andExpect(model().attributeExists("products"));
    }

    @Test
    void testShowCreateForm() throws Exception {
        mockMvc.perform(get("/products/create"))
                .andExpect(status().isOk())
                .andExpect(view().name("products/form"))
                .andExpect(model().attributeExists("product"));
    }

    @Test
    void testSaveValidProductRedirectsWithFlashMessage() throws Exception {
        when(service.save(any(Product.class))).thenReturn(new Product("P1", "Mouse", "ACCESSORY", 25.0, true));

        mockMvc.perform(post("/products/save")
                        .param("name", "Mouse")
                        .param("category", "ACCESSORY")
                        .param("price", "25.00")
                        .param("active", "true"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/products"))
                .andExpect(flash().attributeExists("successMessage"));

        verify(service, times(1)).save(any(Product.class));
    }

    @Test
    void testSaveInvalidProductReturnsFormWithErrors() throws Exception {
        mockMvc.perform(post("/products/save")
                        .param("name", "") // Blank name triggers validation error
                        .param("category", "")
                        .param("price", "-10.0"))
                .andExpect(status().isOk())
                .andExpect(view().name("products/form"))
                .andExpect(model().hasErrors());

        verify(service, never()).save(any(Product.class));
    }

    @Test
    void testEditExistingProduct() throws Exception {
        Product p = new Product("P1", "Webcam", "ACCESSORY", 65.0, true);
        when(service.findById("P1")).thenReturn(p);

        mockMvc.perform(get("/products/edit/P1"))
                .andExpect(status().isOk())
                .andExpect(view().name("products/form"))
                .andExpect(model().attribute("product", p));
    }

    @Test
    void testEditNotFoundProductTriggersExceptionHandler() throws Exception {
        when(service.findById("P999")).thenThrow(new ProductNotFoundException("Product not found with ID: P999"));

        mockMvc.perform(get("/products/edit/P999"))
                .andExpect(status().isOk())
                .andExpect(view().name("error/not-found"))
                .andExpect(model().attributeExists("errorMessage"));
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
