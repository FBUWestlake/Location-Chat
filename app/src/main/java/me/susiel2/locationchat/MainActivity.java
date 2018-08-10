package me.susiel2.locationchat;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.parceler.Parcels;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import me.susiel2.locationchat.database.DatabaseHelper;
import me.susiel2.locationchat.database.ParseOperations;
import me.susiel2.locationchat.model.Chat;
import me.susiel2.locationchat.model.ChatAdapter;
import me.susiel2.locationchat.model.UsersGroups;



public class MainActivity extends AppCompatActivity {

    private ImageView hamburger;
    private ImageView plusButton;
    private EditText etSearchMain;
    String[] states = null;
    public DrawerLayout drawer;
    public RelativeLayout item_chat;
    String location;

    private RecyclerView rv_chats;
    private ArrayList<Chat> chats;
    private ArrayList<Chat> masterList;
    private ArrayList<Boolean> isChatRead;
    private ChatAdapter chatAdapter;
    DatabaseHelper usersDB;
    private int spinnerPosition;
    //public RelativeLayout relativeLayout;
    private TextView logoutButton;
    SwipeRefreshLayout swipeContainer;
    TextView display_name;
    private TextView deleteAccountButton;
    private TextView locationChanger;
    private TextView displayState;




    //private Integer[] stateFlags = { R.drawable.bg_img_1, R.drawable.bg_img_2,
     //       R.drawable.bg_img_3, R.drawable.bg_img_4, R.drawable.bg_img_5 };

