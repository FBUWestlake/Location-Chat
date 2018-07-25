package me.susiel2.locationchat.database;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseObject;

import me.susiel2.locationchat.model.Chat;
import me.susiel2.locationchat.model.Message;

public class ParseApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        ParseObject.registerSubclass(Chat.class);
        ParseObject.registerSubclass(Message.class);

        final Parse.Configuration configuration = new Parse.Configuration.Builder(this)
                .applicationId("bounceId")
                .clientKey("bounceKey")
                .server("http://bounce-fbu.herokuapp.com/parse")
                .build();
        Parse.initialize(configuration);
    }

}