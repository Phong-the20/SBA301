package fu.edu.sba301.slot32.model;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class Product {
    private String id;
    private String name;
    private Double price;
    private String categoryId;
    private boolean active;
    private List<String> tags = new ArrayList<>();
    private ProductSpecs specs;
    private Dimensions dimensions;
    private Integer warrantyMonths;
    private Instant createdAt;

    public Product() {}

    public Product(String id, String name, Double price, String categoryId, boolean active) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.categoryId = categoryId;
        this.active = active;
        this.createdAt = Instant.now();
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Double getPrice() { return price; }
    public void setPrice(Double price) { this.price = price; }

    public String getCategoryId() { return categoryId; }
    public void setCategoryId(String categoryId) { this.categoryId = categoryId; }

    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }

    public List<String> getTags() { return tags; }
    public void setTags(List<String> tags) { this.tags = tags; }

    public ProductSpecs getSpecs() { return specs; }
    public void setSpecs(ProductSpecs specs) { this.specs = specs; }

    public Dimensions getDimensions() { return dimensions; }
    public void setDimensions(Dimensions dimensions) { this.dimensions = dimensions; }

    public Integer getWarrantyMonths() { return warrantyMonths; }
    public void setWarrantyMonths(Integer warrantyMonths) { this.warrantyMonths = warrantyMonths; }

    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }

    @Override
    public String toString() {
        return "Product{id='" + id + "', name='" + name + "', price=" + price +
                ", catId='" + categoryId + "', specs=" + specs + ", dims=" + dimensions + "}";
    }
}
