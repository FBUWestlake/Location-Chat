package me.susiel2.locationchat;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
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
        tvTitle.setText("Title goes here" /* TODO - title goes here */ );
        // TODO - set ivLogo to bitmap from chat image
        Drawable logo = getResources().getDrawable(R.drawable.pepsi_logo);
        ivLogo.setImageDrawable(logo);
    }

    public void openSettingsMenu(MenuItem item){

        // TODO - open the settings menu

        return;
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

}
