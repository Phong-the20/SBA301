package fu.edu.sba301.slot37.controller;

import fu.edu.sba301.slot37.service.IProductService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    private final IProductService productService;

    public HomeController(IProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("pageTitle", "SBA301 - Slot 37 Mongo MVC & JSP");
        model.addAttribute("productCount", productService.count());
        model.addAttribute("activeCount", productService.countActive());
        return "home";
    }
}
