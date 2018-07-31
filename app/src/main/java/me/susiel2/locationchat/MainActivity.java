package me.susiel2.locationchat;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseLiveQueryClient;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SubscriptionHandling;

import org.parceler.Parcels;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import java.util.ArrayList;
import java.util.List;

import me.susiel2.locationchat.database.DatabaseHelper;
import me.susiel2.locationchat.database.ParseOperations;
import me.susiel2.locationchat.model.Chat;
import me.susiel2.locationchat.model.ChatAdapter;
import me.susiel2.locationchat.model.Message;
import me.susiel2.locationchat.model.UsersGroups;
import static me.susiel2.locationchat.database.ParseOperations.getUsersName;



public class MainActivity extends AppCompatActivity {

    //private DrawerLayout dl;
    private ImageView hamburger;
    private ImageView plusButton;
    private EditText etSearchMain;
    private NavigationView nv;
    final String[] data = {"Help", "About"};
    final String[] states = {"Alabama", "Alaska", "Arizona", "Arkansas", "California", "Colorado", "Connecticut", "Delaware", "District of Columbia", "Florida", "Georgia", "Hawaii", "Idaho", "Illinois", "Indiana", "Iowa", "Kansas", "Kentucky", "Louisiana", "Maine", "Maryland", "Massachusetts", "Michigan", "Minnesota",
            "Mississippi", "Missouri", "Montana", "Nebraska", "Nevada", "New Hampshire", "New Jersey", "New Mexico", "New York", "North Carolina", "North Dakota", "Ohio", "Oklahoma", "Oregon", "Pennsylvania", "Rhode Island", "South Carolina", "South Dakota", "Tennessee", "Texas", "Utah", "Vermont", "Virginia", "Washington", "West Virginia", "Wisconsin", "Wyoming"};
    public DrawerLayout drawer;
    public ListView navList;
    public Spinner state_spinner;
    public RelativeLayout item_chat;

    private RecyclerView rv_chats;
    private ArrayList<Chat> chats;
    private ArrayList<Chat> masterList;
    private ChatAdapter chatAdapter;
    DatabaseHelper usersDB;
    private int spinnerPosition;
    public RelativeLayout relativeLayout;
    private Button logoutButton;
    SwipeRefreshLayout swipeContainer;
    TextView display_name;
    private Button deleteAccountButton;





    //private Integer[] stateFlags = { R.drawable.bg_img_1, R.drawable.bg_img_2,
     //       R.drawable.bg_img_3, R.drawable.bg_img_4, R.drawable.bg_img_5 };

    private Integer[] stateFlags = { R.drawable.ic_alabama, R.drawable.ic_alaska, R.drawable.ic_arizona,
    R.drawable.ic_arkansas, R.drawable.ic_ca, R.drawable.ic_colorado, R.drawable.ic_connecticut, R.drawable.ic_delaware, R.drawable.ic_district_of_columbia, R.drawable.ic_fl, R.drawable.ic_georgia,
            R.drawable.ic_hawaii, R.drawable.ic_idaho, R.drawable.ic_il, R.drawable.ic_indiana, R.drawable.ic_iowa, R.drawable.ic_kansas,
            R.drawable.ic_kentucky, R.drawable.ic_louisiana, R.drawable.ic_maine, R.drawable.ic_maryland, R.drawable.ic_massachusetts, R.drawable.ic_michigan,
            R.drawable.ic_mn, R.drawable.ic_mississippi, R.drawable.ic_missouri, R.drawable.ic_montana, R.drawable.ic_nebraska, R.drawable.ic_nv,
            R.drawable.ic_new_hampshire, R.drawable.ic_new_jersey, R.drawable.ic_new_mexico, R.drawable.ic_new_york, R.drawable.ic_nc, R.drawable.ic_north_dakota,
            R.drawable.ic_ohio, R.drawable.ic_oklahoma, R.drawable.ic_oregon, R.drawable.ic_pennsylvania, R.drawable.ic_ri, R.drawable.ic_south_carolina,
            R.drawable.ic_south_dakota, R.drawable.ic_tn, R.drawable.ic_texas, R.drawable.ic_utah, R.drawable.ic_vermont, R.drawable.ic_va, R.drawable.ic_washington,
            R.drawable.ic_west_virginia, R.drawable.ic_wisconsin, R.drawable.ic_wyoming
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        usersDB = new DatabaseHelper(this);

        state_spinner = findViewById(R.id.state_spinner);
        ArrayAdapter<String> stateAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, states);
        state_spinner.setAdapter(stateAdapter);
        state_spinner.setVisibility(View.GONE);


        Intent i = getIntent();
        String add = i.getStringExtra("myValue");

        if (add != null) {
            spinnerPosition = stateAdapter.getPosition(add);
            state_spinner.setSelection(spinnerPosition);
            
        }

        relativeLayout = findViewById(R.id.relativeLayout);
        relativeLayout.setBackgroundResource(stateFlags[spinnerPosition]);

