package me.susiel2.locationchat.database;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;

import me.susiel2.locationchat.ChatActivity;
import me.susiel2.locationchat.model.Message;
import me.susiel2.locationchat.model.MessageAdapter;

public class ParseOperations {

    // Enjoy. :^)
    Message message;

    public void createMessage(String content) {
        ParseObject message = ParseObject.create("Message");
        message.put("USER_ID", ParseUser.getCurrentUser().getObjectId());
        message.put("content", content);
        // TODO: message.put("groupId", groupId);
        message.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e==null) {
                    //Toast.makeText(ChatActivity.class, "Message sent", Toast.LENGTH_SHORT).show();
                    //refreshMessages();
                } else {
                    Log.e("message", "failed to send");
                }
            }
        });
    }

    // Functions to get message information.

    public String getMessageContent() {
        return message.getContent();
    }

    public String getMessageCreatorName() {
        return message.getCreatedBy().getString("name");
    }

    public ParseUser getMessageCreatorUser() {
        return message.getCreatedBy();
    }

    public String getMessageCreatedTime() {
        return message.getCreatedAtString();
    }

}
