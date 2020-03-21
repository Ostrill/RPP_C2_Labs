package com.vladimir.rpp_lab_3_2.database;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface StudentDao {

    @Query("SELECT * FROM students")
    List<StudentEntity> getAll();

    @Insert
    void insert(StudentEntity student);

    @Update
    void update(StudentEntity student);

}
