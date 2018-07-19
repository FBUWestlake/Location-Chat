package me.susiel2.locationchat;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import org.parceler.Parcels;

import java.io.FileNotFoundException;
import java.io.IOException;

import me.susiel2.locationchat.model.Chat;

public class NewChatActivity extends AppCompatActivity {

    Button btn_upload;
    Button btn_newChat;
    ImageView iv_chatImage;
    Bitmap bm_chatImage;
    EditText et_chatName;
    EditText et_description;
    String selectedItemText;
    private Context context;
    public static final int GET_FROM_GALLERY = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_chat);

        final String[] categories = {"food", "outdoors", "sports", "art", "music", "tech", "beauty"};

        btn_newChat = findViewById(R.id.btn_newChat);
        btn_upload = findViewById(R.id.btn_upload);
        et_chatName = findViewById(R.id.et_description);
        et_description = findViewById(R.id.et_description);

        //TODO: allow user to upload image for chat group.
        iv_chatImage = findViewById(R.id.iv_chatImage);

        Spinner spinner_category = findViewById(R.id.spinner_category);
        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, categories);
        spinner_category.setAdapter(categoryAdapter);
        spinner_category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedItemText = adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        //btn_upload.setOnClickListener(new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI), GET_FROM_GALLERY);));

        btn_newChat.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                // Call create chat method.
                Chat chat = new Chat(et_chatName.getText().toString(), bm_chatImage, et_description.getText().toString(), selectedItemText, 0);
                // Intents and such to connect with MainActivity
                //createChat(et_chatName.getText().toString(), iv_chatImage, selectedItemText);
                Intent intent = new Intent();
                intent.putExtra("chat", Parcels.wrap(chat));
                setResult(25, intent);
                finish();
            }
        });

        btn_upload.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI), GET_FROM_GALLERY);
            }
        });
    }

//    public Chat createChat(String chatName, Bitmap bitmap, String category, int numberOfMembers) {
//        final Chat newChat = new Chat();
//        newChat.setName(chatName);
//        newChat.setNumberOfMembers(numberOfMembers);
//        newChat.setBitmapImage(bitmap);
//        newChat.setCategory(category);
//
//        return newChat;
//
//        // Add to db.
//    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //Detects request codes
        if(requestCode==GET_FROM_GALLERY && resultCode == Activity.RESULT_OK) {
            Uri selectedImage = data.getData();
            try {
                bm_chatImage = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                iv_chatImage.setImageBitmap(bm_chatImage);
            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
}
