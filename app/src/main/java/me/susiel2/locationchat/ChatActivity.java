package me.susiel2.locationchat;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
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

import me.susiel2.locationchat.database.ParseApp;
import me.susiel2.locationchat.database.ParseOperations;
import me.susiel2.locationchat.model.Chat;
import me.susiel2.locationchat.model.Message;
import me.susiel2.locationchat.model.MessageAdapter;

public class ChatActivity extends AppCompatActivity {

    Chat chat;
    ParseOperations parseOperations;

    RecyclerView rvMessages;
    MessageAdapter mAdapter;
    LinearLayoutManager mManager;
    List<Message> messages;
    EditText etMessage;
    ImageView ivSendButton;
    ImageView ivLogo;
    TextView tvTitle;
    boolean firstLoad;

    private Button gear;
    private NavigationView nv2;
    private String chatID;
    //final String[] data2 = {"Chat Group Name", "Leave Group"};
    final String[] data2 = {"Leave Group"};    
    public DrawerLayout drawer2;
    public ListView navList2;

    // Test message created as fake message data
//    Message testMessage = new Message("This is a test message", "Jacob Snyder", "4:26 PM",
//            "http://www.clker.com/cliparts/u/2/A/u/A/t/blank-profile-head-hi.png");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        ParseObject.registerSubclass(Message.class);

        chat = Parcels.unwrap(getIntent().getParcelableExtra("chat"));
        chatID = chat.getIdString();
        //final Message chatMessage = new Message(chat.getDescription(), chat.getName(), chat.getCategory(), chat.getImageUrl());

        messages = new ArrayList<Message>();

        rvMessages = (RecyclerView) findViewById(R.id.recycler_chat);
        etMessage = (EditText) findViewById(R.id.edittext_chat);
        ivSendButton = (ImageView) findViewById(R.id.button_chat_send);
        ivLogo = (ImageView) findViewById(R.id.ivLogo);
        tvTitle = (TextView) findViewById(R.id.tvTitle);

        mManager = new LinearLayoutManager(this);
        messages = parseOperations.getGroupMessages(chatID);
        mAdapter = new MessageAdapter(messages);
        rvMessages.setAdapter(mAdapter);
        rvMessages.setLayoutManager(mManager);

//        mAdapter = new MessageAdapter(messages);
//        rvMessages.setAdapter(mAdapter);
//        rvMessages.setLayoutManager(mManager);
        firstLoad = true;

        ParseLiveQueryClient parseLiveQueryClient = ParseLiveQueryClient.Factory.getClient();

        ParseQuery<Message> parseQuery = ParseQuery.getQuery(Message.class);
        // This query can even be more granular (i.e. only refresh if the entry was added by some other user)
        // parseQuery.whereNotEqualTo(USER_ID_KEY, ParseUser.getCurrentUser().getObjectId());

        // Connect to Parse server
        SubscriptionHandling<Message> subscriptionHandling = parseLiveQueryClient.subscribe(parseQuery);

        // Listen for CREATE events
        subscriptionHandling.handleEvent(SubscriptionHandling.Event.CREATE, new
                SubscriptionHandling.HandleEventCallback<Message>() {
                    @Override
                    public void onEvent(ParseQuery<Message> query, Message object) {
                        messages.add(0, object);

                        // RecyclerView updates need to be run on the UI thread
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mAdapter.notifyDataSetChanged();
                                rvMessages.scrollToPosition(0);
                            }
                        });
                    }
                });

        rvMessages.scrollToPosition(messages.size() - 1);


        ivSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO - send the message typed into etMessage
                // TEST - adding a test message to the adapter
//                messages.add(chatMessage);
//                mAdapter.notifyDataSetChanged();
                // END TEST

                // Save message in parse.
                String content = etMessage.getText().toString();
                // TODO: need "get specific group" task to create message successfully
                messages.add(parseOperations.createMessage(content, chatID));
                mAdapter.notifyItemInserted(messages.size() - 1);
                rvMessages.scrollToPosition(messages.size() - 1);
                parseOperations.setMessagesToUnread(chatID);
                etMessage.setText(null);
            }
        });

        tvTitle.setText(chat.getName());
        // TODO - set ivLogo to bitmap from chat image
        Drawable logo = getResources().getDrawable(R.drawable.pepsi_logo);
        ivLogo.setImageDrawable(logo);

        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, data2);
        gear = findViewById(R.id.iv_gear);
        drawer2 = findViewById(R.id.activity_chat);
        navList2 = findViewById(R.id.drawer2);
        navList2.setAdapter(adapter2);
        navList2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                drawer2.addDrawerListener(new DrawerLayout.SimpleDrawerListener() {
                    @Override
                    public void onDrawerClosed(View drawerView) {
                        super.onDrawerClosed(drawerView);
                    }
                });
                drawer2.closeDrawer(Gravity.RIGHT);
            }
        });

        gear.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if (drawer2.isDrawerOpen(Gravity.RIGHT)) {
                    drawer2.closeDrawer(Gravity.RIGHT);
                } else {
                    drawer2.openDrawer(Gravity.RIGHT);
                }
            }
        });
    }

