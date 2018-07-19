package me.susiel2.locationchat;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
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

import java.util.ArrayList;

import me.susiel2.locationchat.models.Message;

public class ChatActivity extends AppCompatActivity {

    RecyclerView rvMessages;
    MessageAdapter mAdapter;
    LinearLayoutManager mManager;
    ArrayList<Message> messages;
    EditText etMessage;
    ImageView ivSendButton;
    ImageView ivLogo;
    TextView tvTitle;

    private Button gear;
    private NavigationView nv2;
    final String[] data2 = {"Chat Group Name", "Leave Group"};
    public DrawerLayout drawer2;
    public ListView navList2;

    // Test message created as fake message data
    Message testMessage = new Message("This is a test message", "Jacob Snyder", "4:26 PM",
            "http://www.clker.com/cliparts/u/2/A/u/A/t/blank-profile-head-hi.png");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        messages = new ArrayList<Message>();

        rvMessages = (RecyclerView) findViewById(R.id.recycler_chat);
        etMessage = (EditText) findViewById(R.id.edittext_chat);
        ivSendButton = (ImageView) findViewById(R.id.button_chat_send);
        ivLogo = (ImageView) findViewById(R.id.ivLogo);
        tvTitle = (TextView) findViewById(R.id.tvTitle);

        mManager = new LinearLayoutManager(this);
        mManager.setReverseLayout(true);
        mAdapter = new MessageAdapter(messages);
        rvMessages.setAdapter(mAdapter);
        rvMessages.setLayoutManager(mManager);

        ivSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO - send the message typed into etMessage
                // TEST - adding a test message to the adapter
                messages.add(testMessage);
                mAdapter.notifyDataSetChanged();
                // END TEST
            }
        });

        // TODO - get chat title and image from Intent
        tvTitle.setText("Title goes here" /* TODO - title goes here */);
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

        //gear = findViewById(R.id.iv_gear);

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

        //gear = findViewById(R.id.iv_gear);

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

