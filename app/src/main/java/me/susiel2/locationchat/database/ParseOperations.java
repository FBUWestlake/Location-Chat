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
import java.util.Arrays;
import java.util.List;

import me.susiel2.locationchat.ChatActivity;
import me.susiel2.locationchat.model.Message;
import me.susiel2.locationchat.model.MessageAdapter;

public class ParseOperations {

    // Enjoy. :^)
    // ¯\_(ツ)_/¯

    public static void createMessage(String content, Chat chat) {
        Message newMessage = new Message();
        newMessage.setCreatedBy(ParseUser.getCurrentUser());
        newMessage.setContent(content);
        newMessage.setChat(chat);
        newMessage.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    Log.d("ParseOperations", "Message sent");
                } else {
                    Log.e("ParseOperations", e.toString());
                }
            }
        });
    }

    //No longer used. Implemented in-class so as to use global variables
    public static List<Message> getGroupMessages(Chat chat) {
        try {
            ParseQuery<Message> query = ParseQuery.getQuery(Message.class);
            query.whereEqualTo("groupId", chat);
            List<Message> result = query.find();
            return result;
        } catch(ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean isChatRead(Chat chat, ParseUser user){
        ParseQuery<UsersGroups> query = ParseQuery.getQuery(UsersGroups.class);
        query.whereEqualTo("group", chat);
        query.whereEqualTo("user", user);
        List<UsersGroups> result;
        try {
            result = query.find();
            for (int i = 0; i < result.size(); i++) {
                return result.get(i).isRead();
            }
        } catch(ParseException e) {
            e.printStackTrace();
        }
        return true;
    }

    public static void ifChatNotReadSetRead(final Chat chat, final ParseUser user){
        ParseQuery<UsersGroups> query = ParseQuery.getQuery(UsersGroups.class);
        query.whereEqualTo("group", chat);
        query.whereEqualTo("user", user);

        query.findInBackground(new FindCallback<UsersGroups>() {
            public void done(List<UsersGroups> itemList, ParseException e) {
                if (e == null) {
                    if(!itemList.get(0).isRead())
                        setMessageAsReadInGroup(user, chat);
                } else {
                    Log.d("item", "Error: " + e.getMessage());
                }
            }
        });


    }

    public static void setMessagesToUnread(Chat chat) {
        ParseQuery<UsersGroups> query = ParseQuery.getQuery(UsersGroups.class);
        List<UsersGroups> result = null;
        try {
            query.whereEqualTo("group", chat);
            result = query.find();
            for (int i = 0; i < result.size(); i++) {
                result.get(i).setRead(false);
                result.get(i).saveInBackground();
            }
        } catch (ParseException e) {
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

    public static void createGroup(String name, String description, File image, String category, final ParseUser user, final String location){
        final Chat chat = new Chat(name, description, new ParseFile(image), category, user, location);
        chat.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e==null && location.equals(getUserLocation(user))) {
                    Log.e("ParseOperationsLocation", getUserLocation(user));
                    addUserToGroup(user, chat);
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

    public static List<Chat> getGroupsUserIsInDeprecated(ParseUser currentUser){
        ParseQuery<UsersGroups> query = ParseQuery.getQuery(UsersGroups.class);
        query.whereEqualTo("user", currentUser);
        query.addDescendingOrder("updatedAt");
        final ArrayList<String> groupIds = new ArrayList<>();

        try {
            List<UsersGroups> results = query.find();
            for(int i = 0; i < results.size(); i++){
                groupIds.add(results.get(i).getChat().getIdString());
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        ArrayList<Chat> groups = new ArrayList<Chat>();
        for(int i = 0; i < groupIds.size(); i++){
            groups.add(getGroupFromId(groupIds.get(i)));
        }
        return groups;
    }

    public static ArrayList<Chat> getGroupsUserIsIn(ParseUser currentUser){
        ParseQuery<UsersGroups> query = ParseQuery.getQuery(UsersGroups.class);
        query.include("group").whereEqualTo("user", currentUser).addDescendingOrder("updatedAt");
        final ArrayList<Chat> groups = new ArrayList<>();
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

    public static Chat getGroupFromId(String objectId){

        ParseQuery<Chat> query = ParseQuery.getQuery(Chat.class);
        query.whereEqualTo("objectId", objectId);
        try {
            List<Chat> result = query.find();
            return result.get(0);
        } catch(ParseException e) {
            e.printStackTrace();
        }

        return null;

    }

    public static String getUserLocation(ParseUser parseUser){
        ParseQuery<ParseUser> query = ParseQuery.getQuery(ParseUser.class);
        query.whereEqualTo("objectId", parseUser.getObjectId());
        try {
            List<ParseUser> result = query.find();
            return result.get(0).getString("location");
        } catch(ParseException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static String getUsersName(ParseUser parseUser){
        ParseQuery<ParseUser> query = ParseQuery.getQuery(ParseUser.class);
        query.whereEqualTo("objectId", parseUser.getObjectId());
        try {
            List<ParseUser> result = query.find();
            return result.get(0).getString("name");
        } catch(ParseException e) {
            e.printStackTrace();
        }

        return null;
    }


    // Note that this method only returns groups whose location matches the location of the user
    public static List<Chat> getGroupsUserIsNotIn(ParseUser currentUser){
        List<Chat> userIsIn = getGroupsUserIsIn(currentUser);
        ArrayList<String> groupNames = new ArrayList<String>();
        for(int i = 0; i < userIsIn.size(); i++)
            groupNames.add(userIsIn.get(i).getName());
        ParseQuery<Chat> query = ParseQuery.getQuery(Chat.class);
        query.whereNotContainedIn("name", Arrays.asList(groupNames));
        String location = getUserLocation(currentUser);
        Log.e("ParseOperations", location);
        query.whereEqualTo("location", location);
        List<Chat> results = new ArrayList<>();
        try {
            results = query.find();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return results;
    }

    public static void addUserToGroup(ParseUser currentUser, Chat chat) {
        final UsersGroups usersGroups = new UsersGroups();
        usersGroups.setUser(currentUser);
        usersGroups.setNotificationsOn(true);
        usersGroups.setRead(true);
        usersGroups.setChat(chat);
        usersGroups.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e==null) {
                    Log.d("ParseOperations", "User successfully added to group");
                } else {
                    Log.d("ParseOperations", "Failed to add user to group");
                }
            }
        });
    }

    public static void changeUserLocation(ParseUser user, String location) {
        user.put("location", location);
        ParseQuery<UsersGroups> query = ParseQuery.getQuery(UsersGroups.class);
        query.include("group");
        query.whereEqualTo("user", user);
        try {
            List<UsersGroups> usersGroups = query.find();
            for (int i = 0; i < usersGroups.size(); i++) {
                String groupName = usersGroups.get(i).getChat().getName();
                Log.d("works", groupName);
                leaveGroup(user, usersGroups.get(i).getChat());
                addUserToGroup(user, getGroupByNameLocation(groupName, location));
            }
        } catch(ParseException e) {
            e.printStackTrace();
        }

    }

    // Unnecessary.
//    public static void setNotificationsForUserInGroup(final boolean notificationsOn, ParseUser user, String groupId){
//        final ParseQuery<UsersGroups> query = ParseQuery.getQuery(UsersGroups.class);
//        query.whereEqualTo("user", user);
//
//        ParseQuery<Chat> query2 = ParseQuery.getQuery(Chat.class);
//        query2.whereEqualTo("objectId", groupId);
//        try {
//            List<Chat> results = query2.find();
//            query.whereEqualTo("group", results.get(0));
//            List<UsersGroups> results2 = query.find();
//            results2.get(0).setNotificationsOn(notificationsOn);
//            results2.get(0).saveInBackground();
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//
//    }

    public static int getNumberOfMembersInGroup(Chat chat){
        final ParseQuery<UsersGroups> query = ParseQuery.getQuery(UsersGroups.class);
        query.whereEqualTo("group", chat);
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

    public static Chat getGroupByNameLocation(String chatName, String location) {
        ParseQuery<Chat> query = ParseQuery.getQuery(Chat.class);
        query.whereFullText("name", chatName);
        query.whereFullText("location", location);
        try {
            return query.find().get(0);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
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

    public static void leaveGroup(ParseUser user, Chat chat) {
        ParseQuery<UsersGroups> query = ParseQuery.getQuery(UsersGroups.class);
        query.whereEqualTo("user", user);
        try {
            query.whereEqualTo("group", chat);
            UsersGroups result = query.find().get(0);
            result.delete();
        } catch(ParseException e) {
            e.printStackTrace();
        }
    }

    public static void setMessageAsReadInGroup(ParseUser user, Chat chat) {
        ParseQuery<UsersGroups> query = ParseQuery.getQuery(UsersGroups.class);
        query.whereEqualTo("user", user);
        query.whereEqualTo("group", chat);
        query.include("read");
        query.findInBackground(new FindCallback<UsersGroups>() {
            public void done(List<UsersGroups> itemList, ParseException e) {
                if (e == null) {
                    itemList.get(0).setRead(true);
                    itemList.get(0).saveInBackground();
                } else {
                    Log.d("item", "Error: " + e.getMessage());
                }
            }
        });
    }
}