//    void refreshMessages() {
//        ParseQuery<Message> query = ParseQuery.getQuery(Message.class);
//        // Configure limit and sort order
//        query.setLimit(60);
//
//        // get the latest 50 messages, order will show up newest to oldest of this group
//        query.orderByDescending("createdAt");
//        // Execute query to fetch all messages from Parse asynchronously
//        // This is equivalent to a SELECT query with SQL
//        query.findInBackground(new FindCallback<Message>() {
//            public void done(List<Message> messagesList, ParseException e) {
//                if (e == null) {
//                    messages.clear();
//                    messages.addAll(messagesList);
//                    mAdapter.notifyDataSetChanged(); // update adapter
//                    // Scroll to the bottom of the list on initial load
//                    if (firstLoad) {
//                        rvMessages.scrollToPosition(0);
//                        firstLoad = false;
//                    }
//                    Log.d("message", "messages loaded");
//                    Log.d("message", String.valueOf(messages.size()));
//                } else {
//                    Log.e("message", "Error Loading Messages" + e);
//                }
//            }
//        });
//
//    }

//    void liveQuery() {
//        ParseLiveQueryClient parseLiveQueryClient = ParseLiveQueryClient.Factory.getClient();
//
//        ParseQuery<Message> parseQuery = ParseQuery.getQuery(Message.class);
//        // This query can even be more granular (i.e. only refresh if the entry was added by some other user)
//        // parseQuery.whereNotEqualTo(USER_ID_KEY, ParseUser.getCurrentUser().getObjectId());
//
//        // Connect to Parse server
//        SubscriptionHandling<Message> subscriptionHandling = parseLiveQueryClient.subscribe(parseQuery);
//
//        // Listen for CREATE events
//        subscriptionHandling.handleEvent(SubscriptionHandling.Event.CREATE, new
//                SubscriptionHandling.HandleEventCallback<Message>() {
//                    @Override
//                    public void onEvent(ParseQuery<Message> query, Message object) {
//                        messages.add(0, object);
//
//                        // RecyclerView updates need to be run on the UI thread
//                        runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                mAdapter.notifyDataSetChanged();
//                                rvMessages.scrollToPosition(0);
//                            }
//                        });
//                    }
//                });
//    }

    public void openSettingsMenu(MenuItem item) {

        // TODO - open the settings menu

        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, data2);
        gear = findViewById(R.id.iv_gear);
        //drawer2 = findViewById(R.id.activity_chat);
        navList2 = findViewById(R.id.drawer2);
        navList2.setAdapter(adapter2);
        navList2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                drawer2.addDrawerListener(new DrawerLayout.SimpleDrawerListener() {
                    @Override
                    public void onDrawerClosed(View drawerView) {
                        super.onDrawerClosed(drawerView);
                    }
                });
                drawer2.closeDrawer(Gravity.RIGHT);
            }
        });

        gear.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if (drawer2.isDrawerOpen(Gravity.RIGHT)) {
                    drawer2.closeDrawer(Gravity.RIGHT);
                } else {
                    drawer2.openDrawer(Gravity.RIGHT);
                }
            }
        });
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    //        dl.addDrawerListener(t);
//        t.syncState();
//
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//
//        nv2 = findViewById(R.id.nv2);
//        nv2.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
//            @Override
//            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//                int id = item.getItemId();
//                switch(id)
//                {
//                    case R.id.nav_chat_name:
//                        Toast.makeText(MainActivity.this, "Chat Name",Toast.LENGTH_SHORT).show();
//                    case R.id.nav_notifs:
//                        Toast.makeText(MainActivity.this, "Notifications",Toast.LENGTH_SHORT).show();
//                    case R.id.nav_leave_group:
//                        Toast.makeText(MainActivity.this, "Leave Group",Toast.LENGTH_SHORT).show();
//                    default:
//                        return true;
//                }
//            }
//        });


//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//
//        if(t.onOptionsItemSelected(item))
//            return true;
//
//        return super.onOptionsItemSelected(item);
//    }

}

