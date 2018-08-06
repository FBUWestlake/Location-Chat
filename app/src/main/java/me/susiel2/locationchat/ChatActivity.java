package me.susiel2.locationchat;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
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
//    SwitchCompat switch_notifications;

    public static final int GET_FROM_GALLERY = 3;
    public final String APP_TAG = "MyCustomApp";
    public String photoFileName = "photo.jpg";
    File resizedFile;
    Bitmap bm_chatImage;

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

        messages = new ArrayList<Message>();
        rvMessages = (RecyclerView) findViewById(R.id.recycler_chat);
        etMessage = (EditText) findViewById(R.id.edittext_chat);
        ivSendButton = (ImageView) findViewById(R.id.button_chat_send);
        ivLogo = (ImageView) findViewById(R.id.ivLogo);
        ivLogo.setImageBitmap(chat.getImageBitmap());
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        addAttachmentBtn = (ImageButton) findViewById(R.id.button_chat_upload);
        androidDropDownMenuIconItem = (LinearLayout) findViewById(R.id.horizontal_dropdown_icon_menu_items);


        ParseQuery<Message> query = ParseQuery.getQuery(Message.class);
        query.whereEqualTo("groupId", chat);
        query.findInBackground(new FindCallback<Message>() {
            public void done(List<Message> itemList, ParseException e) {
                if (e == null) {
                    for(int i = 0; i < itemList.size(); i++)
                        messages.add(itemList.get(i));
                    mAdapter.notifyDataSetChanged();
                    rvMessages.scrollToPosition(messages.size() - 1);
                } else {
                    Log.d("item", "Error: " + e.getMessage());
                }
            }
        });


        mAdapter = new MessageAdapter(messages);
        rvMessages.setAdapter(mAdapter);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setStackFromEnd(true);
        rvMessages.setLayoutManager(manager);

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
                    parseOperations.createMessage(content, pFile, chat);
                    parseOperations.setMessagesToUnread(chat, ParseUser.getCurrentUser());
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
        navList2 = findViewById(R.id.drawer2);
        navList2.setAdapter(adapter2);

        navList2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                parseOperations.leaveGroup(ParseUser.getCurrentUser(), chat);
                Intent intent = new Intent(ChatActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

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

    void liveQuery() {
        ParseLiveQueryClient parseLiveQueryClient = ParseLiveQueryClient.Factory.getClient();
        ParseQuery<Message> parseQuery = ParseQuery.getQuery(Message.class);
        parseQuery.whereNotEqualTo("user", ParseUser.getCurrentUser().getObjectId());
        // This query can even be more granular (i.e. only refresh if the entry was added by some other user)
        // parseQuery.whereNotEqualTo(USER_ID_KEY, ParseUser.getCurrentUser().getObjectId());

        // Connect to Parse server
        SubscriptionHandling<Message> subscriptionHandling = parseLiveQueryClient.subscribe(parseQuery);

        // Listen for CREATE events
        subscriptionHandling.handleEvent(SubscriptionHandling.Event.CREATE, new
                SubscriptionHandling.HandleEventCallback<Message>() {
                    @Override
                    public void onEvent(ParseQuery<Message> query, final Message object) {
                        
                        // RecyclerView updates need to be run on the UI thread
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                messages.add(object);
                                Log.d("livequery", "added");
                                mAdapter.notifyItemInserted(messages.size() - 1);
                                rvMessages.scrollToPosition(messages.size() - 1);
//                                mAdapter.notifyDataSetChanged();
                            }
                        });
                    }
                });
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

}

