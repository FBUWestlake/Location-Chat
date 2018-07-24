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
    }
}
