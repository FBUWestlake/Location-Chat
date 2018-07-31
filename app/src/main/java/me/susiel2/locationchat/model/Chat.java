package me.susiel2.locationchat.model;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

@ParseClassName("Group")
@DatabaseTable(tableName = "groups")
public class Chat extends ParseObject {

    @DatabaseField(columnName = "NAME")
    private static final String KEY_NAME = "name";

    @DatabaseField(columnName = "DESCRIPTION")
    private static final String KEY_DESCRIPTION = "description";

    @DatabaseField(columnName = "PHOTO")
    private static final String KEY_IMAGE = "image";

    @DatabaseField(columnName = "CATEGORY")
    private static final String KEY_CATEGORY = "category";

    @DatabaseField(columnName = "CREATED_BY")
    private static final String KEY_CREATED_BY = "createdBy";

    @DatabaseField(columnName = "LOCATION")
    private static final String KEY_LOCATION_ID = "location";

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