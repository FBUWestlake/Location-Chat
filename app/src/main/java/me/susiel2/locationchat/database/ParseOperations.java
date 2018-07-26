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
            Log.d("ParseOperation", result.get(0).getContent());
            Log.d("ParseOperation", result.get(1).getContent());
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

    public static void createGroup(String name, String description, File image, String category, ParseUser user, String location){
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

    public static void addUserToGroup(ParseUser currentUser, String groupId){
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

    public static void changeUserLocation(ParseUser user, String location) {
        user.put("location", location);
    }

    public static void setNotificationsForUserInGroup(final boolean notificationsOn, ParseUser user, String groupId){
        final ParseQuery<UsersGroups> query = ParseQuery.getQuery(UsersGroups.class);
        query.whereEqualTo("user", user);

        ParseQuery<Chat> query2 = ParseQuery.getQuery(Chat.class);
        query2.getInBackground(groupId, new GetCallback<Chat>() {
            public void done(Chat chat, ParseException e) {
                if (e == null) {
                    query.whereEqualTo("group", chat);
                }
            }
        });

        query.findInBackground(new FindCallback<UsersGroups>() {
            public void done(List<UsersGroups> itemList, ParseException e) {
                if (e == null) {
                    UsersGroups firstItem = itemList.get(0);
                    firstItem.setNotificationsOn(notificationsOn);
                } else {
                    Log.d("item", "Error: " + e.getMessage());
                }
            }
        });
    }

    public static int getNumberOfMembersInGroup(String groupId){
        final ParseQuery<UsersGroups> query = ParseQuery.getQuery(UsersGroups.class);

        ParseQuery<Chat> query2 = ParseQuery.getQuery(Chat.class);
        query2.getInBackground(groupId, new GetCallback<Chat>() {
            public void done(Chat chat, ParseException e) {
                if (e == null) {
                    query.whereEqualTo("group", chat);
                }
            }
        });

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
