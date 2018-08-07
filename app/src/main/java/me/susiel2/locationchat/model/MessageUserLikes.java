package me.susiel2.locationchat.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;


@ParseClassName("MessageUserLikes")
@DatabaseTable(tableName = "message_user_likes")
public class MessageUserLikes extends ParseObject {

    @DatabaseField(columnName = "USER_ID")
    private static final String KEY_USER = "userId";

    @DatabaseField(columnName = "MESSAGE_ID")
    private static final String KEY_MESSAGE = "messageId";

    @DatabaseField(columnName = "LIKED")
    private static final String KEY_LIKED = "liked";

    @DatabaseField(columnName = "DISLIKED")
    private static final String KEY_DISLIKED = "disliked";

    public String getIdString() {
        return getObjectId();
    }

    public ParseUser getUser() {
        return getParseUser(KEY_USER);
    }

    public void setUser(String objectId) {
        put(KEY_USER, objectId);
    }

    public String getMessageId(Message message) {
        return message.getIdString();
    }

    public void setMessageId(String objectId) {
        put(KEY_MESSAGE, objectId);
    }

    public  boolean getLiked() {
        return getBoolean(KEY_LIKED);
    }

    public void setLiked(boolean bool) {
        put(KEY_LIKED, bool);
    }

    public  boolean getDisliked() {
        return getBoolean(KEY_DISLIKED);
    }

    public void setDisliked(boolean bool) {
        put(KEY_DISLIKED, bool);
    }

    public static class Query extends ParseQuery<UsersPoints> {
        public Query() {
            super(UsersPoints.class);
        }

        // TODO - add useful Query methods

    }
}
