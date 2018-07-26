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

    public static void createMessage(String content, String groupObjectID) {
        Message newMessage = new Message();
        newMessage.setCreatedBy(ParseUser.getCurrentUser());
        newMessage.setContent(content);
        ParseQuery<Chat> query = ParseQuery.getQuery(Chat.class);
        query.whereEqualTo("objectId", groupObjectID);
        try {
            List<Chat> result = query.find();
            newMessage.setChat(result.get(0));
            newMessage.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    if (e == null) {
                        Log.d("ParseOperations", "Message sent");
                        //Toast.makeText(ChatActivity.class, "Message sent", Toast.LENGTH_SHORT).show();
                        //refreshMessages();
                    } else {
                        Log.e("ParseOperations", e.toString());
                    }
                }
            });
        } catch(ParseException e) {
            e.printStackTrace();
        }
    }

    public static List<Message> getGroupMessages(String currentGroupObjectID) {
        //List<Message> result;
        ParseQuery<Chat> queryChat = ParseQuery.getQuery(Chat.class);
        queryChat.whereEqualTo("objectId", currentGroupObjectID);
        try {
            List<Chat> chats = queryChat.find();
            ParseQuery<Message> query = ParseQuery.getQuery(Message.class);
            query.whereEqualTo("groupId", chats.get(0));
            List<Message> result = query.find();
            return result;
        } catch(ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void setMessagesToUnread(String currentGroupObjectID) {
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

    public static List<Chat> getUnreadGroups(ParseUser user) {
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

    public static void createGroup(String name, String description, File image, String category, final ParseUser user, String location){
        final Chat chat = new Chat(name, description, new ParseFile(image), category, user, location);
        chat.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e==null) {
                    Log.e("ParseOperations", "New group successfully uploaded");
                    addUserToGroup(user, chat.getObjectId());
                } else {
                    Log.e("ParseOperations", "Failed to upload new group");
                }
            }
        });
    }

    public static List<Chat> getGroupsInCategory(String category) {
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

    public static List<Chat> getGroupsUserIsIn(ParseUser currentUser){
        ParseQuery<UsersGroups> query = ParseQuery.getQuery(UsersGroups.class);
        query.whereEqualTo("user", currentUser);
        query.include("group");
        ArrayList<Chat> groups = new ArrayList<Chat>();
        try {
            List<UsersGroups> results = query.find();
            for(int i = 0; i < results.size(); i++){
                groups.add(results.get(i).getChat());
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return groups;
    }


    // Note that this method only returns groups whose location matches the location of the user
    public static List<Chat> getGroupsUserIsNotIn(ParseUser currentUser){
        List<Chat> userIsIn = getGroupsUserIsIn(currentUser);
        ArrayList<String> groupNames = new ArrayList<String>();
        for(int i = 0; i < userIsIn.size(); i++)
            groupNames.add(userIsIn.get(i).getName());
        ParseQuery<Chat> query = ParseQuery.getQuery(Chat.class);
        query.whereNotContainedIn("name", groupNames);
        query.whereEqualTo("location", currentUser.getString("location"));
        List<Chat> results = new ArrayList<>();
        try {
            results = query.find();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return results;
    }

    public static void addUserToGroup(ParseUser currentUser, String groupId){
        final UsersGroups usersGroups = new UsersGroups();
        usersGroups.setUser(currentUser);
        usersGroups.setNotificationsOn(true);
        usersGroups.setRead(true);

        ParseQuery<Chat> query = ParseQuery.getQuery(Chat.class);
        query.whereEqualTo("objectId", groupId);
        try {
            List<Chat> results = query.find();
            usersGroups.setChat(results.get(0));
        } catch (ParseException e) {
            e.printStackTrace();
        }
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

    public static void changeUserLocation(ParseUser user, String location) {
        user.put("location", location);
    }

    public static void setNotificationsForUserInGroup(final boolean notificationsOn, ParseUser user, String groupId){
        final ParseQuery<UsersGroups> query = ParseQuery.getQuery(UsersGroups.class);
        query.whereEqualTo("user", user);

        ParseQuery<Chat> query2 = ParseQuery.getQuery(Chat.class);
        query2.whereEqualTo("objectId", groupId);
        try {
            List<Chat> results = query2.find();
            query.whereEqualTo("group", results.get(0));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        try {
            List<UsersGroups> results = query.find();
            results.get(0).setNotificationsOn(notificationsOn);
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    public static int getNumberOfMembersInGroup(String groupId){
        final ParseQuery<UsersGroups> query = ParseQuery.getQuery(UsersGroups.class);

        ParseQuery<Chat> query2 = ParseQuery.getQuery(Chat.class);
        query2.whereEqualTo("objectId", groupId);
        try {
            List<Chat> results = query2.find();
            query.whereEqualTo("group", results.get(0));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        List<UsersGroups> results = null;
        try {
            results = query.find();
            return results.size();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return -1;
        
    }

    public static List<Chat> getGroupsBySearch(String searchParam) {
        ParseQuery<Chat> query = ParseQuery.getQuery(Chat.class);
        query.whereFullText("name", searchParam);
        try {
            return query.find();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return new ArrayList<Chat>();
    }

    public static List<Chat> getGroupsBySearchAndCategory(String searchParam, String category) {
        ParseQuery<Chat> query = ParseQuery.getQuery(Chat.class);
        query.whereFullText("name", searchParam);
        query.whereEqualTo("category", category);
        try {
            return query.find();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return new ArrayList<Chat>();
    }
}
