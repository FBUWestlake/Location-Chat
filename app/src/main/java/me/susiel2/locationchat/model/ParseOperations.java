package me.susiel2.locationchat.model;

import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.SaveCallback;

import me.susiel2.locationchat.ChatActivity;

public class ParseOperations {

    // Enjoy. :^)

    public void createMessage(EditText messageContent) {
        String content = messageContent.getText().toString();
        ParseObject message = ParseObject.create("Message");
        //content.put("USER_ID", ParseUser.getCurrentUser().getObjectId());
        message.put("content", content);
        // TODO: message.put("groupId", groupId);
        message.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e==null) {
                    //Toast.makeText(ChatActivity.class, "Message sent", Toast.LENGTH_SHORT).show();
                    //refreshMessages();
                } else {
                    Log.e("message", "failed to send");
                }
            }
        });
        messageContent.setText(null);
    }
}
