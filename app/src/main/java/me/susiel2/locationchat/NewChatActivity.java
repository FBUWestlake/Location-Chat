package me.susiel2.locationchat;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
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
import android.widget.Toast;

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

    ImageView iv_takePhoto;
    ImageView iv_selectPhoto;
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
        final String[] states = getResources().getStringArray(R.array.states);

        btn_newChat = findViewById(R.id.btn_newChat);
        iv_takePhoto = findViewById(R.id.iv_takePhoto);
        iv_selectPhoto = findViewById(R.id.iv_selectPhoto);
        et_chatName = findViewById(R.id.et_chatName);
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

        btn_newChat.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                // Call create chat method.
                if (bm_chatImage == null) {
                    iv_chatImage.buildDrawingCache();
                    bm_chatImage = iv_chatImage.getDrawingCache();
                }
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
                for (int i = 0; i < states.length; i++) {
                    ParseOperations.createGroup(et_chatName.getText().toString(), et_description.getText().toString(), resizedFile, selectedItemText, ParseUser.getCurrentUser(), states[i]);
                }
                // Intents and such to connect with MainActivity
                Intent intent = new Intent();
                setResult(25, intent);
                finish();
            }
        });

        iv_takePhoto.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                onLaunchCamera(iv_takePhoto);
            }
        });

        iv_selectPhoto.setOnClickListener(new View.OnClickListener() {

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
        } else if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
            bm_chatImage = BitmapFactory.decodeFile(photoFile.getAbsolutePath());
            iv_chatImage.setImageBitmap(bm_chatImage);
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

    public void onLaunchCamera(View view) {
        // create Intent to take a picture and return control to the calling application
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Create a File reference to access to future access
        photoFile = getPhotoFileUri(photoFileName);

        // wrap File object into a content provider
        // required for API >= 24
        // See https://guides.codepath.com/android/Sharing-Content-with-Intents#sharing-files-with-api-24-or-higher
        Uri fileProvider = FileProvider.getUriForFile(NewChatActivity.this, "com.code.fileprovider", photoFile);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider);

        // If you call startActivityForResult() using an intent that no app can handle, your app will crash.
        // So as long as the result is not null, it's safe to use the intent.
        if (intent.resolveActivity(getPackageManager()) != null) {
            // Start the image capture intent to take photo
            startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
        }
    }

}
