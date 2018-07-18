package me.susiel2.locationchat.models;

public class Message {

    public String message;
    public String user;
    public String time;
    public String userImage;

    public Message(String message, String user, String time, String userImage){
        this.message = message;
        this.user = user;
        this.time = time;
        this.userImage = userImage;
    }

    // TODO - add method for parsing raw createdAt() data

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getUserImage() {
        return userImage;
    }

    public void setUserImage(String userImage) {
        this.userImage = userImage;
    }
}
