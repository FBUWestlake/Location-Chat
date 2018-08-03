package me.susiel2.locationchat.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.parse.ParseUser;

import java.util.List;

import me.susiel2.locationchat.model.Message;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "bounce.db";

    public static final String TABLE_ONE_NAME = "users";
    public static final String TABLE_TWO_NAME = "groups";
    public static final String TABLE_THREE_NAME = "messages";
    public static final String TABLE_FOUR_NAME = "user_groups";

    // User table items
    private static final String KEY_USER_ID = "objectId";
    private static final String KEY_USER_NAME = "name";
    private static final String KEY_USER_PHONE = "username";
    private static final String KEY_USER_PASSWORD = "password";
    private static final String KEY_USER_CREATEDAT = "createdAt";
    private static final String KEY_USER_LOCATION = "location";

    // Groups table items
    private static final String KEY_GROUP_ID = "objectId";
    private static final String KEY_GROUP_NAME = "name";
    private static final String KEY_GROUP_DESCRIPTION = "description";
    private static final String KEY_GROUP_IMAGE = "image";
    private static final String KEY_GROUP_CREATEDAT = "createdAt";
    private static final String KEY_GROUP_CREATEDBY = "createdBy";
    private static final String KEY_GROUP_LOCATION = "location";
    private static final String KEY_GROUP_CATEGORY = "category";

    // Messages table items
    private static final String KEY_MESSAGE_ID = "objectId";
    private static final String KEY_MESSAGE_CONTENT = "content";
    private static final String KEY_MESSAGE_CREATEDAT = "createdAt";
    private static final String KEY_MESSAGE_ATTACHMENT = "attachment";
    private static final String KEY_MESSAGE_CREATEDBY = "createdBy";
    private static final String KEY_MESSAGE_GROUPID = "groupId";

    // UserGroups table item


    private static DatabaseHelper sInstance;
    public DatabaseHelper(Context context){
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("create table " + TABLE_ONE_NAME + "(" + KEY_USER_ID + " TEXT PRIMARY KEY," + KEY_USER_NAME + " TEXT," + KEY_USER_PHONE + " INTEGER," + KEY_USER_PASSWORD + " TEXT," + KEY_USER_CREATEDAT + " TEXT," + KEY_USER_LOCATION + " TEXT" + ")");
        sqLiteDatabase.execSQL("create table " + TABLE_TWO_NAME + "(" + KEY_GROUP_ID + " TEXT PRIMARY KEY," + KEY_GROUP_NAME + " TEXT," + KEY_GROUP_DESCRIPTION + " TEXT," + KEY_GROUP_IMAGE + " BLOB," + KEY_GROUP_CREATEDAT + " TEXT, " + KEY_GROUP_CREATEDBY + " TEXT REFERENCES " + TABLE_ONE_NAME + "," + KEY_GROUP_LOCATION + " TEXT," + KEY_GROUP_CATEGORY + " TEXT" + ")");
        sqLiteDatabase.execSQL("create table " + TABLE_THREE_NAME + "(" + KEY_MESSAGE_ID + " TEXT PRIMARY KEY," + KEY_MESSAGE_CONTENT + " TEXT," + KEY_MESSAGE_CREATEDAT + " TEXT," + KEY_MESSAGE_ATTACHMENT + " BLOB," + KEY_MESSAGE_CREATEDBY + " TEXT REFERENCES " + TABLE_TWO_NAME + "," + KEY_MESSAGE_GROUPID + " TEXT REFERENCES " + TABLE_TWO_NAME + ")");
        sqLiteDatabase.execSQL("create table " + TABLE_FOUR_NAME + "(USERGROUP_ID TEXT PRIMARY KEY, NOTIFICATIONS INTEGER, READ INTEGER, USER_ID TEXT, GROUP_ID TEXT, FOREIGN KEY(USER_ID) REFERENCES users(USER_ID), FOREIGN KEY(GROUP_ID) REFERENCES groups(GROUP_ID))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int j) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_ONE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_TWO_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_THREE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_FOUR_NAME);

        onCreate(sqLiteDatabase);
    }

    public static synchronized DatabaseHelper getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new DatabaseHelper(context.getApplicationContext());
        }
        return sInstance;
    }

//    public SQLiteDatabase getDb() {
//        return db;
//    }

    public void addMessage(List<Message> messages) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        try {
//            ParseUser user = addOrUpdateUser(message.KEY_CREATED_BY);
            for (int i = 0; i < messages.size(); i++) {
                Message currentMessage = messages.get(i);
                ContentValues values = new ContentValues();
                values.put(KEY_MESSAGE_CREATEDBY, currentMessage.getCreatedBy().getObjectId());
                values.put(KEY_MESSAGE_CONTENT, currentMessage.getContent());
                values.put(KEY_MESSAGE_CREATEDAT, currentMessage.getCreatedAtString());
                values.put(KEY_MESSAGE_GROUPID, currentMessage.getChat().getObjectId());
                db.insert(TABLE_THREE_NAME, null, values);
            }
            db.setTransactionSuccessful();
        }
        finally{
            db.endTransaction();
        }
    }
//
//    public void readMessages() {
//        SQLiteDatabase db = getWritableDatabase();
//
//    }




}
