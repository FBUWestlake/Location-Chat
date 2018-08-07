package me.susiel2.locationchat;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseLiveQueryClient;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SubscriptionHandling;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import me.susiel2.locationchat.database.DatabaseHelper;
import me.susiel2.locationchat.database.ParseApp;
import me.susiel2.locationchat.database.ParseOperations;
import me.susiel2.locationchat.model.Chat;
import me.susiel2.locationchat.model.Message;
import me.susiel2.locationchat.model.MessageAdapter;
import me.susiel2.locationchat.model.UsersGroups;

public class ChatActivity extends AppCompatActivity {

    Chat chat;
    ParseOperations parseOperations;

    RecyclerView rvMessages;
    MessageAdapter mAdapter;
    List<Message> messages;
    EditText etMessage;
    ImageView ivSendButton;
    ImageView ivLogo;
    TextView tvTitle;
    DatabaseHelper dbHelper = new DatabaseHelper(this);
//    SwitchCompat switch_notifications;

    private Button gear;
//    private String chatID;
    final String[] listviewData = {"Leave Group"};
    public DrawerLayout drawer;
    public ListView navList2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        ParseObject.registerSubclass(Message.class);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        chat = Parcels.unwrap(getIntent().getParcelableExtra("chat"));
        Log.e("ChatActivity", "Chat object ID: " + chat.getObjectId() + " " + chat.getName());
//        chatID = chat.getIdString();

        messages = new ArrayList<Message>();
        rvMessages = (RecyclerView) findViewById(R.id.recycler_chat);
        etMessage = (EditText) findViewById(R.id.edittext_chat);
        ivSendButton = (ImageView) findViewById(R.id.button_chat_send);
        ivLogo = (ImageView) findViewById(R.id.ivLogo);
        ivLogo.setImageBitmap(chat.getImageBitmap());
        tvTitle = (TextView) findViewById(R.id.tvTitle);

        mAdapter = new MessageAdapter(messages);
        rvMessages.setAdapter(mAdapter);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setStackFromEnd(true);
        rvMessages.setLayoutManager(manager);

        String lastMessageTime;
        lastMessageTime = dbHelper.readLastMessageTime();
        List<Message> localMessages = dbHelper.readAllMessages(chat.getObjectId());

        if (localMessages != null) {
            for (int i = 0; i < localMessages.size(); i++) {
                messages.add(localMessages.get(i));
            }
            mAdapter.notifyDataSetChanged();
            rvMessages.scrollToPosition(messages.size() - 1);
        }

        ParseQuery<Message> query = ParseQuery.getQuery(Message.class);
        query.whereEqualTo("groupId", chat);
        if (lastMessageTime != null) {
            query.whereGreaterThan("createdAt", lastMessageTime);
        }
        query.findInBackground(new FindCallback<Message>() {
            public void done(List<Message> itemList, ParseException e) {
                if (itemList.size() > 0) {
                    for (int  i = 0; i < itemList.size(); i++) {
                        messages.add(itemList.get(i));
                        dbHelper.addMessages(messages);
                        mAdapter.notifyDataSetChanged();
                        rvMessages.scrollToPosition(messages.size() - 1);
                    }
                }
            }

        });

//        List<Message> localMessages = dbHelper.readAllMessages();
//        if (localMessages.size() != 0) {
//            Log.d("tryna figure it out", "here we are in if");
//            messages = localMessages;
//            mAdapter.notifyDataSetChanged();
//            rvMessages.scrollToPosition(messages.size() - 1);
//
//            ParseQuery<Message> query = ParseQuery.getQuery(Message.class);
//            query.whereEqualTo("groupId", chat);
//            query.whereGreaterThan("createdAt", dbHelper.readLastMessageTime());
//            query.findInBackground(new FindCallback<Message>() {
//                public void done(List<Message> itemList, ParseException e) {
//                    if (itemList.size() > 0) {
//                        for(int  i = 0; i < itemList.size(); i++)
//                            messages.add(itemList.get(i));
//                        dbHelper.addMessages(messages);
//                        mAdapter.notifyDataSetChanged();
//                        rvMessages.scrollToPosition(messages.size() - 1);
//                    }
//                }
//            });
//        } else {
//            Log.d("tryna figure it out", "here we are in else");
//            ParseQuery<Message> query = ParseQuery.getQuery(Message.class);
//            query.whereEqualTo("groupId", chat);
//            query.findInBackground(new FindCallback<Message>() {
//                public void done(List<Message> itemList, ParseException e) {
//                    if (e == null) {
//                        for (int i = 0; i < itemList.size(); i++)
//                            messages.add(itemList.get(i));
//                        dbHelper.addMessages(messages);
//                        mAdapter.notifyDataSetChanged();
//                        rvMessages.scrollToPosition(messages.size() - 1);
//                    } else {
//                        Log.d("item", "Error: " + e.getMessage());
//                    }
//                }
//            });
//        }

        liveQuery();
        rvMessages.scrollToPosition(messages.size() - 1);

        ivSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Save message in parse.
                String content = etMessage.getText().toString();
                if(!content.equals("")) {
                    parseOperations.createMessage(content, chat);
                    parseOperations.setMessagesToUnread(chat, ParseUser.getCurrentUser());
                    etMessage.setText(null);
                }
            }
        });

        tvTitle.setText(chat.getName());

        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listviewData);
        gear = findViewById(R.id.iv_gear);
        drawer = findViewById(R.id.activity_chat);
        navList2 = findViewById(R.id.drawer2);
        navList2.setAdapter(adapter2);

        navList2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                parseOperations.leaveGroup(ParseUser.getCurrentUser(), chat);
                Intent intent = new Intent(ChatActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

//        switch_notifications = findViewById(R.id.switch_notifications);
//        switch_notifications.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
//                if (b) {
//                    Log.d("switch", String.valueOf(b));
//                    parseOperations.setNotificationsForUserInGroup(b, ParseUser.getCurrentUser(), chatID);
//                } else {
//                    Log.d("switch", String.valueOf(b));
//                    parseOperations.setNotificationsForUserInGroup(b, ParseUser.getCurrentUser(), chatID);
//                }
//            }
//        });

        gear.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if (drawer.isDrawerOpen(Gravity.RIGHT)) {
                    drawer.closeDrawer(Gravity.RIGHT);
                } else {
                    drawer.openDrawer(Gravity.RIGHT);
                }
            }
        });
    }

    void liveQuery() {
        ParseLiveQueryClient parseLiveQueryClient = ParseLiveQueryClient.Factory.getClient();
        ParseQuery<Message> parseQuery = ParseQuery.getQuery(Message.class);
        parseQuery.whereNotEqualTo("user", ParseUser.getCurrentUser().getObjectId());
        // This query can even be more granular (i.e. only refresh if the entry was added by some other user)
        // parseQuery.whereNotEqualTo(USER_ID_KEY, ParseUser.getCurrentUser().getObjectId());

        // Connect to Parse server
        SubscriptionHandling<Message> subscriptionHandling = parseLiveQueryClient.subscribe(parseQuery);

        // Listen for CREATE events
        subscriptionHandling.handleEvent(SubscriptionHandling.Event.CREATE, new
                SubscriptionHandling.HandleEventCallback<Message>() {
                    @Override
                    public void onEvent(ParseQuery<Message> query, Message object) {
                        messages.add(object);
                        dbHelper.addMessage(object);
                        Log.d("livequery", "added");

                        // RecyclerView updates need to be run on the UI thread
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mAdapter.notifyItemInserted(messages.size() - 1);
                                rvMessages.scrollToPosition(messages.size() - 1);
                            }
                        });
                    }
                });
    }
}

