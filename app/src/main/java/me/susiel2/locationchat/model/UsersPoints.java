package me.susiel2.locationchat.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

@ParseClassName("UserPoints")
@DatabaseTable(tableName = "user_points")
public class UsersPoints extends ParseObject {

    @DatabaseField(columnName = "USER_ID")
    private static final String KEY_USER = "userId";

    @DatabaseField(columnName = "TOTAL_POINTS")
    private static final String KEY_POINTS = "totalPoints";

    @DatabaseField(columnName = "GROUP_ID")
    private static final String KEY_GROUP_ID = "groupId";

    public String getIdString() {
        return getObjectId();
    }

    public ParseUser getUser() {
        return getParseUser(KEY_USER);
    }

    public void setUser(String objectId) {
        put(KEY_USER, objectId);
    }

    public int getTotalPoints() {
        return (int) getInt(KEY_POINTS);
    }

    public void setTotalPoints(int totalPoints) {
        put(KEY_POINTS, totalPoints);
    }

    public Chat getChat() {
        return (Chat) getParseObject(KEY_GROUP_ID);
    }

    public static class Query extends ParseQuery<UsersPoints> {
        public Query() {
            super(UsersPoints.class);
        }

        // TODO - add useful Query methods

    }
}
