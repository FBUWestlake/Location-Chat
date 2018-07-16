package me.susiel2.locationchat;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

public class ProfileActivity extends AppCompatActivity {

    private TextView userNameView;
    private ImageView userProfilePictureView;
    private TextView userInterests;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        userNameView = (TextView) findViewById(R.id.tvUsername);
        userProfilePictureView = (ImageView) findViewById(R.id.ivProfilePic);
        userInterests = (TextView) findViewById(R.id.tvInterests);

    }
}
