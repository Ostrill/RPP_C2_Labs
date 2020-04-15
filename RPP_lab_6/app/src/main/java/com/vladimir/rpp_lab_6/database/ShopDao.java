package com.vladimir.rpp_lab_6.database;

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

    @Insert
    void insert(ShopEntity product);

    @Update
    void update(ShopEntity product);

    @Delete
    void delete(ShopEntity product);

}
