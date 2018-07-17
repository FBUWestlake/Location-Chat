package me.susiel2.locationchat;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import java.util.ArrayList;

import me.susiel2.locationchat.models.Message;

public class ChatActivity extends AppCompatActivity {

    RecyclerView rvMessages;
    MessageAdapter mAdapter;
    LinearLayoutManager mManager;
    ArrayList<Message> messages;
    EditText etMessage;
    ImageView ivSendButton;

    Message testMessage = new Message("This is a test message", "Jacob Snyder", "4:26 PM",
            "http://www.clker.com/cliparts/u/2/A/u/A/t/blank-profile-head-hi.png");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        messages = new ArrayList<Message>();

        rvMessages = (RecyclerView) findViewById(R.id.recycler_chat);
        etMessage = (EditText) findViewById(R.id.edittext_chat);
        ivSendButton = (ImageView) findViewById(R.id.button_chat_send);

        mManager = new LinearLayoutManager(this);
        mManager.setReverseLayout(true);
        mAdapter = new MessageAdapter(messages);
        rvMessages.setAdapter(mAdapter);
        rvMessages.setLayoutManager(mManager);

        ivSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // TODO - send the message typed into etMessage
                //TEST - adding a test message to the adapter
                messages.add(testMessage);
                mAdapter.notifyDataSetChanged();
                //END TEST
            }
        });

    }

}
