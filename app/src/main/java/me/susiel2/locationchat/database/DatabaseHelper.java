package me.susiel2.locationchat.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "bounce.db";
    public static final String TABLE_ONE_NAME = "users";
    public static final String TABLE_TWO_NAME = "groups";
    public static final String TABLE_THREE_NAME = "messages";
    public static final String TABLE_FOUR_NAME = "user_groups";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
        SQLiteDatabase db = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("create table " + TABLE_ONE_NAME + " (USER_ID TEXT PRIMARY KEY, NAME TEXT, PHONE_NUMBER INTEGER, PASSWORD TEXT, CREATED_AT TEXT, LOCATION TEXT)");
        sqLiteDatabase.execSQL("create table " + TABLE_TWO_NAME + "(GROUP_ID TEXT PRIMARY KEY, NAME TEXT, DESCRIPTION TEXT, PHOTO BLOB, CREATED_AT TEXT, CREATED_BY TEXT, LOCATION TEXT, CATEGORY TEXT, FOREIGN KEY(CREATED_BY) REFERENCES users(USER_ID))");
        sqLiteDatabase.execSQL("create table " + TABLE_THREE_NAME + "(MESSAGE_ID TEXT PRIMARY KEY, CONTENT TEXT, CREATED_AT TEXT, ATTACHMENT BLOB, CREATED_BY TEXT, GROUP_ID TEXT, FOREIGN KEY(CREATED_BY) REFERENCES user(USER_ID), FOREIGN KEY(GROUP_ID) REFERENCES groups(GROUP_ID))");
        sqLiteDatabase.execSQL("create table " + TABLE_FOUR_NAME + "(NOTIFICATIONS INTEGER, READ INTEGER, USER_ID TEXT, GROUP_ID TEXT, FOREIGN KEY(USER_ID) REFERENCES users(USER_ID), FOREIGN KEY(GROUP_ID) REFERENCES groups(GROUP_ID))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int j) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_ONE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_TWO_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_THREE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_FOUR_NAME);

        onCreate(sqLiteDatabase);
    }
}
