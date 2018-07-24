package me.susiel2.locationchat.model;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseObject;

public class ParseApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        ParseObject.registerSubclass(Chat.class);

        final Parse.Configuration configuration = new Parse.Configuration.Builder(this)
                .applicationId("bounceId")
                .clientKey("bounceKey")
                .server("http://bounce-fbu.herokuapp.com/parse")
                .build();
        Parse.initialize(configuration);
    }

}