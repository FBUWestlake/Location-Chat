package me.susiel2.locationchat.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.Date;

@ParseClassName("Message")
@DatabaseTable(tableName = "messages")
public class Message extends ParseObject{

    @DatabaseField(columnName = "CONTENT")
    private static final String KEY_CONTENT = "content";

    @DatabaseField(columnName = "ATTACHMENT")
    private static final String KEY_ATTACHMENT = "attachment";

    @DatabaseField(columnName = "GROUP_ID")
    private static final String KEY_GROUP_ID = "groupId";

    @DatabaseField(columnName = "CREATED_BY")
    private static final String KEY_CREATED_BY = "createdBy";

    public String getIdString() { return getObjectId(); }

    public String getContent() {
        return getString(KEY_CONTENT);
    }

    public void setContent(String content) {
        put(KEY_CONTENT, content);
    }

    // TODO - determine whether attachment is a link or image or video and return something accordingly
    // TODO contd. - currently this just returns a string for whatever the attachment is.
    public String getAttachment() {
        return getString(KEY_ATTACHMENT);
    }
    public void setAttachment(String attachment) {
        put(KEY_ATTACHMENT, attachment);
    }
    // End TODO

    public Chat getChat() {
        return (Chat) getParseObject(KEY_GROUP_ID);
    }

    public void setChat(Chat chat) { put(KEY_GROUP_ID, chat); }

    public ParseUser getCreatedBy() {
        return getParseUser(KEY_CREATED_BY);
    }

    public void setCreatedBy(ParseUser user) {
        put(KEY_CREATED_BY, user);
    }

    public String getCreatedAtString() {
        return getCreatedAt().toString();
    }

    public static class Query extends ParseQuery<Message> {
        public Query() {
            super(Message.class);
        }

        // TODO - add useful Query methods

    }

}
