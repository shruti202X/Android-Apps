package com.example.random_number_inbetween;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.content.Context;

import androidx.annotation.NonNull;

public class ClickDbHelper extends SQLiteOpenHelper {

    public static final class MyContract {
        // To prevent someone from accidentally instantiating the contract class,
        // make the constructor private.
        private MyContract() {}

        /* Inner class that defines the table contents */
        public static class Click implements BaseColumns {
            public static final String TABLE_NAME = "Click";
            public static final String COLUMN_1 = "Time";
            public static final String COLUMN_2 = "Description";
        }
    }

    //https://www.sqlitetutorial.net/sqlite-date/
    //https://alvinalexander.com/android/sqlite-default-datetime-field-current-time-now/
    //https://www.sqlite.org/datatype3.html

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + MyContract.Click.TABLE_NAME + " (" +
                    MyContract.Click._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    MyContract.Click.COLUMN_1 + " DATETIME DEFAULT CURRENT_TIMESTAMP," +
                    MyContract.Click.COLUMN_2 + " TEXT)";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + MyContract.Click.TABLE_NAME;

    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Click.db";

    public ClickDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(@NonNull SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }
    public void onUpgrade(@NonNull SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}
