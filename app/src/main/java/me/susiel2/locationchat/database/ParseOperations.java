package me.susiel2.locationchat.database;

import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.File;

import me.susiel2.locationchat.ChatActivity;
import me.susiel2.locationchat.model.Chat;
import me.susiel2.locationchat.model.UsersGroups;

public class ParseOperations {

    // Enjoy. :^)
    // ¯\_(ツ)_/¯

    public void createMessage(EditText messageContent) {
        String content = messageContent.getText().toString();
        ParseObject message = ParseObject.create("Message");
        //content.put("USER_ID", ParseUser.getCurrentUser().getObjectId());
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
        messageContent.setText(null);
    }

    public void createGroup(String name, String description, File image, String category, ParseUser user, String location){
        Chat chat = new Chat(name, description, new ParseFile(image), category, user, location);
        chat.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e==null) {
                    Log.e("ParseOperations", "New group successfully uploaded");
                } else {
                    Log.e("ParseOperations", "Failed to upload new group");
                }
            }
        });
    }

    public void addUserToGroup(ParseUser currentUser, String groupId){
        final UsersGroups usersGroups = new UsersGroups();
        usersGroups.setUser(currentUser);
        usersGroups.setNotificationsOn(true);
        usersGroups.setRead(true);

        ParseQuery<Chat> query = ParseQuery.getQuery(Chat.class);
        query.getInBackground(groupId, new GetCallback<Chat>() {
            public void done(Chat chat, ParseException e) {
                if (e == null) {
                    usersGroups.setChat(chat);
                }
            }
        });
        usersGroups.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e==null) {
                    Log.e("ParseOperations", "User successfully added to group");
                } else {
                    Log.e("ParseOperations", "Failed to add user to group");
                }
            }
        });
    }
}
