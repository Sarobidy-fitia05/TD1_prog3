package org.example.prouct;

import java.time.LocalDateTime;

public class Product {
    private int id;
    private String name;
    private float price;
    private LocalDateTime creationDatetime;
    private Category category;

    public Product(int id, float price, LocalDateTime creationDatetime, Category category, String name) {
        this.id = id;
        this.price = price;
        this.creationDatetime = creationDatetime;
        this.category = category;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public float getPrice() {
        return price;
    }

    public LocalDateTime getCreationDatetime() {
        return creationDatetime;
    }

    public Category getCategory() {
        return category;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public void setCreationDatetime(LocalDateTime creationDatetime) {
        this.creationDatetime = creationDatetime;
    }

    public void setCategory(Category category) {
        this.category = category;
    }
}
