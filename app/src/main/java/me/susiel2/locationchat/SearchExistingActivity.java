package me.susiel2.locationchat;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
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
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import me.susiel2.locationchat.database.ParseOperations;
import me.susiel2.locationchat.model.Chat;
import me.susiel2.locationchat.model.ChatAdapter;
import me.susiel2.locationchat.model.UsersGroups;

public class SearchExistingActivity extends AppCompatActivity {

    ChatAdapter adapter;
    RecyclerView rvChats;
    ArrayList<Chat> chats;
    ArrayList<Chat> masterList;
    EditText etSearch;

    TextView btNewGroup;
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
                ParseOperations.addUserToGroup(ParseUser.getCurrentUser(), chats.get(position));
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

        btNewGroup.setOnClickListener(new OnOneClickListener() {
            @Override
            public void onOneClick(View view) {
                Intent i = new Intent(SearchExistingActivity.this, NewChatActivity.class);
                startActivityForResult(i, 25);
            }
        });

    }

    public void updateChatsUserIsNotIn(){

        //Query for user location
        ParseQuery<ParseUser> query1 = ParseQuery.getQuery(ParseUser.class);
        query1.whereEqualTo("objectId", ParseUser.getCurrentUser().getObjectId());
        query1.findInBackground(new FindCallback<ParseUser>() {
            public void done(List<ParseUser> objects, ParseException e) {
                if (e == null) {
                    final String location = objects.get(0).getString("location");

                    //Query for groups user belongs to
                    ParseQuery<UsersGroups> query2 = ParseQuery.getQuery(UsersGroups.class);
                    query2.include("group").whereEqualTo("user", ParseUser.getCurrentUser()).addDescendingOrder("updatedAt");
                    query2.findInBackground(new FindCallback<UsersGroups>() {
                        public void done(List<UsersGroups> itemList, ParseException e) {
                            if (e == null) {

                                ArrayList<String> groupNames = new ArrayList<String>();
                                for(int i = 0; i < itemList.size(); i++) {
                                    groupNames.add(itemList.get(i).getChat().getName());
                                    Log.e("MainActivity","Chat name string: " + itemList.get(i).getChat().getName());
                                }

                                //Query for groups user isn't in
                                ParseQuery<Chat> query3 = ParseQuery.getQuery(Chat.class);
                                query3.whereNotContainedIn("name", Arrays.asList(groupNames));
                                query3.whereEqualTo("location", location);
                                query3.findInBackground(new FindCallback<Chat>() {
                                    @Override
                                    public void done(List<Chat> objects, ParseException e) {
                                        if(e == null){

                                            //If an update is needed, rebuild the list of chats and bind them
                                            if(chats.size() == 0 || (!objects.get(0).getName().equals(chats.get(0).getName()) || objects.size() != chats.size())){
                                                Log.e("SearchExistingActivity", "YES need for chat list modification.");
                                                chats.clear();
                                                masterList.clear();
                                                for (int j = 0; j < objects.size(); j++) {
                                                    chats.add(objects.get(j));
                                                    masterList.add(objects.get(j));
                                                    Log.e("SearchExistingActivity","Chat name: " + objects.get(j).getName());

                                                }
                                                adapter.notifyDataSetChanged();
                                            }

                                        }
                                    }
                                });

                            } else {
                                Log.d("item", "Error: " + e.getMessage());
                            }
                        }
                    });

                }
            }
        });

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

    public abstract class OnOneClickListener implements View.OnClickListener {
        private static final long MIN_CLICK_INTERVAL = 1000; //in millis
        private long lastClickTime = 0;

        @Override
        public final void onClick(View v) {
            long currentTime = SystemClock.elapsedRealtime();
            if (currentTime - lastClickTime > MIN_CLICK_INTERVAL) {
                lastClickTime = currentTime;
                onOneClick(v);
            }
        }

        public abstract void onOneClick(View v);
    }

}
