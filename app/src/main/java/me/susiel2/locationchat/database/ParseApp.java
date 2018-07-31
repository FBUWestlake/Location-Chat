package me.susiel2.locationchat.database;

import android.app.Application;


import com.parse.Parse;
import com.parse.ParseObject;

import me.susiel2.locationchat.model.Chat;
import me.susiel2.locationchat.model.Message;
import me.susiel2.locationchat.model.UsersGroups;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

public class ParseApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        ParseObject.registerSubclass(Chat.class);
        ParseObject.registerSubclass(Message.class);
        ParseObject.registerSubclass(UsersGroups.class);

        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        // Can be Level.BASIC, Level.HEADERS, or Level.BODY
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        builder.networkInterceptors().add(httpLoggingInterceptor);

        final Parse.Configuration configuration = new Parse.Configuration.Builder(this)
                .applicationId("bounceId")
                .clientKey("bounceKey")
                .server("http://bounce-fbu.herokuapp.com/parse")
                .build();
        Parse.enableLocalDatastore(this);
        Parse.initialize(configuration);
    }

}