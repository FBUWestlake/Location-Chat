package me.susiel2.locationchat.model;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;

import java.util.Date;

@ParseClassName("Message")
public class Message extends ParseObject {

//    public String message;
//    public String user;
//    public String time;
//    public String userImage;
//
//    public Message(String message, String user, String time, String userImage){
//        this.message = message;
//        this.user = user;
//        this.time = time;
//        this.userImage = userImage;
//    }
//
//    // TODO - add method for parsing raw createdAt() data
//
//    public String getMessage() {
//        return message;
//    }
//
//    public void setMessage(String message) {
//        this.message = message;
//    }
//
//    public String getUser() {
//        return user;
//    }
//
//    public void setUser(String user) {
//        this.user = user;
//    }
//
//    public String getTime() {
//        return time;
//    }
//
//    public void setTime(String time) {
//        this.time = time;
//    }
//
//    public String getUserImage() {
//        return userImage;
//    }
//
//    public void setUserImage(String userImage) {
//        this.userImage = userImage;
//    }

    public static final String CREATED_BY_KEY = "createdBy";
    public static final String CONTENT_KEY = "content";
    public static final String IMAGE_KEY = "profileImage";
    public static final String NAME_KEY = "name";


    public String getCreatedBy() {
        return getString(CREATED_BY_KEY);
    }

    public String getContent() {
        return getString(CONTENT_KEY);
    }

    public void setContent(String content) {
        put(CONTENT_KEY, content);
    }

    public Date getCreatedAt() {
        return super.getCreatedAt();
    }

    public ParseFile getProfileImage() {
        return getParseFile(IMAGE_KEY);
    }

    public String getName() {
        return getString(NAME_KEY);
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

@ParseClassName("Message")
public class Message {

    private static final String KEY_CONTENT = "content";
    private static final String KEY_ATTACHMENT = "attachment";
    private static final String KEY_GROUP_ID = "groupId";
    private static final String KEY_CREATED_BY = "createdBy";

    public ParseObject getGroupIdString() {
        return getObjectId().toString();
    }

    public String getName() {
        return getString(KEY_NAME);
    }

    public void setName(String name) {
        put(KEY_NAME, name);
    }

    public String getDescription() {
        return getString(KEY_DESCRIPTION);
    }

    public void setDescription(String desc) {
        put(KEY_DESCRIPTION, desc);
    }

    public ParseObject getImage() {
        return getParseObject(KEY_IMAGE);
    }

    public void setImage(ParseObject obj) {
        put(KEY_IMAGE, obj);
    }

    public String getCategory() {
        return getString(KEY_CATEGORY);
    }

    public void setCategory(String category) {
        put(KEY_CATEGORY, category);
    }

    public ParseUser getCreatedBy() {
        return getParseUser(KEY_CREATED_BY);
    }

    public void setCreatedBy(ParseUser user) {
        put(KEY_CREATED_BY, user);
    }

    public int getLocation() {
        return getInt(KEY_LOCATION_ID);
    }

    public void setLocation(int location) {
        put(KEY_LOCATION_ID, location);
    }

    public String getCreatedAtString() {
        return getCreatedAt().toString();
    }

    public static class Query extends ParseQuery<Chat> {
        public Query() {
            super(Chat.class);
        }

        // TODO - add useful Query methods

    }
}
