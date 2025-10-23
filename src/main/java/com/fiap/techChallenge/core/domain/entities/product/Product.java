package com.fiap.techChallenge.core.domain.entities.product;

import java.math.BigDecimal;
import java.util.UUID;

import com.fiap.techChallenge.core.domain.enums.Category;
import com.fiap.techChallenge.core.domain.enums.ProductStatus;

public class Product {

    private UUID id;

    private String name;

    private String description;

    private BigDecimal price;

    private Category category;

    private ProductStatus status;

    private String image;

    private Product(UUID id, String name, String description, BigDecimal price, Category category, ProductStatus status, String image) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.category = category;
        this.image = image;
        this.status = status;
    }

    public UUID getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return this.price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Category getCategory() {
        return this.category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public ProductStatus getStatus() {
        return this.status;
    }

    public void setStatus(ProductStatus status) {
        this.status = status;
    }

    public String getImage() {
        return this.image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public static Product build(UUID id, String name, String description, BigDecimal price, Category category, ProductStatus status, String image) {
        validate(name, description, price, category, image);
        return new Product(id, name, description, price, category, status, image);
    }

    private static void validate(String name, String description, BigDecimal price, Category category, String image) {

        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("O nome do produto deve ser preenchido");
        }
        if (description == null || description.trim().isEmpty()) {
            throw new IllegalArgumentException("A descrição do produto deve ser preenchida.");
        }
        if (price == null || price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("O preço do produto deve estar preenchido e não pode ser negativo.");
        }
        if (category == null) {
            throw new IllegalArgumentException("A categoria do produto deve ser preenchida.");
        }
        if (image == null || image.trim().isEmpty()) {
            throw new IllegalArgumentException("A imagem do produto deve ser informada.");
        }
    }
}
