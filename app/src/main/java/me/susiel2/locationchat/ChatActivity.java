package me.susiel2.locationchat;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.support.design.widget.NavigationView;
import android.support.v4.content.FileProvider;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.omadahealth.github.swipyrefreshlayout.library.SwipyRefreshLayout;
import com.omadahealth.github.swipyrefreshlayout.library.SwipyRefreshLayoutDirection;
import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseLiveQueryClient;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SubscriptionHandling;

import org.json.JSONObject;
import org.parceler.Parcels;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;
import me.susiel2.locationchat.database.DatabaseHelper;
import me.susiel2.locationchat.database.ParseApp;
import me.susiel2.locationchat.database.ParseOperations;
import me.susiel2.locationchat.model.BitmapScaler;
import me.susiel2.locationchat.model.Chat;
import me.susiel2.locationchat.model.Message;
import me.susiel2.locationchat.model.MessageAdapter;
import me.susiel2.locationchat.model.UsersGroups;

public class ChatActivity extends AppCompatActivity {

    Chat chat;
    ParseOperations parseOperations;

    RecyclerView rvMessages;
    MessageAdapter mAdapter;
    List<Message> messages;
    EditText etMessage;
    ImageView ivSendButton;
    ImageView ivLogo;
    TextView tvTitle;
    ImageButton addAttachmentBtn;
    LinearLayout androidDropDownMenuIconItem;
    ImageView drawerGroupImage;
    TextView drawerGroupName;
    TextView drawerGroupDescription;
    TextView leaveGroup;
    SwipyRefreshLayout swipeUpRefresh;

    public static final int GET_FROM_GALLERY = 3;
    public final String APP_TAG = "MyCustomApp";
    public String photoFileName = "photo.jpg";
    File resizedFile;
    Bitmap bm_chatImage;
    DatabaseHelper dbHelper = new DatabaseHelper(this);
//    SwitchCompat switch_notifications;

    private Button gear;
//    private String chatID;
    final String[] listviewData = {"Leave Group"};
    public DrawerLayout drawer;
    public ListView navList2;

