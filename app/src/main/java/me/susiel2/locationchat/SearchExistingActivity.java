package me.susiel2.locationchat;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import org.parceler.Parcels;

import java.util.ArrayList;

import me.susiel2.locationchat.model.Chat;
import me.susiel2.locationchat.model.ChatAdapter;

public class SearchExistingActivity extends AppCompatActivity {

    ChatAdapter adapter;
    RecyclerView rvChats;
    ArrayList<Chat> chats;

    Button btNewGroup;
    Spinner categorySpinner;
    final String[] categories = {"All Categories", "Food", "Entertainment", "Work"};

    // Mock data
    Chat mockChat = new Chat("Mock Chat", "https://images-na.ssl-images-amazon.com/images/I/51io9pmG2QL._SL1072_.jpg", "This is a chat for hot dogs", "Dogs", 400);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_existing);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        rvChats = findViewById(R.id.rvExistingChats);
        chats = new ArrayList<Chat>();
        adapter = new ChatAdapter(chats, new ChatAdapter.ClickListener() {
            @Override
            public void onChatClicked(int position) {
                // TODO - add this chat to list of joined chats (or maybe bring up a screen that gives you details about the chat)
            }
        });
        btNewGroup = findViewById(R.id.btNewGroup);

        rvChats.setLayoutManager(new LinearLayoutManager(this));
        rvChats.setAdapter(adapter);

        categorySpinner = findViewById(R.id.categorySpinner);
        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categories);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(categoryAdapter);

        // Mock data
        chats.add(mockChat);
        chats.add(mockChat);
        chats.add(mockChat);
        adapter.notifyDataSetChanged();
        // End mock data

        btNewGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(SearchExistingActivity.this, NewChatActivity.class);
                startActivityForResult(i, 25);
            }
        });

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == 25 && requestCode == 25) {
            //Chat chat = data.getParcelableExtra("chat");
            setResult(25, data);
            finish();
        }
    }

}
