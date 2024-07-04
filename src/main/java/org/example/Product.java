package org.example;

public abstract class Product {
    private String name;
    private double price;
    private String details;

    public Product(String name, double price, String details) {
        this.name = name;
        this.price = price;
        this.details = details;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public String getDetails() {
        return details;
    }
}