    @SuppressLint("RestrictedApi")
    @Override
    protected boolean onPrepareOptionsPanel(View view, Menu menu) {
        if (menu != null) {
            if (menu.getClass().getSimpleName().equals("MenuBuilder")) {
                try {
                    Method m = menu.getClass().getDeclaredMethod(
                            "setOptionalIconsVisible", Boolean.TYPE);
                    m.setAccessible(true);
                    m.invoke(menu, true);
                } catch (Exception e) {
                    Log.e(getClass().getSimpleName(), "onMenuOpened...unable to set icons for overflow menu", e);
                }
            }
        }
        return super.onPrepareOptionsPanel(view, menu);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        ParseObject.registerSubclass(Message.class);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        chat = Parcels.unwrap(getIntent().getParcelableExtra("chat"));
        Log.e("ChatActivity", "Chat object ID: " + chat.getObjectId());
        Log.e("ChatActivity", "Chat object ID: " + chat.getObjectId() + " " + chat.getName());
//        chatID = chat.getIdString();

//        final Handler handler = new Handler();
//        final Runnable refresh = new Runnable() {
//            @Override
//            public void run() {
//                Intent intent = new Intent(ChatActivity.this, ChatActivity.class);
//                finish();
//                startActivity(intent);
//                handler.postDelayed(this, 300);
//            }
//        };
//        handler.post(refresh);

        messages = new ArrayList<Message>();
        rvMessages = (RecyclerView) findViewById(R.id.recycler_chat);
        etMessage = (EditText) findViewById(R.id.edittext_chat);
        ivSendButton = (ImageView) findViewById(R.id.button_chat_send);
        ivLogo = (ImageView) findViewById(R.id.ivLogo);
        ivLogo.setImageBitmap(chat.getImageBitmap());
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        addAttachmentBtn = (ImageButton) findViewById(R.id.button_chat_upload);
        androidDropDownMenuIconItem = (LinearLayout) findViewById(R.id.horizontal_dropdown_icon_menu_items);
        drawerGroupImage = (ImageView) findViewById(R.id.groupImagePic);
        swipeUpRefresh = findViewById(R.id.swipeUpRefresh);


        Glide.with(getApplicationContext()).load(chat.getImageBitmap()).apply(RequestOptions.bitmapTransform(new RoundedCornersTransformation(25, 0, RoundedCornersTransformation.CornerType.ALL)))
                .into(drawerGroupImage);

        drawerGroupName = (TextView) findViewById(R.id.drawerChatName);
        drawerGroupName.setText(chat.getName());
        drawerGroupDescription = (TextView) findViewById(R.id.drawerChatDescription);
        drawerGroupDescription.setText(chat.getDescription());

        leaveGroup = (TextView) findViewById(R.id.leaveGroup);
        leaveGroup.setOnClickListener(new OnOneClickListener() {
            @Override
            public void onOneClick(View v) {
                parseOperations.leaveGroup(ParseUser.getCurrentUser(), chat);
                finish();
            }
        });


        mAdapter = new MessageAdapter(messages);
        rvMessages.setAdapter(mAdapter);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setStackFromEnd(true);
        rvMessages.setLayoutManager(manager);

//        String lastMessageTime = null;
//        if (dbHelper.isTableEmpty(dbHelper.TABLE_THREE_NAME) == false) {
//            Log.d("empty table", "table is not empty");
//            lastMessageTime = dbHelper.readLastMessageTime(chat.getObjectId());
//        }

        myHandler.postDelayed(mRefreshMessagesRunnable, POLL_INTERVAL);


        if (isNetworkAvailable()) {
            ParseQuery<Message> query1 = ParseQuery.getQuery(Message.class);
            query1.whereEqualTo("groupId", chat);
            query1.include("createdBy");
//            if (lastMessageTime != null) {
//                query1.whereGreaterThan("createdAt", lastMessageTime);
//                Log.e("last message exists", "this happens");
//            }
            try {
                List<Message> newMessages = query1.find();
                if (newMessages.size() != 0) {
                    dbHelper.addMessages(newMessages);
                    Log.e("Last Message", newMessages.get(newMessages.size() - 1).getContent());
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

//        List<Message> localMessages = dbHelper.readAllMessages(chat.getObjectId());
//
//        if (localMessages != null) {
//            for (int i = 0; i < localMessages.size(); i++) {
//                messages.add(localMessages.get(i));
//            }
//            mAdapter.notifyDataSetChanged();
//            rvMessages.scrollToPosition(messages.size() - 1);
//        }

        List<Message> localMessages = dbHelper.readAllMessages(chat.getObjectId());

        if (localMessages != null) {
            for (int i = 0; i < localMessages.size(); i++) {
                messages.add(localMessages.get(i));
                Log.e("adding local messages", localMessages.get(i).getBody());
            }
            mAdapter.notifyDataSetChanged();
            rvMessages.scrollToPosition(messages.size() - 1);
        }

        liveQuery();
        rvMessages.scrollToPosition(messages.size() - 1);

        ivSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Save message in parse.
                // Call create chat method.
                if (bm_chatImage == null) {
                    resizedFile = null;
                } else {
                    Bitmap bm_resized = BitmapScaler.scaleToFitWidth(bm_chatImage, 500);
                    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                    bm_resized.compress(Bitmap.CompressFormat.JPEG, 40, bytes);

                    resizedFile = getPhotoFileUri(photoFileName + "_resized");
                    try {
                        resizedFile.createNewFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    FileOutputStream fos;
                    try {
                        fos = new FileOutputStream(resizedFile);
                        fos.write(bytes.toByteArray());
                        fos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                String content = etMessage.getText().toString();
                if(!content.equals("") || resizedFile != null ) {
                    ParseFile pFile;
                    if(resizedFile != null)
                        pFile = new ParseFile(resizedFile);
                    else
                        pFile = null;
                    if (isNetworkAvailable()) {
                        Message newMessage = parseOperations.createMessageReturn(content, pFile, chat);
                        parseOperations.setMessagesToUnread(chat, ParseUser.getCurrentUser());
                        messages.add(newMessage);
                        mAdapter.notifyItemInserted(messages.size() - 1);
                        rvMessages.scrollToPosition(messages.size() - 1);
                    } else {
                        dbHelper.saveUnsentMessage(content, chat.getObjectId());
                        Message message = new Message(content, "not sent", "0");
                        Log.e("idgi", content);
                        messages.add(message);
                        mAdapter.notifyItemInserted(messages.size() - 1);
                        rvMessages.scrollToPosition(messages.size() - 1);
                    }
                    etMessage.setText("");
                    bm_chatImage = null;
                    addAttachmentBtn.setImageDrawable(getResources().getDrawable(R.drawable.ic_add));
                    addAttachmentBtn.setOnClickListener(new OnOneClickListener() {
                        @Override
                        public void onOneClick(View v) {
                            if (androidDropDownMenuIconItem.getVisibility() == View.VISIBLE) {
                                androidDropDownMenuIconItem.setVisibility(View.INVISIBLE);
                            } else {
                                androidDropDownMenuIconItem.setVisibility(View.VISIBLE);
                            }
                        }
                    });

                }
            }
        });

        tvTitle.setText(chat.getName());

        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listviewData);
        gear = findViewById(R.id.iv_gear);
        drawer = findViewById(R.id.activity_chat);

        addAttachmentBtn.setOnClickListener(new OnOneClickListener() {
            @Override
            public void onOneClick(View v) {
                if (androidDropDownMenuIconItem.getVisibility() == View.VISIBLE) {
                    androidDropDownMenuIconItem.setVisibility(View.INVISIBLE);
                } else {
                    androidDropDownMenuIconItem.setVisibility(View.VISIBLE);
                }
            }
        });

        gear.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if (drawer.isDrawerOpen(Gravity.RIGHT)) {
                    drawer.closeDrawer(Gravity.RIGHT);
                } else {
                    drawer.openDrawer(Gravity.RIGHT);
                }
            }
        });
    }

    static final int POLL_INTERVAL = 1000; // milliseconds
    Handler myHandler = new Handler();  // android.os.Handler
    Runnable mRefreshMessagesRunnable = new Runnable() {
        @Override
        public void run() {
            sendSavedMessages();
            myHandler.postDelayed(this, POLL_INTERVAL);
        }
    };


    public void sendSavedMessages() {
        if (!dbHelper.isTableEmpty(dbHelper.TABLE_FIVE_NAME) && isNetworkAvailable()) {
            List<String> unsentContent = dbHelper.readUnsentMessages(chat.getObjectId());
            Log.e("size of unsent", String.valueOf(unsentContent.size()));
            for (int i = 0; i < unsentContent.size(); i++) {
                Log.e("unsent message sent", "WORKS!!");
                parseOperations.createMessage(unsentContent.get(i), null, chat);
            }
            parseOperations.setMessagesToUnread(chat, ParseUser.getCurrentUser());

            ParseQuery<Message> query1 = ParseQuery.getQuery(Message.class);
            query1.whereEqualTo("groupId", chat);
            query1.include("createdBy");
//            if (lastMessageTime != null) {
//                query1.whereGreaterThan("createdAt", lastMessageTime);
//                Log.e("last message exists", "this happens");
//            }
            try {
                List<Message> newMessages = query1.find();
                // Need to add query for time of last message.
                if (newMessages.size() != 0) {
                    dbHelper.addMessages(newMessages);
                    Log.e("Last Message", newMessages.get(newMessages.size() - 1).getContent());
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }

            List<Message> localMessages = dbHelper.readAllMessages(chat.getObjectId());

            if (localMessages != null) {
                for (int i = 0; i < localMessages.size(); i++) {
                    messages.add(localMessages.get(i));
                    Log.e("adding local messages", localMessages.get(i).getBody());
                }
                mAdapter.notifyDataSetChanged();
                rvMessages.scrollToPosition(messages.size() - 1);
            }
            dbHelper.deleteTable(dbHelper.TABLE_FIVE_NAME);
            if (dbHelper.isTableEmpty(dbHelper.TABLE_FIVE_NAME)) {
                Log.e("deletion", "EMPTY");
            }
        }
    }

    void liveQuery() {
        chat = Parcels.unwrap(getIntent().getParcelableExtra("chat"));
        stopService(getIntent());
        ParseLiveQueryClient parseLiveQueryClient = ParseLiveQueryClient.Factory.getClient();
        ParseQuery<Message> parseQuery = ParseQuery.getQuery(Message.class);
//        parseQuery.whereNotEqualTo("createdBy", ParseUser.getCurrentUser()); TODO: figure out why this is not working.
        parseQuery.whereEqualTo("groupId", chat);
        parseQuery.include("createdBy");
        // This query can even be more granular (i.e. only refresh if the entry was added by some other user)
        // parseQuery.whereNotEqualTo(USER_ID_KEY, ParseUser.getCurrentUser().getObjectId());

        // Connect to Parse server
        SubscriptionHandling<Message> subscriptionHandling = parseLiveQueryClient.subscribe(parseQuery);

        // Listen for CREATE events
        subscriptionHandling.handleEvent(SubscriptionHandling.Event.CREATE, new
                SubscriptionHandling.HandleEventCallback<Message>() {
                    @Override

                    public void onEvent(ParseQuery<Message> query, final Message object) {
                        Log.e("created by livequery", object.getCreatedBy().getObjectId());
                        Log.e("current user livequery", ParseUser.getCurrentUser().getObjectId());
                        if (!object.getCreatedBy().getObjectId().equals(ParseUser.getCurrentUser().getObjectId())) {
                            messages.add(object);
                            // RecyclerView updates need to be run on the UI thread
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    messages.add(object);
                                    Log.d("livequery", object.getCreatedBy().getObjectId());
                                    Log.e("livequery", object.getContent());
                                    mAdapter.notifyItemInserted(messages.size() - 1);
                                    rvMessages.scrollToPosition(messages.size() - 1);
//                              mAdapter.notifyDataSetChanged();
                                }
                            });
                        }
                    }
                });
    }

    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //Detects request codes
        if(requestCode==GET_FROM_GALLERY && resultCode == Activity.RESULT_OK) {
            Uri selectedImage = data.getData();
            try {
                bm_chatImage = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                addAttachmentBtn.setImageDrawable(getResources().getDrawable(R.drawable.cancel_document));
                addAttachmentBtn.setOnClickListener(new OnOneClickListener() {
                    @Override
                    public void onOneClick(View v) {
                        bm_chatImage = null;
                        addAttachmentBtn.setImageDrawable(getResources().getDrawable(R.drawable.ic_add));
                        addAttachmentBtn.setOnClickListener(new OnOneClickListener() {
                            @Override
                            public void onOneClick(View v) {
                                if (androidDropDownMenuIconItem.getVisibility() == View.VISIBLE) {
                                    androidDropDownMenuIconItem.setVisibility(View.INVISIBLE);
                                } else {
                                    androidDropDownMenuIconItem.setVisibility(View.VISIBLE);
                                }
                            }
                        });
                    }
                });
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
            bm_chatImage = BitmapFactory.decodeFile(photoFile.getAbsolutePath());
            addAttachmentBtn.setImageDrawable(getResources().getDrawable(R.drawable.cancel_document));
            addAttachmentBtn.setOnClickListener(new OnOneClickListener() {
                @Override
                public void onOneClick(View v) {
                    bm_chatImage = null;
                    addAttachmentBtn.setImageDrawable(getResources().getDrawable(R.drawable.ic_add));
                    addAttachmentBtn.setOnClickListener(new OnOneClickListener() {
                        @Override
                        public void onOneClick(View v) {
                            if (androidDropDownMenuIconItem.getVisibility() == View.VISIBLE) {
                                androidDropDownMenuIconItem.setVisibility(View.INVISIBLE);
                            } else {
                                androidDropDownMenuIconItem.setVisibility(View.VISIBLE);
                            }
                        }
                    });
                }
            });
        }
    }

    public File getPhotoFileUri(String fileName) {
        // Get safe storage directory for photos
        // Use `getExternalFilesDir` on Context to access package-specific directories.
        // This way, we don't need to request external read/write runtime permissions.
        File mediaStorageDir = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), APP_TAG);

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()){
            Log.d(APP_TAG, "failed to create directory");
        }

        // Return the file target for the photo based on filename
        File file = new File(mediaStorageDir.getPath() + File.separator + fileName);

        return file;
    }

    public final static int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 1034;
    File photoFile;

    public void onLaunchCamera() {
        // create Intent to take a picture and return control to the calling application
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Create a File reference to access to future access
        photoFile = getPhotoFileUri(photoFileName);

        // wrap File object into a content provider
        // required for API >= 24
        // See https://guides.codepath.com/android/Sharing-Content-with-Intents#sharing-files-with-api-24-or-higher
        Uri fileProvider = FileProvider.getUriForFile(ChatActivity.this, "com.code.fileprovider", photoFile);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider);

        // If you call startActivityForResult() using an intent that no app can handle, your app will crash.
        // So as long as the result is not null, it's safe to use the intent.
        if (intent.resolveActivity(getPackageManager()) != null) {
            // Start the image capture intent to take photo
            startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
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

    public void menuItemClickTake(View view) {
        androidDropDownMenuIconItem.setVisibility(View.INVISIBLE);
        onLaunchCamera();
    }

    public void menuItemClickUpload(View view) {
        androidDropDownMenuIconItem.setVisibility(View.INVISIBLE);
        startActivityForResult(new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI), GET_FROM_GALLERY);
    }

    public void menuItemClickLocation(View view) {
        androidDropDownMenuIconItem.setVisibility(View.INVISIBLE);
        // TODO - offer either "current location" or "enter an address"
        //startActivityForResult(new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI), GET_FROM_GALLERY);
    }
}

