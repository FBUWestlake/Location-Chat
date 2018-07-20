package me.susiel2.locationchat.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "bounce.db";
    public static final String TABLE_ONE_NAME = "users_table";
    public static final String TABLE_TWO_NAME = "groups_table";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
        SQLiteDatabase db = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("create table " + TABLE_ONE_NAME + " (NAME TEXT PRIMARY KEY, PHONE_NUMBER INTEGER, EMAIL TEXT, PASSWORD TEXT, GROUPS TEXT, CATEGORIES TEXT)");
        sqLiteDatabase.execSQL("create table " + TABLE_TWO_NAME + "(NAME TEXT PRIMARY KEY, DESCRIPTION TEXT, NUMBER_OF_MEMBERS INTEGER, CATEGORY TEXT, CREATED_AT TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int j) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_ONE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_TWO_NAME);
        onCreate(sqLiteDatabase);
    }
}
