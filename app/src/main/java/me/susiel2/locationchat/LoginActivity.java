package me.susiel2.locationchat;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.parse.ParseUser;


public class LoginActivity extends AppCompatActivity {


    private EditText usernameInput;
    private EditText passwordInput;
    private Button loginBtn;
    private Button signUpBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // TODO - set up parse login
//        ParseUser currentUser = ParseUser.getCurrentUser();
        ParseUser currentUser = null;

        if (currentUser != null) {
            //HomeActivity is the main screen of the app, where we want users to go after logging in
            final Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }

        usernameInput = findViewById(R.id.username_et);
        passwordInput = findViewById(R.id.password_et);
        loginBtn = findViewById(R.id.login_btn);
        signUpBtn = findViewById(R.id.signUp_btn);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String username = usernameInput.getText().toString();
                final String password = passwordInput.getText().toString();
                login(username, password);
            }
        });

//the sign up button doesnt to anything in our app b/c we're building the MVP
        /*signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Intent i = new Intent(LoginActivity.this, CreateAccountActivity.class);
                startActivity(i);
            }
        });*/
    }

    private void login(String username, String password) {

        final Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish();

        // TODO - set up parse login
//        ParseUser.logInInBackground(username, password, new LogInCallback() {
//            @Override
//            public void done(ParseUser user, ParseException e) {
//                if (e == null) {
//                    Log.d("LoginActivity", "Login successful");
//
//                    //HomeActivity is the main screen of the app, where we want users to go after logging in
//                    final Intent intent = new Intent(LoginActivity.this, ChatActivity.class);
//                    startActivity(intent);
//                    finish();
//                } else {
//                    Log.e("LoginActivity", "Login failure");
//                    e.printStackTrace();
//                }
//            }
//        });
    }

}
