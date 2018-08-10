package me.susiel2.locationchat.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.gsm.SmsMessage;
import android.util.Log;

import com.parse.ParseUser;

import org.apache.commons.lang3.SerializationUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import me.susiel2.locationchat.model.Message;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "bounce.db";

    public static final String TABLE_ONE_NAME = "users";
    public static final String TABLE_TWO_NAME = "groups";
    public static final String TABLE_THREE_NAME = "messages";
    public static final String TABLE_FOUR_NAME = "user_groups";
    public static final String TABLE_FIVE_NAME = "unsent_messages";

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
    private static final String KEY_MESSAGE_CREATEDBYNAME = "createdByName";
    private static final String KEY_MESSAGE_GROUPID = "groupId";
    private static final String KEY_MESSAGE_LIKES = "likes";

    // Unsent message items
    private static final String KEY_UNSENT_CONTENT = "content";
    private static final String KEY_UNSENT_GROUPID = "groupId";



    private static DatabaseHelper sInstance;
    public DatabaseHelper(Context context){
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
//        sqLiteDatabase.execSQL("create table " + TABLE_ONE_NAME + "(" + KEY_USER_ID + " TEXT PRIMARY KEY," + KEY_USER_NAME + " TEXT," + KEY_USER_PHONE + " INTEGER," + KEY_USER_PASSWORD + " TEXT," + KEY_USER_CREATEDAT + " TEXT," + KEY_USER_LOCATION + " TEXT" + ")");
//        sqLiteDatabase.execSQL("create table " + TABLE_TWO_NAME + "(" + KEY_GROUP_ID + " TEXT PRIMARY KEY," + KEY_GROUP_NAME + " TEXT," + KEY_GROUP_DESCRIPTION + " TEXT," + KEY_GROUP_IMAGE + " BLOB," + KEY_GROUP_CREATEDAT + " TEXT, " + KEY_GROUP_CREATEDBY + " TEXT REFERENCES " + TABLE_ONE_NAME + "," + KEY_GROUP_LOCATION + " TEXT," + KEY_GROUP_CATEGORY + " TEXT" + ")");
        sqLiteDatabase.execSQL("create table " + TABLE_THREE_NAME + "(" + KEY_MESSAGE_ID + " TEXT PRIMARY KEY," + KEY_MESSAGE_CONTENT + " TEXT," + KEY_MESSAGE_CREATEDAT + " TEXT," + KEY_MESSAGE_ATTACHMENT + " BLOB," + KEY_MESSAGE_CREATEDBY + " TEXT, " + KEY_MESSAGE_CREATEDBYNAME + " TEXT, " + KEY_MESSAGE_GROUPID + " TEXT, " + KEY_MESSAGE_LIKES + " TEXT " + ")");
//        sqLiteDatabase.execSQL("create table " + TABLE_FOUR_NAME + "(USERGROUP_ID TEXT PRIMARY KEY, NOTIFICATIONS INTEGER, READ INTEGER, USER_ID TEXT, GROUP_ID TEXT, FOREIGN KEY(USER_ID) REFERENCES users(USER_ID), FOREIGN KEY(GROUP_ID) REFERENCES groups(GROUP_ID))");
//        sqLiteDatabase.execSQL("create table " + TABLE_FIVE_NAME + "(" + KEY_MESSAGEOBJECT + " BLOB" + ")");
        sqLiteDatabase.execSQL("create table " + TABLE_FIVE_NAME + "(" + KEY_UNSENT_CONTENT + " TEXT," + KEY_UNSENT_GROUPID + " TEXT" + ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int j) {
//        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_ONE_NAME);
//        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_TWO_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_THREE_NAME);
//        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_FOUR_NAME);

        onCreate(sqLiteDatabase);
    }

    public static synchronized DatabaseHelper getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new DatabaseHelper(context.getApplicationContext());
        }
        return sInstance;
    }

    public void addMessages(List<Message> messages) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        try {
            if (messages == null) {
                return;
            } else {
//            ParseUser user = addOrUpdateUser(message.KEY_CREATED_BY);
                for (int i = 0; i < messages.size(); i++) {
                    Message currentMessage = messages.get(i);
                    ContentValues values = new ContentValues();
                    Log.d("uh oh message", currentMessage.getContent());
                    values.put(KEY_MESSAGE_ID, currentMessage.getObjectId());
                    values.put(KEY_MESSAGE_CREATEDBY, currentMessage.getCreatedBy().getObjectId());
                    values.put(KEY_MESSAGE_CREATEDBYNAME, currentMessage.getCreatedBy().getString("name"));
                    values.put(KEY_MESSAGE_CONTENT, currentMessage.getContent());
                    values.put(KEY_MESSAGE_CREATEDAT, currentMessage.getCreatedAtString());
                    values.put(KEY_MESSAGE_GROUPID, currentMessage.getChat().getObjectId());
                    values.put(KEY_MESSAGE_LIKES, String.valueOf(currentMessage.getLikes()));
                    db.insert(TABLE_THREE_NAME, null, values);
                }
                db.setTransactionSuccessful();
                Log.d("database", "successfully add messages");
            }
        } finally {
            db.endTransaction();
        }
    }

    public void addMessage(Message message) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            Log.e("messageId", message.getObjectId());
            values.put(KEY_MESSAGE_ID, message.getObjectId());
            values.put(KEY_MESSAGE_CREATEDBY, message.getCreatedBy().getObjectId());
            values.put(KEY_MESSAGE_CONTENT, message.getContent());
            values.put(KEY_MESSAGE_CREATEDAT, message.getCreatedAtString());
            values.put(KEY_MESSAGE_GROUPID, message.getChat().getObjectId());
            values.put(KEY_MESSAGE_LIKES, message.getLikes());
            db.insert(TABLE_THREE_NAME, null, values);
            db.setTransactionSuccessful();
        } finally{
            db.endTransaction();
        }
    }

