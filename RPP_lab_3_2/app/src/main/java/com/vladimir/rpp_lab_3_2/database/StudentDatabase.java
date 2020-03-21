package com.vladimir.rpp_lab_3_2.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = {StudentEntity.class}, version = 2)
public abstract class StudentDatabase extends RoomDatabase {
    public abstract StudentDao studentDao();

    public static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(final SupportSQLiteDatabase database) {
            database.execSQL("CREATE TABLE students_tmp (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, addTime INTEGER)");
            database.execSQL("INSERT INTO students_tmp (id, addTime) SELECT id, addTime FROM students");
            database.execSQL("DROP TABLE students");
            database.execSQL("ALTER TABLE students_tmp RENAME TO students");
            database.execSQL("ALTER TABLE students ADD COLUMN first_name TEXT");
            database.execSQL("ALTER TABLE students ADD COLUMN mid_name TEXT");
            database.execSQL("ALTER TABLE students ADD COLUMN last_name TEXT");
        }
    };
}