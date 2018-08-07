package me.susiel2.locationchat.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;

@ParseClassName("UserPoints")
@DatabaseTable(tableName = "user_points")
public class UsersPoints extends ParseObject {

    @DatabaseField(columnName = "USER_ID")
    private static final String KEY_USER = "user";

    @DatabaseField(columnName = "TOTAL_POINTS")
    private static final String KEY_POINTS = "totalPoints";

    public String getIdString() {
        return getObjectId();
    }

    public ParseUser getUser() {
        return getParseUser(KEY_USER);
    }

    public void setUser(ParseUser parseUser) {
        put(KEY_USER, parseUser);
    }

    public int getTotalPoints() {
        return (int) getInt(KEY_POINTS);
    }

    public void setTotalPoints(int totalPoints) {
        put(KEY_POINTS, totalPoints);
    }


}
