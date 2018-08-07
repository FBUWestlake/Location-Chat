package me.susiel2.locationchat.model;

import android.text.format.DateUtils;

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

@ParseClassName("Message")
@DatabaseTable(tableName = "messages")
public class Message extends ParseObject{

    @DatabaseField(columnName = "CONTENT")
    private static final String KEY_CONTENT = "content";

    @DatabaseField(columnName = "ATTACHMENT")
    private static final String KEY_ATTACHMENT = "image";

    @DatabaseField(columnName = "GROUP_ID")
    private static final String KEY_GROUP_ID = "groupId";

    @DatabaseField(columnName = "CREATED_BY")
    private static final String KEY_CREATED_BY = "createdBy";
    
    @DatabaseField(columnName = "LIKES")
    private static final String KEY_LIKES = "likes";

    public String getIdString() { return getObjectId(); }

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

    public void setChat(Chat chat) { put(KEY_GROUP_ID, chat); }

    public ParseUser getCreatedBy() {
        return getParseUser(KEY_CREATED_BY);
    }

    public void setCreatedBy(ParseUser user) {
        put(KEY_CREATED_BY, user);
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

}
