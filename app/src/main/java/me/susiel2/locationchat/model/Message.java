package me.susiel2.locationchat.model;

import com.cardiomood.android.sync.annotations.ParseField;
import com.cardiomood.android.sync.ormlite.SyncEntity;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.io.Serializable;
import java.security.acl.Group;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import me.susiel2.locationchat.database.ParseOperations;

@ParseClassName("Message")
public class Message extends ParseObject {

    private static String content = "";
    private static String createdBy = null;
    private static String group = null;
    private static String date = null;

    public Message(String content, String createdBy, String group, String date) {
//        this.KEY_CONTENT = content;
//        this.createdBy = createdBy;
//        this.group = group;
//        this.date = date;
        super();
        setContent(content);
        setCreatedByFromUserId(createdBy);
        setChatFromGroupId(group);
        setCreatedAt(date);
    }

    public Message() {

    }

    private static final String KEY_CONTENT = "content";

    private static final String KEY_ATTACHMENT = "attachment";

    private static final String KEY_GROUP_ID = "groupId";

    private static final String KEY_CREATED_BY = "createdBy";

    private static final String KEY_CREATED_AT = "createdAt";

    private static final String KEY_LIKES = "likes";

    public String getIdString() {
        return getObjectId();
    }

    public String getContent() {
        return getString(KEY_CONTENT);
    }

    public void setContent(String content) {
        put(KEY_CONTENT, content);
    }

    // TODO - determine whether attachment is a link or image or video and return something accordingly
    // TODO contd. - currently this just returns a string for whatever the attachment is.
    public String getAttachment() {
        return getString(KEY_ATTACHMENT);
    }

    public void setAttachment(String attachment) {
        put(KEY_ATTACHMENT, attachment);
    }
    // End TODO

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
        return getCreatedAt().toString();
    }

    public void setCreatedAt(String dateString) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE MMM dd kk:mm:ss zzz yyyy");
        Date date = null;
        try {
            date = dateFormat.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        put(KEY_CREATED_AT, date);
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
}
