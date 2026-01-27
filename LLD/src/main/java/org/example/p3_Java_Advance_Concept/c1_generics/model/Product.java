package org.example.p3_Java_Advance_Concept.c1_generics.model;

import java.util.Objects;

/**
 * A simple product entity for demonstrating generics with domain objects.
 * <p>
 * This class represents a product in an e-commerce context and is used
 * in repository pattern demonstrations.
 * </p>
 *
 * @see org.example.p3_Java_Advance_Concept.c1_generics.contracts.IRepository
 */
public class Product {

    private final String id;
    private final String name;
    private final double price;
    private final String category;

    /**
     * Creates a new product.
     *
     * @param id       unique product identifier
     * @param name     product name
     * @param price    product price
     * @param category product category
     */
    public Product(String id, String name, double price, String category) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.category = category;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public String getCategory() {
        return category;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Product product = (Product) o;
        return Objects.equals(id, product.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Product{id='" + id + "', name='" + name + "', price=" + price + "}";
    }
}
