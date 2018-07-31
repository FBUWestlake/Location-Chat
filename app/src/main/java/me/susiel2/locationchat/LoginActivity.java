package me.susiel2.locationchat;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;


public class LoginActivity extends AppCompatActivity {


    private EditText phoneNumberInput;
    private EditText passwordInput;
    private Button loginBtn;
    private Button signUpBtn;
    TextView textInfo;
    EditText subEditText;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // TODO - set up parse login
//        ParseUser currentUser = ParseUser.getCurrentUser();
        ParseUser currentUser = ParseUser.getCurrentUser();

        if (currentUser != null) {
            //HomeActivity is the main screen of the app, where we want users to go after logging in
            final Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }

        phoneNumberInput = findViewById(R.id.phoneNumber_et);
        passwordInput = findViewById(R.id.password_et);
        loginBtn = findViewById(R.id.login_btn);
        signUpBtn = findViewById(R.id.signUp_btn);
        textInfo = (TextView)findViewById(R.id.info);


        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String phoneNumber = phoneNumberInput.getText().toString();
                final String password = passwordInput.getText().toString();
                login(phoneNumber, password);
            }
        });

        signUpBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                //final String phoneNumber = phoneNumberInput.getText().toString();
                //final String password = passwordInput.getText().toString();

                openDialog();

            }
        });
    }
    
    private void openDialog(){
        LayoutInflater inflater = LayoutInflater.from(LoginActivity.this);
        View subView = inflater.inflate(R.layout.dialog_layout, null);
        final EditText subEditText = (EditText) subView.findViewById(R.id.dialogEditText);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Please Enter Your Display Name");
        builder.setMessage("You cannot change this later.");
        builder.setView(subView);
        //AlertDialog alertDialog = builder.create();

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //textInfo.setText(subEditText.getText().toString());

                final String phoneNumber = phoneNumberInput.getText().toString();
                final String password = passwordInput.getText().toString();
                String name = subEditText.getText().toString();

                dialog.dismiss();
                signUp(phoneNumber, password, name);
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(LoginActivity.this, "Cancel", Toast.LENGTH_LONG).show();
            }
        });

        AlertDialog alertDialog = builder.create();
        builder.show();

//        final String phoneNumber = phoneNumberInput.getText().toString();
//        final String password = passwordInput.getText().toString();
//        String name = subEditText.getText().toString();
//        signUp(phoneNumber, password, name);

    }

    private void login(String phoneNumber, String password) {

//        final Intent intent = new Intent(LoginActivity.this, MainActivity.class);
//        startActivity(intent);
//        finish();

        // TODO - set up parse login
        ParseUser.logInInBackground(phoneNumber, password, new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {
                if (e == null) {
                    Log.d("LoginActivity", "Login successful");
                    // Inside a callback, so MainActivity.this
                    final Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    Toast.makeText(LoginActivity.this, "Successfully logged in", Toast.LENGTH_SHORT).show();
                    startActivity(intent);
                    finish();
                } else {
                    Log.e("LoginActivity", "Login failure.");
                    Toast.makeText(LoginActivity.this, "Cannot identify login info", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        });
    }

    private void signUp(final String phoneNumber, final String password, String name) {
        // Create the ParseUser
        ParseUser user = new ParseUser();
        // Set core properties
        user.setUsername(phoneNumber);
        user.setPassword(password);
        user.put("name", name);

        // Invoke signUpInBackground
        user.signUpInBackground(new SignUpCallback() {
            public void done(ParseException e) {
                if (e == null) {
                    // Hooray! Let them use the app now.
                    Log.d("LoginActivity", "Sign up successful");
                    Toast.makeText(LoginActivity.this, "Successfully signed up", Toast.LENGTH_SHORT).show();
                    ParseUser.logInInBackground(phoneNumber, password, new LogInCallback() {
                        @Override
                        public void done(ParseUser user, ParseException e) {
                            if (e == null) {
                                Log.d("LoginActivity", "Login successful");
                                // Inside a callback, so MainActivity.this

                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(intent);
                                finish();
                            } else {
                                Log.e("LoginActivity", "Login failure.");
                                Toast.makeText(LoginActivity.this, "Cannot identify login info", Toast.LENGTH_SHORT).show();
                                e.printStackTrace();
                            }
                        }
                    });
                } else {
                    // Sign up didn't succeed. Look at the ParseException
                    // to figure out what went wrong
                    Log.e("SignUpActivity", "Sign up failure.");
                    e.printStackTrace();
                }
            }
        });

    }

}
