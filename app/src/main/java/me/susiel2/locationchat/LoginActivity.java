package me.susiel2.locationchat;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import me.susiel2.locationchat.database.ParseOperations;
import me.susiel2.locationchat.model.UsersPoints;



public class LoginActivity extends AppCompatActivity {


    private EditText phoneNumberInput;
    private EditText passwordInput;
    private Button loginBtn;
    private Button signUpBtn;
    TextView textInfo;
    EditText subEditText;
    public Spinner state_spinner;
    private int spinnerPosition;
    final String[] states = {"Alabama", "Alaska", "Arizona", "Arkansas", "California", "Colorado", "Connecticut", "Delaware", "District of Columbia", "Florida", "Georgia", "Hawaii", "Idaho", "Illinois", "Indiana", "Iowa", "Kansas", "Kentucky", "Louisiana", "Maine", "Maryland", "Massachusetts", "Michigan", "Minnesota",
            "Mississippi", "Missouri", "Montana", "Nebraska", "Nevada", "New Hampshire", "New Jersey", "New Mexico", "New York", "North Carolina", "North Dakota", "Ohio", "Oklahoma", "Oregon", "Pennsylvania", "Rhode Island", "South Carolina", "South Dakota", "Tennessee", "Texas", "Utah", "Vermont", "Virginia", "Washington", "West Virginia", "Wisconsin", "Wyoming"};
String text = "";



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


        //add this for potential spinner
        //states = getResources().getStringArray(R.array.states);
        state_spinner = subView.findViewById(R.id.state_spinner);
        Log.d("This is state spinner", ""+ state_spinner);
        ArrayAdapter<String> stateAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, states);
        state_spinner.setAdapter(stateAdapter);




        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Please Enter Your Display Name and Choose Your State");
        builder.setMessage("You can change location but not display name in the app.");
        builder.setView(subView);
        //AlertDialog alertDialog = builder.create();

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //textInfo.setText(subEditText.getText().toString());

                final String phoneNumber = phoneNumberInput.getText().toString();
                final String password = passwordInput.getText().toString();
                String name = subEditText.getText().toString();
                text = state_spinner.getSelectedItem().toString();
                Log.d("This is selected state", text);



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

        if(name.equals("")) {
            Toast.makeText(LoginActivity.this, "Please enter a display name", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create the ParseUser
        ParseUser user = new ParseUser();
        // Set core properties
        user.setUsername(phoneNumber);
        user.setPassword(password);
        user.put("totalPoints", 0);
        user.put("name", name);

        user.put("location", text);
        user.saveInBackground(new SaveCallback() {
            public void done(ParseException e) {
                if (e == null) {
                    //success, saved!
                    Log.d("MyApp", "Successfully saved!");
                } else {
                    //fail to save!
                    e.printStackTrace();
                }
            }
        });

        // Invoke signUpInBackground
        user.signUpInBackground(new SignUpCallback() {
            public void done(ParseException e) {
                if (e == null) {
                    // Hooray! Let them use the app now.
                    Log.d("LoginActivity", "Sign up successful");
                    Toast.makeText(LoginActivity.this, "Registration successful", Toast.LENGTH_SHORT).show();
                    ParseUser.logInInBackground(phoneNumber, password, new LogInCallback() {
                        @Override
                        public void done(ParseUser user, ParseException e) {
                            if (e == null) {
                                Log.d("LoginActivity", "Login successful");
                                // Inside a callback, so MainActivity.this

                                UsersPoints newUserPoints = new UsersPoints();
                                newUserPoints.setUser(user.getObjectId());
                                newUserPoints.setTotalPoints(0);
                                newUserPoints.saveInBackground();
                                Log.d("User total points", "" + newUserPoints.getTotalPoints());

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
                    if(e.getCode() == ParseException.USERNAME_TAKEN)
                        Toast.makeText(LoginActivity.this, "This phone number is already registered", Toast.LENGTH_SHORT).show();
                    else if(e.getCode() == ParseException.USERNAME_MISSING)
                        Toast.makeText(LoginActivity.this, "Please enter a phone number", Toast.LENGTH_SHORT).show();
                    else if(e.getCode() == ParseException.PASSWORD_MISSING)
                        Toast.makeText(LoginActivity.this, "Please enter a password", Toast.LENGTH_SHORT).show();
                    else
                        Toast.makeText(LoginActivity.this, "Please fill out all fields", Toast.LENGTH_SHORT).show();

                    Log.e("SignUpActivity", "Sign up failure: code " + e.getCode());
                    e.printStackTrace();
                }
            }
        });

    }

}
