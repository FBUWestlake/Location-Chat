package me.susiel2.locationchat;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.parse.Parse;
import com.parse.ParseUser;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import me.susiel2.locationchat.database.ParseOperations;
import me.susiel2.locationchat.model.Chat;
import me.susiel2.locationchat.model.ChatAdapter;

public class SearchExistingActivity extends AppCompatActivity {

    ChatAdapter adapter;
    RecyclerView rvChats;
    ArrayList<Chat> chats;

    Button btNewGroup;
    Spinner categorySpinner;
    final String[] categories = {"All Categories", "Food", "Entertainment", "Work"};

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
                // TODO - Bring up a details page
                Log.e("SearchExistingActivity", "chat wasnt clicked");

            }

            @Override
            public void onAddClicked(int position) {
                ParseOperations.addUserToGroup(ParseUser.getCurrentUser(), chats.get(position).getIdString());
                chats.remove(position);
                adapter.notifyDataSetChanged();
                // TODO - add chat to list of added chats and remove it from this list
                Log.e("SearchExistingActivity", "chat is added blah blah blah");
            }
        });
        btNewGroup = findViewById(R.id.btNewGroup);

        rvChats.setLayoutManager(new LinearLayoutManager(this));
        rvChats.setAdapter(adapter);

        categorySpinner = findViewById(R.id.categorySpinner);
        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categories);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(categoryAdapter);

        updateChatsUserIsNotIn();
        // TODO - Populate list of chats that current user isn't in

        btNewGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(SearchExistingActivity.this, NewChatActivity.class);
                startActivityForResult(i, 25);
            }
        });

    }

    public void updateChatsUserIsNotIn(){
        List<Chat> currentGroups = ParseOperations.getGroupsUserIsNotIn(ParseUser.getCurrentUser());
        Log.e("MainActivity","Number of Chats : " + currentGroups.size());
        for(int i = 0; i < currentGroups.size(); i++) {
            chats.add(currentGroups.get(i));
            Log.e("MainActivity","Chat name: " + currentGroups.get(i).getName());
        }
        adapter.notifyDataSetChanged();
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == 25 && requestCode == 25) {
            finish();
        }
    }

}
