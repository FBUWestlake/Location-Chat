package me.susiel2.locationchat.model;

import android.graphics.Bitmap;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.parceler.Parcel;

import java.io.File;

@ParseClassName("Group")
public class Chat extends ParseObject {

    private static final String KEY_NAME = "name";
    private static final String KEY_DESCRIPTION = "description";
    private static final String KEY_IMAGE = "image";
    private static final String KEY_CATEGORY = "category";
    private static final String KEY_CREATED_BY = "createdBy";
    private static final String KEY_LOCATION_ID = "location";

    public String getGroupIdString() {
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

    public ParseFile getImage() {
        return getParseFile(KEY_IMAGE);
    }

    public void setImage(ParseFile obj) {
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