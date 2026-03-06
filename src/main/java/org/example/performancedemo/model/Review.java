package org.example.performancedemo.model;

import jakarta.persistence.Index;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.UUID;


@Entity
@Table(name = "reviews", indexes = {
        @Index(name = "idx_reviews_product_id", columnList = "productId")
})
@Document(collection = "reviews")
public class Review {

    @Id
    private String id;
    private String productId;
    private int rating;
    private String text;
    private LocalDateTime createdAt;

    public Review() {
    }

    public Review(String productId, int rating, String text, LocalDateTime createdAt) {
        this.id = UUID.randomUUID().toString();
        this.productId = productId;
        this.rating = rating;
        this.text = text;
        this.createdAt = createdAt;
    }

    public String getId() {
        return id;
    }

    public String getProductId() {
        return productId;
    }

    public int getRating() {
        return rating;
    }

    public String getText() {
        return text;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}