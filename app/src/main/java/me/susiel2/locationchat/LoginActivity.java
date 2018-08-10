package me.susiel2.locationchat;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;

import me.susiel2.locationchat.database.ParseOperations;
import me.susiel2.locationchat.model.UsersPoints;



public class LoginActivity extends AppCompatActivity {


    private EditText phoneNumberInput;
    private EditText passwordInput;
    private TextView loginBtn;
    private TextView signUpBtn;
    EditText subEditText;
    public Spinner state_spinner;
    private int spinnerPosition;
    final String[] states = {"Alabama", "Alaska", "Arizona", "Arkansas", "California", "Colorado", "Connecticut", "Delaware", "District of Columbia", "Florida", "Georgia", "Hawaii", "Idaho", "Illinois", "Indiana", "Iowa", "Kansas", "Kentucky", "Louisiana", "Maine", "Maryland", "Massachusetts", "Michigan", "Minnesota",
            "Mississippi", "Missouri", "Montana", "Nebraska", "Nevada", "New Hampshire", "New Jersey", "New Mexico", "New York", "North Carolina", "North Dakota", "Ohio", "Oklahoma", "Oregon", "Pennsylvania", "Rhode Island", "South Carolina", "South Dakota", "Tennessee", "Texas", "Utah", "Vermont", "Virginia", "Washington", "West Virginia", "Wisconsin", "Wyoming"};
    String text = "";
    public RelativeLayout relativeLayout;
    /*private Integer[] stateFlags = { R.drawable.ic_alabama, R.drawable.ic_alaska, R.drawable.ic_arizona,
            R.drawable.ic_arkansas, R.drawable.ic_ca, R.drawable.ic_colorado, R.drawable.ic_connecticut, R.drawable.ic_delaware, R.drawable.ic_district_of_columbia, R.drawable.ic_fl, R.drawable.ic_georgia,
            R.drawable.ic_hawaii, R.drawable.ic_idaho, R.drawable.ic_il, R.drawable.ic_indiana, R.drawable.ic_iowa, R.drawable.ic_kansas,
            R.drawable.ic_kentucky, R.drawable.ic_louisiana, R.drawable.ic_maine, R.drawable.ic_maryland, R.drawable.ic_massachusetts, R.drawable.ic_michigan,
            R.drawable.ic_mn, R.drawable.ic_mississippi, R.drawable.ic_missouri, R.drawable.ic_montana, R.drawable.ic_nebraska, R.drawable.ic_nv,
            R.drawable.ic_new_hampshire, R.drawable.ic_new_jersey, R.drawable.ic_new_mexico, R.drawable.ic_new_york, R.drawable.ic_nc, R.drawable.ic_north_dakota,
            R.drawable.ic_ohio, R.drawable.ic_oklahoma, R.drawable.ic_oregon, R.drawable.ic_pennsylvania, R.drawable.ic_ri, R.drawable.ic_south_carolina,
            R.drawable.ic_south_dakota, R.drawable.ic_tn, R.drawable.ic_texas, R.drawable.ic_utah, R.drawable.ic_vermont, R.drawable.ic_va, R.drawable.ic_washington,
            R.drawable.ic_west_virginia, R.drawable.ic_wisconsin, R.drawable.ic_wyoming
    };
*/


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

        getSupportActionBar().setTitle("");

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

                //changing background here now
                /*
                int hold = state_spinner.getSelectedItemPosition();
                Log.d("Item Position is", "" + hold);
                SharedPreferences sharedPref = LoginActivity.this.getSharedPreferences("my_prefs", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putInt("background_resource", stateFlags[hold]);
                editor.apply();
*/

                /*
                relativeLayout = findViewById(R.id.relativeLayout);
                relativeLayout.setBackgroundResource(stateFlags[hold]);
                relativeLayout.getBackground().setAlpha(120);
*/

                dialog.dismiss();
                signUp(phoneNumber, password, name);
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

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

                    //adding this right now!!!!!!! see FB workchat mssg
                    ParseUser currentUser = ParseUser.getCurrentUser();
                    Object locationId = currentUser.get("location");

                    /*
                    int index = -1;
                    for (int i=0;i<stateFlags.length;i++) {
                        if (stateFlags[i].equals(locationId.toString())) {
                            index = i;
                            relativeLayout = findViewById(R.id.relativeLayout);
                            relativeLayout.setBackgroundResource(stateFlags[index]);
                            relativeLayout.getBackground().setAlpha(120);
                            break;
                        }
                    }
*/

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
