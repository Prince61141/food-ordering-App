package com.example.foodorderingapp.homepage;

public class Product {
    public int id;
    public String name,brand;
    public double price;
    public float rating;
    public int quantity=0;
    public String image;
    public String detail;

    public Product(int id, String name, double price, String brand, float rating, String image, String detail) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.brand = brand;
        this.rating = rating;
        this.image = image;
        this.detail = detail;
    }

}
