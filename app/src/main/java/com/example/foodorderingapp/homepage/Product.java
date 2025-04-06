package com.example.foodorderingapp.homepage;

public class Product {
    public int id;
    public String name,brand;
    public double price;
    public float rating;
    public int quantity=0;

    public Product(int id, String name, double price, String brand, float rating) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.brand = brand;
        this.rating = rating;
    }

}
