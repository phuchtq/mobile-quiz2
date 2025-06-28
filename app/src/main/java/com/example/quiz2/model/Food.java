package com.example.quiz2.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "foods")
public class Food {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String name;
    private String description;
    private double price;
    private String imageName;

    public Food(String name, String description, double price, String imageName) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.imageName = imageName;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public String getImageName() { return imageName; }
    public void setImageName(String imageName) { this.imageName = imageName; }
}