package fu.edu.sba301.slot36.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("products")
public class Product {

    @Id
    private String id;

    @NotBlank(message = "Product name is required")
    private String name;

    @NotBlank(message = "Category is required")
    private String category;

    @PositiveOrZero(message = "Price must be positive or zero")
    private double price;

    private boolean active = true;

    public Product() {}

    public Product(String id, String name, String category, double price, boolean active) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.price = price;
        this.active = active;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }

    @Override
    public String toString() {
        return "Product{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", category='" + category + '\'' +
                ", price=" + price +
                ", active=" + active +
                '}';
    }
}
