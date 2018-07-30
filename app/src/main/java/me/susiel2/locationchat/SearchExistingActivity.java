package me.susiel2.locationchat;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
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
    ArrayList<Chat> masterList;
    EditText etSearch;

    Button btNewGroup;
    Spinner categorySpinner;
    final String[] categories = {"All Categories", "food", "beauty", "tech", "sports", "art", "outdoors", "music"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_existing);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        rvChats = findViewById(R.id.rvExistingChats);
        chats = new ArrayList<Chat>();
        masterList = new ArrayList<Chat>();
        adapter = new ChatAdapter(chats, new ChatAdapter.ClickListener() {
            @Override
            public void onChatClicked(int position) {
                // TODO - Bring up a details page
                Log.e("SearchExistingActivity", "chat plus button wasnt clicked");
            }

            @Override
            public void onAddClicked(int position) {
                ParseOperations.addUserToGroup(ParseUser.getCurrentUser(), chats.get(position).getIdString());
                masterList.remove(chats.get(position));
                chats.remove(position);
                adapter.notifyDataSetChanged();
            }
        });
        btNewGroup = findViewById(R.id.btNewGroup);

        rvChats.setLayoutManager(new LinearLayoutManager(this));
        rvChats.setAdapter(adapter);

        categorySpinner = findViewById(R.id.categorySpinner);
        final ArrayAdapter<String> categoryAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categories);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(categoryAdapter);

        etSearch = findViewById(R.id.etSearch);
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                final String beforeText = etSearch.getText().toString();
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                        if(beforeText.equals(etSearch.getText().toString()))
                            updateChatsBySearchAndCategory();
                    }
                }, 300);
            }
        });

        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                updateChatsBySearchAndCategory();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });

        updateChatsUserIsNotIn();

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
        chats.clear();
        masterList.clear();
        for(int i = 0; i < currentGroups.size(); i++) {
            chats.add(currentGroups.get(i));
            masterList.add(currentGroups.get(i));
            Log.e("MainActivity","Chat name: " + currentGroups.get(i).getName());
        }
        adapter.notifyDataSetChanged();
    }

    public void updateChatsBySearchAndCategory(){
        ArrayList<Chat> tempList = new ArrayList<Chat>();
        for(int i = 0; i < masterList.size(); i++){
            if((categorySpinner.getSelectedItem().toString().equals("All Categories") || masterList.get(i).getCategory().equals(categorySpinner.getSelectedItem().toString())) &&
                    masterList.get(i).getName().toUpperCase().contains(etSearch.getText().toString().toUpperCase()))
                tempList.add(masterList.get(i));
        }
        if(!tempList.equals(chats)) {
            Log.e("SearchExistingActivity", "modifying chat list");
            chats.clear();
            for(int i = 0; i < tempList.size(); i++)
                chats.add(tempList.get(i));
            adapter.notifyDataSetChanged();
        }
        else
            Log.e("SearchExistingActivity", "no need for chat list modification");
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == 25 && requestCode == 25) {
            finish();
        }
    }

}
