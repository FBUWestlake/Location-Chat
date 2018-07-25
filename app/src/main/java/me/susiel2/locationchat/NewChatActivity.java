package me.susiel2.locationchat;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import com.parse.ParseFile;
import com.parse.ParseUser;

import org.parceler.Parcels;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import me.susiel2.locationchat.database.ParseOperations;
import me.susiel2.locationchat.model.BitmapScaler;
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
    public final String APP_TAG = "MyCustomApp";
    public String photoFileName = "photo.jpg";
    File resizedFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_chat);

        final String[] categories = {"food", "outdoors", "sports", "art", "music", "tech", "beauty"};

        btn_newChat = findViewById(R.id.btn_newChat);
        btn_upload = findViewById(R.id.btn_upload);
        et_chatName = findViewById(R.id.et_chatName);
        et_description = findViewById(R.id.et_chatName);

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

                ParseOperations.createGroup(et_chatName.getText().toString(), et_description.getText().toString(), resizedFile, selectedItemText, ParseUser.getCurrentUser(), /* TODO - create chat for every location */"California");

                // Intents and such to connect with MainActivity
                Intent intent = new Intent();
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
}