    //test for spinner change here
        state_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                relativeLayout.setBackgroundResource(stateFlags[position]);


                ParseUser currentUser = ParseUser.getCurrentUser();
                String selected = state_spinner.getItemAtPosition(spinnerPosition).toString();
                currentUser.put("location", selected);
                currentUser.saveInBackground(new SaveCallback() {
                    public void done(ParseException e) {
                        if (e == null) {
                            //success, saved!
                            Log.d("MyApp", "Successfully saved!");
                        } else {
                            //fail to save!
                            e.printStackTrace();
                        }
                    }
                });
                //Log.d("tag", selected);
                //String test = getUserLocation(currentUser);
                //Log.d("tag 2", test);

            }


            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });

        hamburger = findViewById(R.id.iv_hamburger);
        plusButton = findViewById(R.id.iv_addChat);
        drawer = findViewById(R.id.activity_main);
        logoutButton = findViewById(R.id.logoutBtn);
        etSearchMain = findViewById(R.id.etSearchMain);
        deleteAccountButton = findViewById(R.id.deleteAccountBtn);

        
        display_name = findViewById(R.id.display_name);
        ParseUser currentUser = ParseUser.getCurrentUser();
        display_name = findViewById(R.id.display_name);
        display_name.setText(getUsersName(currentUser));

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, data);
        navList = findViewById(R.id.drawer);
        navList.setAdapter(adapter);
        navList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                drawer.addDrawerListener(new DrawerLayout.SimpleDrawerListener(){
                    @Override
                    public void onDrawerClosed(View drawerView) {
                        super.onDrawerClosed(drawerView);
                    }
                });
                drawer.closeDrawer(Gravity.LEFT);
            }
        });

        hamburger.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if (drawer.isDrawerOpen(Gravity.LEFT)) {
                    drawer.closeDrawer(Gravity.LEFT);
                } else {
                    drawer.openDrawer(Gravity.LEFT);
                }
            }
        });

        plusButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, SearchExistingActivity.class);
                startActivityForResult(i, 25);
            }
        });

        rv_chats = findViewById(R.id.rv_chats);
        chats = new ArrayList<>();
        masterList = new ArrayList<>();
        chatAdapter = new ChatAdapter(chats, new ChatAdapter.ClickListener() {
            @Override
            public void onAddClicked(int position) {
                onChatClicked(position);
            }

            @Override
            public void onChatClicked(int position) {
                Intent i = new Intent(MainActivity.this, ChatActivity.class);
                if (!ParseOperations.isChatRead(chats.get(position), ParseUser.getCurrentUser())) {
                    Log.e("MainActivity", "setting this chat to read.");
                    ParseOperations.setMessageAsReadInGroup(ParseUser.getCurrentUser(), chats.get(position));
                }
                i.putExtra("chat", Parcels.wrap(chats.get(position)));
                startActivityForResult(i, 26);
            }
        });

        rv_chats.setAdapter(chatAdapter);
        rv_chats.setLayoutManager(new LinearLayoutManager(this));

        etSearchMain.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                final String beforeText = etSearchMain.getText().toString();
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                        if(beforeText.equals(etSearchMain.getText().toString()))
                            updateBySearch();
                    }
                }, 300);
            }
        });

        FloatingActionButton btn_maps = findViewById(R.id.mapsBtn);
        btn_maps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Intent intent = new Intent(MainActivity.this, MapDemoActivity.class);
                startActivity(intent);
            }
        });
        
         logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParseUser.logOut();
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);            }
        });
        
        deleteAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParseUser.getCurrentUser().deleteInBackground();
                ParseUser.logOutInBackground();
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);            }
        });

        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list here.
                // Make sure you call swipeContainer.setRefreshing(false)
                // once the network request has completed successfully.
                updateChats();
            }
        });
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        updateChats();

    }

    public void updateChats(){
        ArrayList<Chat> currentGroups = ParseOperations.getGroupsUserIsIn(ParseUser.getCurrentUser());
        if(chats.size() == 0 || !currentGroups.get(0).getName().equals(chats.get(0).getName())) {
            chats.clear();
            masterList.clear();
            for (int i = 0; i < currentGroups.size(); i++) {
                chats.add(currentGroups.get(i));
                masterList.add(currentGroups.get(i));
            }
            chatAdapter.notifyDataSetChanged();
        }
        swipeContainer.setRefreshing(false);
        etSearchMain.setText("");
    }

    public void updateBySearch(){
        ArrayList<Chat> tempList = new ArrayList<Chat>();
        for(int i = 0; i < masterList.size(); i++){
            if(masterList.get(i).getName().toUpperCase().contains(etSearchMain.getText().toString().toUpperCase()))
                tempList.add(masterList.get(i));
        }
        if(!tempList.equals(chats)) {
            chats.clear();
            for(int i = 0; i < tempList.size(); i++)
                chats.add(tempList.get(i));
            chatAdapter.notifyDataSetChanged();
        }
        else
            Log.e("SearchExistingActivity", "no need for chat list modification");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        updateChats();
    }

}
