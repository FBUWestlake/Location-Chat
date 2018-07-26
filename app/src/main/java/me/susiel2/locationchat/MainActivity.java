package me.susiel2.locationchat;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;

import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseQuery;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import me.susiel2.locationchat.database.DatabaseHelper;
import me.susiel2.locationchat.database.ParseOperations;
import me.susiel2.locationchat.model.Chat;
import me.susiel2.locationchat.model.ChatAdapter;


public class MainActivity extends AppCompatActivity {

    //private DrawerLayout dl;
    private ImageView hamburger;
    private ImageView plusButton;
    private NavigationView nv;
    final String[] data = {"Help", "About", "Log Out"};
    final String[] states = {"Alabama", "Alaska", "Arizona", "Arkansas", "California", "Colorado", "Connecticut", "Delaware", "District of Columbia", "Florida", "Georgia", "Hawaii", "Idaho", "Illinois", "Indiana", "Iowa", "Kansas", "Kentucky", "Louisiana", "Maine", "Maryland", "Massachusetts", "Michigan", "Minnesota",
            "Mississippi", "Missouri", "Montana", "Nebraska", "Nevada", "New Hampshire", "New Jersey", "New Mexico", "New York", "North Carolina", "North Dakota", "Ohio", "Oklahoma", "Oregon", "Pennsylvania", "Rhode Island", "South Carolina", "South Dakota", "Tennessee", "Texas", "Utah", "Vermont", "Virginia", "Washington", "West Virginia", "Wisconsin", "Wyoming"};
    public DrawerLayout drawer;
    public ListView navList;
    public Spinner state_spinner;
    public RelativeLayout item_chat;

    private RecyclerView rv_chats;
    private ArrayList<Chat> chats;
    private ChatAdapter chatAdapter;
    DatabaseHelper usersDB;
    private int spinnerPosition;
    public RelativeLayout relativeLayout;

    //private Integer[] stateFlags = { R.drawable.bg_img_1, R.drawable.bg_img_2,
     //       R.drawable.bg_img_3, R.drawable.bg_img_4, R.drawable.bg_img_5 };

    private Integer[] stateFlags = { R.drawable.ic_alabama, R.drawable.ic_alaska, R.drawable.ic_arizona };

//    Chat mexicanFood = new Chat("Mexican Food", "https://leaf.nutrisystem.com/wp-content/uploads/2017/05/mexican.jpg",
//            "We love Mexican food!", "food", 400);
//
//    Chat acousticGuitar = new Chat("Acoustic Guitar", "https://cdn.mos.cms.futurecdn.net/oZr3irkSDKpSSjmFkpgP6K.jpg",
//            "For those who play and enjoy acoustic guitar", "music", 200);
//
//    Chat outdoorClimbing = new Chat("Outdoor Climbing", "http://www.cwexpeditions.net/includes/pics/gallery/1097.jpg",
//            "outdoor climbing enthusiasts!", "outdoors", 350);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);


        List<Chat> results = ParseOperations.getGroupsBySearch("original");
        Log.d("MainActivity", results.get(0).getObjectId());
        Log.d("MainActivity", "Num of members in this group: " + ParseOperations.getNumberOfMembersInGroup(results.get(0).getObjectId()));

        usersDB = new DatabaseHelper(this);

        state_spinner = findViewById(R.id.state_spinner);
        ArrayAdapter<String> stateAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, states);
        state_spinner.setAdapter(stateAdapter);

        //state_spinner.setSelection(4);


        Intent i = getIntent();
        String add = i.getStringExtra("myValue");

        //String myValue = "MT";
    //state_spinner.setSelection(getIndex(state_spinner, myValue));
        //ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.select_state, android.R.layout.simple_spinner_item);
        //adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //state_spinner.setAdapter(adapter);

        if (add != null) {
            spinnerPosition = stateAdapter.getPosition(add);
            state_spinner.setSelection(spinnerPosition);
        }
//second test here
        //ImageView imageview=(ImageView) findViewById(getResources().getIdentifier("imgView_"+i, "id", getPackageName()));
        //imageview.setImageResource(getResources().getIdentifier("img_"+i, "drawable",  getPackageName()));


        //test here
        relativeLayout = findViewById(R.id.relativeLayout);
        relativeLayout.setBackgroundResource(stateFlags[spinnerPosition]);

    /*
    //private method of your class
    private int getIndex(Spinner spinner, String myString){
        for (int i=0;i<spinner.getCount();i++){
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(myString)){
                return i;
            }
        }
        return 0;
    }
*/

        hamburger = findViewById(R.id.iv_hamburger);
        plusButton = findViewById(R.id.iv_addChat);
        drawer = findViewById(R.id.activity_main);

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
        chatAdapter = new ChatAdapter(chats, new ChatAdapter.ClickListener() {
            @Override
            public void onAddClicked(int position) {
                onChatClicked(position);
            }

            @Override
            public void onChatClicked(int position) {
                Intent i = new Intent(MainActivity.this, ChatActivity.class);
                i.putExtra("chat", Parcels.wrap(chats.get(position)));
                startActivity(i);
            }
        });

        rv_chats.setAdapter(chatAdapter);
        rv_chats.setLayoutManager(new LinearLayoutManager(this));

//        chats.add(mexicanFood);
//        chats.add(acousticGuitar);
//        chats.add(outdoorClimbing);
//        chatAdapter.notifyDataSetChanged();
        // TODO - populate list of chats that user is a part of, based on the user's location


        FloatingActionButton btn_maps = findViewById(R.id.mapsBtn);
        btn_maps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Intent intent = new Intent(MainActivity.this, MapDemoActivity.class);
                startActivity(intent);
            }
        });

//        dl.addDrawerListener(t);
//        t.syncState();
//
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//
//        nv = findViewById(R.id.nv);
//        nv.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
//            @Override
//            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//                int id = item.getItemId();
//                switch(id)
//                {
//                    case R.id.nav_help:
//                        Toast.makeText(MainActivity.this, "My Account",Toast.LENGTH_SHORT).show();
//                    case R.id.nav_about:
//                        Toast.makeText(MainActivity.this, "Settings",Toast.LENGTH_SHORT).show();
//                    case R.id.nav_log_out:
//                        Toast.makeText(MainActivity.this, "My Cart",Toast.LENGTH_SHORT).show();
//                    default:
//                        return true;
//                }
//            }
//        });




    }




    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == 25 && requestCode == 25) {
//            Chat chat = (Chat) Parcels.unwrap(data.getParcelableExtra("chat"));
//            if (chat != null) {
//                chat.setBitmapImage(BitmapFactory.decodeFile(chat.getImageFile().toString()));
//                chats.add(0, chat);
//                chatAdapter.notifyItemInserted(0);
//                Log.d("inserted chat", chat.getName());
//            } else {
//                Log.d("not inserted", "nono");
//            }
            // TODO - refresh feed or manually get new chat and add it to the adapter
        }
    }
}
