package com.vladimir.rpp_lab_6.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {ShopEntity.class}, version = 1, exportSchema = false)
public abstract class ShopDatabase extends RoomDatabase {
    public abstract ShopDao shopDao();
}
