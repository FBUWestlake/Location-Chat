package me.susiel2.locationchat.database;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.GetCallback;
import com.parse.FindCallback;
import com.parse.Parse;
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
import java.util.ArrayList;
import java.util.List;

import me.susiel2.locationchat.ChatActivity;
import me.susiel2.locationchat.model.Message;
import me.susiel2.locationchat.model.MessageAdapter;

public class ParseOperations {

    // Enjoy. :^)
    // ¯\_(ツ)_/¯

    public void createMessage(String content, String groupObjectID) {
        ParseObject message = ParseObject.create("Message");
        message.put("USER_ID", ParseUser.getCurrentUser().getObjectId());
        message.put("content", content);
        message.put("groupId", groupObjectID);
        // TODO: message.put("groupId", groupId);
        message.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e==null) {
                    Log.d("ParseOperations", "Message sent");
                    //Toast.makeText(ChatActivity.class, "Message sent", Toast.LENGTH_SHORT).show();
                    //refreshMessages();
                } else {
                    Log.e("message", "failed to send");
                }
            }
        });
    }

    public List<Message> getGroupMessages(String currentGroupObjectID) {
        ParseQuery<Message> query = ParseQuery.getQuery(Message.class);
        query.whereEqualTo("groupId", currentGroupObjectID);
        List<Message> result;
        try {
            result = query.find();
            return result;
        } catch(ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void setMessagesToUnread(String currentGroupObjectID) {
        ParseQuery<UsersGroups> query = ParseQuery.getQuery(UsersGroups.class);
        query.whereEqualTo("groupId", currentGroupObjectID);
        List<UsersGroups> result;
        try {
            result = query.find();
            for (int i = 0; i < result.size(); i++) {
                result.get(i).setRead(false);
            }
        } catch(ParseException e) {
            e.printStackTrace();
        }

    }

    public List<Chat> getUnreadGroups(ParseUser user) {
        ParseQuery<UsersGroups> query = ParseQuery.getQuery(UsersGroups.class);
        query.whereEqualTo("user", user);
        query.whereEqualTo("read", false);
        List<UsersGroups> result;
        List<Chat> chatGroups = null;
        try {
            result = query.find();
            for (int i = 0; i < result.size(); i++) {
                chatGroups.add(result.get(i).getChat());
            }
        } catch(ParseException e) {
            e.printStackTrace();
        }
        return null;
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

    public List<Chat> getGroupsInCategory(String category) {
        ParseQuery<Chat> query = ParseQuery.getQuery(Chat.class);
        query.whereEqualTo("category", category);
        List<Chat> result;
        try {
            result = query.find();
            return result;
        } catch(ParseException e) {
            e.printStackTrace();
        }
        return null;
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

    public void changeUserLocation(ParseUser user, String location) {
        user.put("location", location);
    }
}
