package com.vladimir.rpp_lab_3_2.database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "students")
public class StudentEntity {

    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "first_name") public String firstName;
    @ColumnInfo(name = "mid_name")   public String midName;
    @ColumnInfo(name = "last_name")  public String lastName;

    public Long addTime;

}
