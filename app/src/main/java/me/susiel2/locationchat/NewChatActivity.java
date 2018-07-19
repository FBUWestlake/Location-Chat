package me.susiel2.locationchat;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import me.susiel2.locationchat.model.Chat;

public class NewChatActivity extends AppCompatActivity {

    Button btn_newChat;
    ImageView iv_chatImage;
    EditText et_chatName;
    String selectedItemText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_chat);

        final String[] categories = {"food", "outdoors", "sports", "art", "music", "tech", "beauty"};

        btn_newChat = findViewById(R.id.btn_newChat);
        et_chatName = findViewById(R.id.et_chatName);
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
                createChat(et_chatName.getText().toString(), 0, "https://images-na.ssl-images-amazon.com/images/I/51io9pmG2QL._SL1072_.jpg", selectedItemText);
                // Intents and such to connect with MainActivity
            }
        });
    }

    public void createChat(String chatName, int numberOfMembers, String image, String category) {
        final Chat newChat = new Chat();
        newChat.setName(chatName);
        newChat.setNumberOfMembers(numberOfMembers);
        newChat.setImageUrl(image);
        newChat.setCategory(category);

        // Add to db.
    }
}
