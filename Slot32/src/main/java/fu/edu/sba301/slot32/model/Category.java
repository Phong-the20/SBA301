package fu.edu.sba301.slot32.model;

import java.time.Instant;

public class Category {
    private String id;
    private String name;
    private String description;
    private boolean active;
    private Instant createdAt;

    public Category() {}

    public Category(String id, String name, String description, boolean active) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.active = active;
        this.createdAt = Instant.now();
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }

    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }

    @Override
    public String toString() {
        return "Category{id='" + id + "', name='" + name + "', active=" + active + "}";
    }
}