//    public String readLastMessageTime(String groupId) {
//        SQLiteDatabase db = this.getReadableDatabase();
//
//        String selection = "SELECT * FROM " + TABLE_THREE_NAME + " WHERE " + KEY_MESSAGE_GROUPID + " = '" + groupId + "'" + " ORDER BY " + KEY_MESSAGE_CREATEDAT + " DESC LIMIT 1";
//        Cursor cursor = db.rawQuery(selection, null);
//        String lastMessageTime = null;
//        if (cursor.moveToFirst()) {
//            lastMessageTime = cursor.getString(cursor.getColumnIndex(KEY_MESSAGE_CREATEDAT));
//            Log.d("database", lastMessageTime);
//        } else {
//            return null;
//        }
//        cursor.close();
//        return lastMessageTime;
//
//    }

    public List<Message> readAllMessages(String groupId) {
        SQLiteDatabase db = this.getReadableDatabase();
//        String[] params = new String[]{groupId};
        String selection = "SELECT * FROM " + TABLE_THREE_NAME + " WHERE " + KEY_MESSAGE_GROUPID + " = '" + groupId + "'";
//        String selection = "SELECT * FROM " + TABLE_THREE_NAME;
        Cursor cursor = db.rawQuery(selection, null);
        List<Message> messages = new ArrayList<>();
        try {
            if (cursor.moveToFirst()) {
                do {
                    Log.d("given groupID", groupId);
                    Log.d("what is the groupID", cursor.getString(cursor.getColumnIndex(KEY_MESSAGE_GROUPID)));
                    Log.d("are they equal", "yes they are!");
                    Message message = new Message(
                            cursor.getString(cursor.getColumnIndex(KEY_MESSAGE_CONTENT)),
                            cursor.getString(cursor.getColumnIndex(KEY_MESSAGE_CREATEDBYNAME)),
                            cursor.getString(cursor.getColumnIndex(KEY_MESSAGE_CREATEDBY)),
                            cursor.getString(cursor.getColumnIndex(KEY_MESSAGE_GROUPID)),
                            cursor.getString(cursor.getColumnIndex(KEY_MESSAGE_CREATEDAT)),
                            cursor.getString(cursor.getColumnIndex(KEY_MESSAGE_LIKES))
                    );
                    messages.add(message);
                } while (cursor.moveToNext());
            }
        } catch (SQLiteException e) {
            Log.d("SQLite error", e.getMessage());
        }finally {
            //db.endTransaction();
        }
        Log.d("read all messages", String.valueOf(messages.size()));
        cursor.close();
        return messages;
    }

    public void saveUnsentMessage(String content, String groupId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            values.put(KEY_UNSENT_CONTENT, content);
            values.put(KEY_UNSENT_GROUPID, groupId);
            db.insert(TABLE_FIVE_NAME, null, values);
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }

    public List<String> readUnsentMessages(String groupId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String selection = "SELECT * FROM " + TABLE_FIVE_NAME + " WHERE " + KEY_UNSENT_GROUPID + "= '" + groupId + "'";
        Cursor cursor = db.rawQuery(selection, null);
        List<String> contents = new ArrayList<>();
        try {
            if (cursor.moveToFirst()) {
                do {
                    Log.e("HELLO", "I'M HERE");
                    String content = cursor.getString(cursor.getColumnIndex(KEY_UNSENT_CONTENT));
                    Log.e("READ DATABASE", cursor.getString(cursor.getColumnIndex(KEY_UNSENT_CONTENT)));
                    contents.add(content);
                } while(cursor.moveToNext());
            } else {
                Log.e("WAWA", "MAMAMA");
            }
        } catch (SQLiteException e) {
            e.printStackTrace();
        }
        cursor.close();
        return contents;
    }

    public boolean isTableEmpty(String tableName) {
        SQLiteDatabase db = this.getReadableDatabase();
        String selection = "SELECT count(*) FROM " + tableName;
        Cursor cursor = db.rawQuery(selection, null);
        cursor.moveToFirst();
        if (cursor.getInt(0) > 0) {
            return false;
        } else {
            return true;
        }

    }

    public void deleteTable(String tableName) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(tableName, null, null);
        db.execSQL("delete from " + tableName);
    }
}
