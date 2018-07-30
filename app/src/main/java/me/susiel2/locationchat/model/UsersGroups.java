package me.susiel2.locationchat.model;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

@ParseClassName("UserGroups")
public class UsersGroups extends ParseObject {

    private static final String KEY_USER = "user";
    private static final String KEY_GROUP = "group";
    private static final String KEY_NOTIFICATIONS = "notifications";
    private static final String KEY_READ = "read";

    public String getIdString() {
        return getObjectId();
    }

    public ParseUser getUser() {
        return getParseUser(KEY_USER);
    }

    public void setUser(ParseUser parseUser) {
        put(KEY_USER, parseUser);
    }

    public Chat getChat() {
        return (Chat) getParseObject(KEY_GROUP);
    }

    public void setChat(Chat chat) {
        put(KEY_GROUP, chat);
    }

    public boolean isNotificationsOn() {
        return getBoolean(KEY_NOTIFICATIONS);
    }

    public void setNotificationsOn(boolean notificationsOn) {
        put(KEY_NOTIFICATIONS, notificationsOn);
    }

    public boolean isRead() {
        return getBoolean(KEY_READ);
    }

    public void setRead(boolean read) { put(KEY_READ, read); }

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