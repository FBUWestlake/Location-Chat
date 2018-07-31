package me.susiel2.locationchat.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

@ParseClassName("UserGroups")
@DatabaseTable(tableName = "user_groups")
public class UsersGroups extends ParseObject {

    @DatabaseField(columnName = "USER_ID")
    private static final String KEY_USER = "user";

    @DatabaseField(columnName = "GROUP_ID")
    private static final String KEY_GROUP = "group";

    @DatabaseField(columnName = "NOTIFICATIONS")
    private static final String KEY_NOTIFICATIONS = "notifications";

    @DatabaseField(columnName = "READ")
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