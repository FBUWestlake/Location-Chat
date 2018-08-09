package me.susiel2.locationchat.model;

import android.text.format.DateUtils;

import android.util.Log;

import com.cardiomood.android.sync.annotations.ParseField;
import com.cardiomood.android.sync.ormlite.SyncEntity;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.io.Serializable;
import java.security.acl.Group;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import me.susiel2.locationchat.database.ParseOperations;

@ParseClassName("Message")
public class Message extends ParseObject {

    private static String content = "";
    private static String createdBy = null;
    private static String group = null;
    private static String date = null;

    public Message(String content, String createdByName, String createdByID, String group, String date, String messageLikes) {
//        this.KEY_CONTENT = content;
//        this.createdBy = createdBy;
//        this.group = group;
//        this.date = date;
        super();
        setBody(content);
        setName(createdByName);
        setUserId(createdByID);
        setGroupId(group);
        setTime(date);
        setMessageLikes(messageLikes);
    }

    public Message(String content, String date, String messageLikes) {
        super();
        setBody(content);
        setTime(date);
        setMessageLikes(messageLikes);
    }

    public Message() {

    }

    private static final String KEY_CONTENT = "content";

    private static final String KEY_ATTACHMENT = "image";

    private static final String KEY_GROUP_ID = "groupId";

    private static final String KEY_CREATED_BY = "createdBy";

    private static final String KEY_CREATED_AT = "createdAt";

    private static final String KEY_LIKES = "likes";
    private String body;
    private String groupId;
    private String time;
    private String name;
    private String userId;
    private String messageLikes;

    public String getIdString() {
        return getObjectId();
    }

    public String getContent() {
        return getString(KEY_CONTENT);
    }

    public void setContent(String content) {
        put(KEY_CONTENT, content);
    }

    public ParseFile getFile() {
        return getParseFile(KEY_ATTACHMENT);
    }

    public void setFile(ParseFile parseFile) {
        put(KEY_ATTACHMENT, parseFile);
    }

    public Chat getChat() {
        return (Chat) getParseObject(KEY_GROUP_ID);
    }

    public void setChat(Chat chat) {
        put(KEY_GROUP_ID, chat);
    }

    public void setChatFromGroupId(String objectId) {
        Chat chat = ParseOperations.getGroupFromId(objectId);
        put(KEY_GROUP_ID, chat);
    }

    public ParseUser getCreatedBy() {
        return getParseUser(KEY_CREATED_BY);
    }

    public void setCreatedBy(ParseUser user) {
        put(KEY_CREATED_BY, user);
    }

    public void setCreatedByFromUserId(String objectId) {
        ParseQuery<ParseUser> query = ParseQuery.getQuery(ParseUser.class);
        query.whereEqualTo("objectId", objectId);
        try {
            put(KEY_CREATED_BY, query.find().get(0));
        } catch (com.parse.ParseException e) {
            e.printStackTrace();
        }
    }

    public String getCreatedAtString() {
        String rawTime = getCreatedAt().toString();
        return getRelativeTimeAgo(rawTime);
    }

    public int getLikes() {
        return getInt(KEY_LIKES);
    }

    public void setLikes(int likes) {
        put(KEY_LIKES, likes);
    }


    public static class Query extends ParseQuery<Message> {
        public Query() {
            super(Message.class);
        }

        // TODO - add useful Query methods

    }

    public String getRelativeTimeAgo(String rawJsonDate) {
        String twitterFormat = "EEE MMM dd HH:mm:ss zzz yyyy";
        SimpleDateFormat sf = new SimpleDateFormat(twitterFormat, Locale.ENGLISH);
        sf.setLenient(true);

        String relativeDate = "";
        try {
            long dateMillis = sf.parse(rawJsonDate).getTime();
            relativeDate = DateUtils.getRelativeTimeSpanString(dateMillis,
                    System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS).toString();
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }

        return relativeDate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getMessageLikes() {
        return messageLikes;
    }

    public void setMessageLikes(String messageLikes) {
        this.messageLikes = messageLikes;
    }
}
