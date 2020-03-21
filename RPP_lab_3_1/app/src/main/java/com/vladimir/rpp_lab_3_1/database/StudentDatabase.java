package com.vladimir.rpp_lab_3_1.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {StudentEntity.class}, version = 1)
public abstract class StudentDatabase extends RoomDatabase {
    public abstract StudentDao studentDao();
}