package com.vladimir.rpp_lab_7.database;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface ShopDao {

    @Query("SELECT * FROM shop")
    List<ShopEntity> getAll();

    @Query("SELECT * FROM shop WHERE quantity > 0")
    List<ShopEntity> getAllInStock();

    @Query("UPDATE shop SET quantity = quantity - 1 WHERE id = :id")
    void buyProductById(int id);

    @Insert
    void insert(ShopEntity product);

    @Update
    void update(ShopEntity product);

    @Delete
    void delete(ShopEntity product);

}