    /*
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
    */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        usersDB = new DatabaseHelper(this);
        states = getResources().getStringArray(R.array.states);
        ArrayAdapter<String> stateAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, states);

        hamburger = findViewById(R.id.iv_hamburger);
        plusButton = findViewById(R.id.iv_addChat);
        drawer = findViewById(R.id.activity_main);
        logoutButton = findViewById(R.id.logoutBtn);
        etSearchMain = findViewById(R.id.etSearchMain);
        deleteAccountButton = findViewById(R.id.deleteAccountBtn);
        locationChanger = findViewById(R.id.locationChanger);
        displayState = findViewById(R.id.display_state);

        display_name = findViewById(R.id.display_name);
        ParseUser currentUser = ParseUser.getCurrentUser();

        location = currentUser.getString("location");
        displayState.setText(location);

        ParseQuery<ParseUser> query = ParseQuery.getQuery(ParseUser.class);
        query.whereEqualTo("objectId", ParseUser.getCurrentUser().getObjectId());

        query.findInBackground(new FindCallback<ParseUser>() {
            public void done(List<ParseUser> objects, ParseException e) {
                if (e == null) {
                    display_name.setText(objects.get(0).getString("name"));
                } else {
                    // Something went wrong.
                }
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

        plusButton.setOnClickListener(new OnOneClickListener(){
            @Override
            public void onOneClick(View v) {
                Intent i = new Intent(MainActivity.this, SearchExistingActivity.class);
                startActivityForResult(i, 25);
            }

//            @Override
//            public void onClick(View view) {
//                Intent i = new Intent(MainActivity.this, SearchExistingActivity.class);
//                startActivityForResult(i, 25);
//            }
        });

        rv_chats = findViewById(R.id.rv_chats);
        chats = new ArrayList<>();
        masterList = new ArrayList<>();
        isChatRead = new ArrayList<>();
        chatAdapter = new ChatAdapter(chats, new ChatAdapter.ClickListener() {
            @Override
            public void onAddClicked(int position) {
                onChatClicked(position);
            }

            @Override
            public void onChatClicked(int position) {
                Intent i = new Intent(MainActivity.this, ChatActivity.class);
                ParseOperations.ifChatNotReadSetRead(chats.get(position), ParseUser.getCurrentUser());
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

        locationChanger.setText(location);
        locationChanger.setOnClickListener(new OnOneClickListener() {
            @Override
            public void onOneClick(View v) {
                final Intent intent = new Intent(MainActivity.this, MapDemoActivity.class);
                startActivityForResult(intent,26);
            }
        });
        
         logoutButton.setOnClickListener(new OnOneClickListener() {
            @Override
            public void onOneClick(View v) {

                //adding "are you sure you want to log out?" popup
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:
                                //Yes button clicked
                                ParseUser.logOut();
                                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                                startActivity(intent);
                                finish();
                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                //No button clicked
                                dialog.dismiss();
                                break;
                        }
                    }
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                builder.setMessage("Are you sure you want to logout?").setPositiveButton("Yes", dialogClickListener)
                        .setNegativeButton("No", dialogClickListener).show();

            }
        });
        
        deleteAccountButton.setOnClickListener(new OnOneClickListener() {
            @Override
            public void onOneClick(View v) {

                //adding "are you sure?" feature to delete account

                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:
                                //Yes button clicked
                                ParseQuery<UsersGroups> query = ParseQuery.getQuery(UsersGroups.class);
                                query.whereEqualTo("user", ParseUser.getCurrentUser()).addDescendingOrder("updatedAt");
                                query.findInBackground(new FindCallback<UsersGroups>() {
                                    public void done(List<UsersGroups> itemList, ParseException e) {
                                        if (e == null) {
                                            for(int i = 0; i < itemList.size(); i++)
                                                itemList.get(i).deleteInBackground();
                                            ParseUser.getCurrentUser().deleteInBackground();
                                            ParseUser.logOutInBackground();
                                            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                                            startActivity(intent);
                                            finish();
                                        } else {
                                            Log.d("item", "Error: " + e.getMessage());
                                        }
                                    }
                                });
                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                //No button clicked
                                dialog.dismiss();
                                break;
                        }
                    }
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                builder.setMessage("Are you sure you want to delete this account?").setPositiveButton("Yes", dialogClickListener)
                        .setNegativeButton("No", dialogClickListener).show();
            }
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
        Log.e("MainActivity", "updating chats now");
        ParseQuery<UsersGroups> query = ParseQuery.getQuery(UsersGroups.class);
        query.include("group").whereEqualTo("user", ParseUser.getCurrentUser()).addDescendingOrder("updatedAt");
        query.findInBackground(new FindCallback<UsersGroups>() {
            public void done(List<UsersGroups> itemList, ParseException e) {
                if (e == null) {
                    boolean refresh = false;
                    if(chats.size() == 0 || itemList.size() != chats.size())
                        refresh = true;
                    for(int i = 0; i < itemList.size(); i++) {
                        if(refresh)
                            break;
                        if(!itemList.get(i).getChat().getName().equals(chats.get(i).getName()) || itemList.get(i).isRead() != isChatRead.get(i) || !itemList.get(i).getChat().getLocation().equals(chats.get(i).getLocation())) {
                            refresh = true;
                            break;
                        }
                    }
                    if(refresh){
                        Log.e("MainActivity", "Refreshing chats");
                        chats.clear();
                        masterList.clear();
                        isChatRead.clear();
                        for (int j = 0; j < itemList.size(); j++) {
                            chats.add(itemList.get(j).getChat());
                            masterList.add(itemList.get(j).getChat());
                            isChatRead.add(itemList.get(j).isRead());
                        }
                        chatAdapter.notifyDataSetChanged();
                    }
                    else
                        Log.e("MainActivity", "No need to refresh chats");
                    swipeContainer.setRefreshing(false);
                } else {
                    Log.d("item", "Error: " + e.getMessage());
                }
            }
        });


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
        super.onActivityResult(requestCode, resultCode, data);

        //Detects request codes
        if(requestCode==26 && resultCode == Activity.RESULT_OK) {
            String newLocation = data.getStringExtra("location");
            if(!newLocation.equals("")) {
                location = newLocation;
                Log.e("MainActivity", "About change the location");
                ParseOperations.changeUserLocation(ParseUser.getCurrentUser(), location);
                locationChanger.setText(location);
                displayState.setText(location);
            }
            else
                Toast.makeText(MainActivity.this, "No new location selected", Toast.LENGTH_SHORT).show();
        }
        Log.e("MainActivity", "About to update the chats");
        updateChats();
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
