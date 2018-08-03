package me.susiel2.locationchat.model;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.cardiomood.android.sync.annotations.ParseField;
import com.cardiomood.android.sync.ormlite.SyncEntity;
import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.io.Serializable;
import java.util.Date;

@ParseClassName("Group")
@DatabaseTable(tableName = "groups")
public class Chat extends ParseObject {

    public String KEY_NAME = "name";

    public String KEY_DESCRIPTION = "description";

    public String KEY_IMAGE = "image";

    public String KEY_CATEGORY = "category";

    public String KEY_CREATED_BY = "createdBy";

    public String KEY_LOCATION_ID = "location";

    public String getIdString() {
        return getObjectId();
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

    public String getLocation() {
        return getString(KEY_LOCATION_ID);
    }

    public void setLocation(String location) {
        put(KEY_LOCATION_ID, location);
    }

    public String getCreatedAtString() {
        return getCreatedAt().toString();
    }

    public Chat(){
        super();
    }

    public Chat(String name, String description, ParseFile image, String category, ParseUser createdBy, String location){
        super();
        setName(name);
        setDescription(description);
        setImage(image);
        setCategory(category);
        setCreatedBy(createdBy);
        setLocation(location);
    }
    public String name;
    public String imageUrl;
    public String description;
    public String category;
    public int members;

    public Chat(String name, String imageUrl,
                String description, String category, int members) {
        this.name = name;
        this.imageUrl = imageUrl;
        this.description = description;
        this.category = category;
        this.members = members;
    }

    public Bitmap getImageBitmap(){
        try {
            return BitmapFactory.decodeFile(getImage().getFile().getAbsolutePath());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static class Query extends ParseQuery<Chat> {
        public Query() {
            super(Chat.class);
        }

        // TODO - add useful Query methods

    }

}