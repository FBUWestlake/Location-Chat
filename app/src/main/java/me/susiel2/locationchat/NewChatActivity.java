package me.susiel2.locationchat;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

public class NewChatActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_chat);

        final String[] categories = {"food", "outdoors", "sports", "art", "music", "tech", "beauty"};

        final Button btn_newChat = findViewById(R.id.btn_newChat);
        final EditText et_chatName = findViewById(R.id.et_chatName);

        Spinner spinner_category = findViewById(R.id.spinner_category);
        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<>(this, android.R.layout.select_dialog_item, categories);
        spinner_category.setPrompt("Category");
        spinner_category.setAdapter(categoryAdapter);
    }
}
