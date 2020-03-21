package com.vladimir.rpp_lab_3_2.utils;

import android.content.Context;
import android.text.format.Time;

import com.vladimir.rpp_lab_3_2.database.StudentDatabase;
import com.vladimir.rpp_lab_3_2.database.StudentEntity;

import java.util.List;
import java.util.StringTokenizer;

import androidx.room.Room;

public class DbSingleton {
    private static DbSingleton instance = null;
    private StudentDatabase database = null;

    public static DbSingleton getInstance(Context context) {
        if (instance == null) {
            instance = new DbSingleton(context);
        }
        return instance;
    }

    public StudentDatabase getDatabase() {
        return database;
    }

    private DbSingleton(Context context) {
        database = Room.databaseBuilder(context,
                StudentDatabase.class, "database")
                .addMigrations(StudentDatabase.MIGRATION_1_2)
                .allowMainThreadQueries()
                .build();
    }

    public void insertRandomStudent() {
        final Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                insertRandom();
            }
        });
        thread.start();
    }

    private void insertRandom() {
        final StudentEntity student = new StudentEntity();

        Time time = new Time();
        time.setToNow();
        student.addTime = time.toMillis(true);

        int index = (int) (Math.random() * (Constants.names.length - 1));
        StringTokenizer tokenizer = new StringTokenizer(Constants.names[index], " ");

        student.firstName = tokenizer.nextToken();
        student.midName   = tokenizer.nextToken();
        student.lastName  = tokenizer.nextToken();


        instance
                .database
                .studentDao()
                .insert(student);
    }

    public List<StudentEntity> getStudents() {
        return instance
                .database
                .studentDao()
                .getAll();
    }

    public void refreshDB() {
        final Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                instance.database.clearAllTables();
                for (int i = 0; i < 5; ++i) insertRandom();
            }
        });
        thread.start();
    }

    public void changeLast() {
        final Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                List<StudentEntity> list = getStudents();
                StudentEntity student = list.get(list.size()-1);

                student.firstName = "Иванов";
                student.midName = "Иван";
                student.lastName = "Иванович";

                instance.getDatabase().studentDao().update(student);
            }
        });
        thread.start();
    }
}
