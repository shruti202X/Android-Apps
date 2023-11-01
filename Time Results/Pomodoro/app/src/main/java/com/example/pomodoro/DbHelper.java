package com.example.pomodoro;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.content.Context;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

public class DbHelper extends SQLiteOpenHelper {

    public static final class MyContract {
        // To prevent someone from accidentally instantiating the contract class,
        // make the constructor private.
        private MyContract() {}

        /* Inner class that defines the table contents */
        public static class Memo implements BaseColumns {
            public static final String TABLE_NAME = "MemoTable";
            public static final String COLUMN_1 = "MemoTime";
            public static final String COLUMN_2 = "MemoText";
            public static final String COLUMN_3 = "SessionNumber";
            public static final String COLUMN_4 = "WorkType";
            public static final String COLUMN_5 = "PomodoroTimeElapsed";
            public static final String COLUMN_6 = "BreakTimeElapsed";
        }

        public static class Plan implements BaseColumns {
            public static final String TABLE_NAME = "PlanTable";
            public static final String COLUMN_TITLE = "PlanTitle";
            public static final String COLUMN_WORK_TIME = "PlanWorkTime";
            public static final String COLUMN_BREAK_TIME = "PlanBreakTime";
            public static final String COLUMN_PRIORITY = "PlanPriority";
            public static final String COLUMN_TIME = "PlanTime";
            public static final String COLUMN_TAGS = "PlanTags";
        }
        public static class Tag implements BaseColumns {
            public static final String TABLE_NAME = "TagsTable";
            public static final String COLUMN_TAG = "TagName";
        }
        public static class Session implements BaseColumns {
            public static final String TABLE_NAME = "SessionTable";
            public static final String COLUMN_PLAN_ID = "PlanID";
            public static final String COLUMN_SESSION_TYPE = "SessionType";
            public static final String COLUMN_TIME_ELAPSED = "TimeElapsed";
        }
    }

    private static final String CREATE_TABLE_MEMO =
            "CREATE TABLE " + MyContract.Memo.TABLE_NAME + " (" +
                    MyContract.Memo._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    MyContract.Memo.COLUMN_1 + " DATETIME DEFAULT CURRENT_TIMESTAMP, " +
                    MyContract.Memo.COLUMN_2 + " TEXT, " +
                    MyContract.Memo.COLUMN_3 + " INTEGER, " +
                    MyContract.Memo.COLUMN_4 + " INTEGER, " +
                    MyContract.Memo.COLUMN_5 + " INTEGER, " +
                    MyContract.Memo.COLUMN_6 + " INTEGER)";

    private static final String CREATE_TABLE_PLAN =
            "CREATE TABLE " + MyContract.Plan.TABLE_NAME + " (" +
                    MyContract.Plan._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    MyContract.Plan.COLUMN_TITLE + " TEXT," +
                    MyContract.Plan.COLUMN_WORK_TIME + " INTEGER," +
                    MyContract.Plan.COLUMN_BREAK_TIME + " INTEGER," +
                    MyContract.Plan.COLUMN_PRIORITY + " TEXT," +
                    MyContract.Plan.COLUMN_TIME + " TEXT," +
                    MyContract.Plan.COLUMN_TAGS + " TEXT" +
                    ")";

    private static final String CREATE_TABLE_TAG =
            "CREATE TABLE " + MyContract.Tag.TABLE_NAME + " (" +
                    MyContract.Tag._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    MyContract.Tag.COLUMN_TAG + " TEXT" +
                    ")";

    private static final String CREATE_TABLE_SESSION =
            "CREATE TABLE " + MyContract.Tag.TABLE_NAME + " (" +
                    MyContract.Session._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    MyContract.Session.COLUMN_PLAN_ID + " INTEGER" +
                    MyContract.Session.COLUMN_SESSION_TYPE + " TEXT" +
                    MyContract.Session.COLUMN_TIME_ELAPSED + " INTEGER" +
                    ")";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + MyContract.Memo.TABLE_NAME;

    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Data.db";

    public DbHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(@NonNull SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_PLAN);
        db.execSQL(CREATE_TABLE_TAG);
        db.execSQL(CREATE_TABLE_MEMO);
        db.execSQL(CREATE_TABLE_SESSION);
    }
    public void onUpgrade(@NonNull SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        String drop = "DROP TABLE IF EXISTS ";
        db.execSQL(drop + MyContract.Plan.TABLE_NAME);
        db.execSQL(drop + MyContract.Tag.TABLE_NAME);
        db.execSQL(drop + MyContract.Memo.TABLE_NAME);
        db.execSQL(drop + MyContract.Session.TABLE_NAME);

        //Create tables again
        onCreate(db);
    }
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}