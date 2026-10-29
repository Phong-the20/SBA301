package fu.edu.sba301.slot36.controller;

import fu.edu.sba301.slot36.exception.ProductNotFoundException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalMvcExceptionHandler {

    @ExceptionHandler(ProductNotFoundException.class)
    public String handleNotFound(ProductNotFoundException ex, Model model) {
        model.addAttribute("errorMessage", ex.getMessage());
        return "error/not-found";
    }
}
