package me.susiel2.locationchat.model;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

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
public class Message extends ParseObject{

    private static final String KEY_CONTENT = "content";
    private static final String KEY_ATTACHMENT = "attachment";
    private static final String KEY_GROUP_ID = "groupId";
    private static final String KEY_CREATED_BY = "createdBy";

    public String getIdString() { return getObjectId(); }

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

    public void setChat(Chat chat) { put(KEY_GROUP_ID, chat.getIdString()); }

    public ParseUser getCreatedBy() {
        return getParseUser(KEY_CREATED_BY);
    }

    public void setCreatedBy(ParseUser user) {
        put(KEY_CREATED_BY, user);
    }

    public String getCreatedAtString() {
        return getCreatedAt().toString();
    }

    public static class Query extends ParseQuery<Message> {
        public Query() {
            super(Message.class);
        }

        // TODO - add useful Query methods

    }

}
