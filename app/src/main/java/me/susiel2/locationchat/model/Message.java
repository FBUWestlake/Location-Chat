package me.susiel2.locationchat.model;

import com.parse.ParseClassName;
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
