package com.vladimir.rpp_lab_3_1.database;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "students")
public class StudentEntity {

    @PrimaryKey(autoGenerate = true)
    public int id;

    public String fullName;

    public Long addTime;

}
