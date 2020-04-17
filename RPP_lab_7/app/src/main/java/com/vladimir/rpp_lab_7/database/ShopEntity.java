package com.vladimir.rpp_lab_7.database;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "shop")
public class ShopEntity {

    @PrimaryKey(autoGenerate = true)
    public int id;

    public String name;
    public int price;
    public int quantity;
    public int image;

    public ShopEntity(String name, int price, int quantity, int image) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.image = image;
    }
}
