package fu.edu.sba301.slot36.controller;

import fu.edu.sba301.slot36.model.Product;
import fu.edu.sba301.slot36.service.IProductService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/products")
public class ProductController {

    private final IProductService service;

    public ProductController(IProductService service) {
        this.service = service;
    }

    @GetMapping
    public String list(@RequestParam(value = "keyword", required = false) String keyword, Model model) {
        model.addAttribute("products", service.search(keyword));
        model.addAttribute("keyword", keyword != null ? keyword : "");
        return "products/list";
    }

    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("product", new Product());
        return "products/form";
    }

    @PostMapping("/save")
    public String saveProduct(
            @Valid @ModelAttribute("product") Product product,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            return "products/form";
        }

        service.save(product);
        redirectAttributes.addFlashAttribute("successMessage", "Product saved successfully!");
        return "redirect:/products";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable String id, Model model) {
        Product existing = service.findById(id);
        model.addAttribute("product", existing);
        return "products/form";
    }

    @PostMapping("/delete/{id}")
    public String deleteProduct(@PathVariable String id, RedirectAttributes redirectAttributes) {
        service.deleteById(id);
        redirectAttributes.addFlashAttribute("successMessage", "Product deleted successfully!");
        return "redirect:/products";
    }
}
