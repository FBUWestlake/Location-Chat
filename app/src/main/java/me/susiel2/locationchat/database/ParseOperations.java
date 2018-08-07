package me.susiel2.locationchat.database;

import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import me.susiel2.locationchat.model.Chat;
import me.susiel2.locationchat.model.Message;
import me.susiel2.locationchat.model.UsersGroups;

public class ParseOperations {

    // Enjoy. :^)
    // ¯\_(ツ)_/¯

    public static void createMessage(String content, ParseFile file, Chat chat) {
        Message newMessage = new Message();
        newMessage.setCreatedBy(ParseUser.getCurrentUser());
        newMessage.setContent(content);
        newMessage.setChat(chat);
        newMessage.setLikes(0);
        if(file != null)
            newMessage.setFile(file);
        try {
            newMessage.save();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Log.e("ParseOperations", "Empty file text: " + newMessage.getFile());
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

    //No longer used, implemented in-class
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

    //Sets all flags to unread except for the message poster
    public static void setMessagesToUnread(Chat chat, final ParseUser currentUser) {
        ParseQuery<UsersGroups> query = ParseQuery.getQuery(UsersGroups.class);
        query.whereEqualTo("group", chat);
        query.whereEqualTo("read", true);
        query.include("user");
        query.findInBackground(new FindCallback<UsersGroups>() {
            public void done(List<UsersGroups> itemList, ParseException e) {
                if (e == null) {
                    for(int i = 0; i < itemList.size(); i++) {
                        if(itemList.get(i).getUser() != null && !itemList.get(i).getUser().getUsername().equals(currentUser.getUsername())) {
                            itemList.get(i).setRead(false);
                            itemList.get(i).saveInBackground();
                        }
                    }
                } else {
                    Log.d("item", "Error: " + e.getMessage());
                }
            }
        });
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

                ParseQuery<ParseUser> query = ParseQuery.getQuery(ParseUser.class);
                query.whereEqualTo("objectId", ParseUser.getCurrentUser().getObjectId());

                query.findInBackground(new FindCallback<ParseUser>() {
                    public void done(List<ParseUser> objects, ParseException e) {
                        if (e == null) {
                            if (e==null && location.equals(objects.get(0).getString("location"))) {
                                addUserToGroup(user, chat);
                            }
                        } else {
                            Log.e("ParseOperations", "Failed to upload new group");
                        }
                    }
                });
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
        //usersGroups.setNotificationsOn(true);
        usersGroups.setRead(true);
        usersGroups.setChat(chat);
        try {
            usersGroups.save();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public static void changeUserLocation(final ParseUser user, final String location) {
        user.put("location", location);
        user.saveInBackground();
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

    //No longer used. Implemented in-class to run in background
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
        query.whereEqualTo("name", chatName);
        //query.whereFullText("location", location);
        try {
            query.whereEqualTo("location", location);
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
        query.whereEqualTo("group", chat);
        try {
            query.find().get(0).delete();
        } catch (ParseException e) {
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

/*    public static int getTotalPoints(ParseUser parseUser){
        ParseQuery<ParseUser> query = ParseQuery.getQuery(ParseUser.class);
        query.whereEqualTo("objectId", parseUser.getObjectId());
        try {
            List<ParseUser> result = query.find();
            return result.get(0).getInt("totalPoints");
        } catch(ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }
*/
    public static ParseUser getUserFromId(String userId){
        ParseQuery<ParseUser> query = ParseQuery.getQuery(ParseUser.class);
        query.whereEqualTo("objectId", userId);
        try {
            List<ParseUser> result = query.find();
            return result.get(0);
        } catch(ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /*
    public static ParseUser getPointsFromId(String userId){
        ParseQuery<ParseUser> query = ParseQuery.getQuery(ParseUser.class);
        query.whereEqualTo("objectId", userId);
        try {
            List<ParseUser> result = query.find();
            return result.get(0);
        } catch(ParseException e) {
            e.printStackTrace();
        }
        return null;
    } */

    //query.whereEqualTo("read", true);
    //        query.include("user");
}
