package me.susiel2.locationchat.model;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;
import java.util.List;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;
import me.susiel2.locationchat.R;
import me.susiel2.locationchat.database.ParseOperations;

public class MessageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int VIEW_TYPE_MESSAGE_SENT = 1;
    private static final int VIEW_TYPE_MESSAGE_RECEIVED = 2;

    private List<Message> mMessageList;
    private Context context;
    ParseOperations parseOperations;

    public MessageAdapter(List<Message> messages) {
        mMessageList = messages;
    }

    void appendMessage(Message message) {
        mMessageList.add(0, message);
        notifyDataSetChanged();
    }


    @Override
    public int getItemViewType(int position) {
        Message message = (Message) mMessageList.get(position);

        if (message.getCreatedBy().getObjectId().equals(ParseUser.getCurrentUser().getObjectId())) {
            return VIEW_TYPE_MESSAGE_SENT;
        } else {
            return VIEW_TYPE_MESSAGE_RECEIVED;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        context = parent.getContext();

        View view;

        if (viewType == VIEW_TYPE_MESSAGE_SENT) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_sent_message, parent, false);
            return new SentMessageHolder(view);
        } else if (viewType == VIEW_TYPE_MESSAGE_RECEIVED) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_received_message, parent, false);
            return new ReceivedMessageHolder(view);
        }

        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Message message = (Message) mMessageList.get(position);
        switch (holder.getItemViewType()) {
            case VIEW_TYPE_MESSAGE_SENT:
                ((SentMessageHolder) holder).bind(message);
                break;
            case VIEW_TYPE_MESSAGE_RECEIVED:
                ((ReceivedMessageHolder) holder).bind(message);
        }
    }

    @Override
    public int getItemCount() {
        return mMessageList.size();
    }

    private class SentMessageHolder extends RecyclerView.ViewHolder {
        TextView messageText, timeText, tvNumberSent;
        ImageView ivThumbsUpSent;
        ImageView attachedImage;

        SentMessageHolder(View itemView) {
            super(itemView);

            messageText = (TextView) itemView.findViewById(R.id.text_message_body);
            timeText = (TextView) itemView.findViewById(R.id.text_message_time);
            tvNumberSent = (TextView) itemView.findViewById(R.id.tvNumberSent);
            ivThumbsUpSent = (ImageView) itemView.findViewById(R.id.ivThumbsUpSent);
            attachedImage = (ImageView) itemView.findViewById(R.id.attachedPicture);

        }

        void bind(Message message) {
            messageText.setText(message.getContent());
            timeText.setText(message.getCreatedAtString());

            if (message.getFile() != null) {
                Log.e("MessageAdapter", "binding image to message " + message.getContent() + " and file " + message.getFile());
                Bitmap bm_resized = null;
                try {
                    String filePath = message.getFile().getFile().getAbsolutePath();
                    bm_resized = BitmapScaler.scaleToFitWidth(BitmapFactory.decodeFile(filePath), 500);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                bm_resized.compress(Bitmap.CompressFormat.JPEG, 40, bytes);
                Glide.with(context).load(bm_resized).apply(RequestOptions.bitmapTransform(new RoundedCornersTransformation(25, 0, RoundedCornersTransformation.CornerType.ALL)))
                        .into(attachedImage);
            } else {
                Drawable myDrawable = context.getResources().getDrawable(R.drawable.asfalt_light);
                attachedImage.setImageDrawable(myDrawable);
            }

            final int numberOfLikes = message.getLikes();
            tvNumberSent.setText(numberOfLikes + " ");

        }
    }

    // Messages sent by others display a profile image and nickname.
    private class ReceivedMessageHolder extends RecyclerView.ViewHolder {
        TextView messageText, timeText, nameText, tvNumberRec;
        Button likeButton;
        ImageView ivThumbsUp;
        Button dislikeButton;
        ImageView ivThumbsDown;
        ImageView attachedImage;
        Button viewHiddenMessageButton;

        ReceivedMessageHolder(View itemView) {
            super(itemView);

            messageText = (TextView) itemView.findViewById(R.id.text_message_body);
            timeText = (TextView) itemView.findViewById(R.id.text_message_time);
            nameText = (TextView) itemView.findViewById(R.id.text_message_name);
            tvNumberRec = (TextView) itemView.findViewById(R.id.tvNumberRec);
            ivThumbsUp = (ImageView) itemView.findViewById(R.id.ivThumbsUp);
            likeButton = itemView.findViewById(R.id.likeButton);

            ivThumbsDown = (ImageView) itemView.findViewById(R.id.ivThumbsDown);
            dislikeButton = itemView.findViewById(R.id.dislikeButton);

            attachedImage = (ImageView) itemView.findViewById(R.id.attachedPicture);
            viewHiddenMessageButton = itemView.findViewById(R.id.viewHiddenMessageButton);

        }

        void bind(Message message) {
            final Message message1 = message;
            ivThumbsUp.setImageResource(R.drawable.outline_thumb_up);
            ivThumbsDown.setImageResource(R.drawable.outline_thumb_down);

            //query to see if the message ID and user Id are together in the MessageUserLikes table.
            //If they are, then color in whichever value is true.
            ParseQuery<MessageUserLikes> query1 = ParseQuery.getQuery(MessageUserLikes.class);
            query1.whereEqualTo("userId", ParseUser.getCurrentUser().getObjectId());
            query1.whereEqualTo("messageId", message1.getObjectId());
            query1.findInBackground(new FindCallback<MessageUserLikes>() {
                public void done(List<MessageUserLikes> objects, ParseException e) {
                    if (e == null) {
                        if (objects.size() != 0) {
                            Log.d("This is first object", "" + objects.get(0));
                            Log.d("Is it liked?", "" + objects.get(0).getLiked());
                            Log.d("Is it disliked?", "" + objects.get(0).getDisliked());
                            if (objects.get(0).getLiked()) {
                                ivThumbsUp.setImageResource(R.drawable.filled_thumb_up);
                            }
                            if (objects.get(0).getDisliked()) {
                                ivThumbsDown.setImageResource(R.drawable.filled_thumb_down);
                            }
                        }
                    } else {
                        // Something went wrong.
                    }
                }
            });

            if (message.getLikes() < -2) {
                messageText.setText("Message hidden due to low score. Click to view.");
                timeText.setText(message1.getCreatedAtString());

                //just added these lines
                ParseQuery<ParseUser> query = ParseQuery.getQuery(ParseUser.class);
                query.whereEqualTo("objectId", message.getCreatedBy().getObjectId());
                query.findInBackground(new FindCallback<ParseUser>() {
                    public void done(List<ParseUser> objects, ParseException e) {
                        if (e == null) {
                            if (objects.size() != 0)
                                nameText.setText(objects.get(0).getString("name"));
                            else
                                nameText.setText("[deleted]");
                        } else {
                            // Something went wrong.
                        }
                    }
                });

                if (message.getFile() != null) {
                    Log.e("MessageAdapter", "binding image to message " + message.getContent() + " and file " + message.getFile());
                    Bitmap bm_resized = null;
                    try {
                        String filePath = message.getFile().getFile().getAbsolutePath();
                        bm_resized = BitmapScaler.scaleToFitWidth(BitmapFactory.decodeFile(filePath), 500);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                    bm_resized.compress(Bitmap.CompressFormat.JPEG, 40, bytes);
                    Glide.with(context).load(bm_resized).apply(RequestOptions.bitmapTransform(new RoundedCornersTransformation(25, 0, RoundedCornersTransformation.CornerType.ALL)))
                            .into(attachedImage);
                } else {
                    Drawable myDrawable = context.getResources().getDrawable(R.drawable.asfalt_light);
                    attachedImage.setImageDrawable(myDrawable);
                }

                viewHiddenMessageButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        messageText.setText(message1.getContent());
                    }
                });

            } else {
                messageText.setText(message.getContent());

                ParseQuery<ParseUser> query = ParseQuery.getQuery(ParseUser.class);
                query.whereEqualTo("objectId", message.getCreatedBy().getObjectId());

                query.findInBackground(new FindCallback<ParseUser>() {
                    public void done(List<ParseUser> objects, ParseException e) {
                        if (e == null) {
                            if (objects.size() != 0)
                                nameText.setText(objects.get(0).getString("name"));
                            else
                                nameText.setText("[deleted]");
                        } else {
                            // Something went wrong.
                        }
                    }
                });


                if (message.getFile() != null) {
                    Log.e("MessageAdapter", "binding image to message " + message.getContent() + " and file " + message.getFile());
                    Bitmap bm_resized = null;
                    try {
                        String filePath = message.getFile().getFile().getAbsolutePath();
                        bm_resized = BitmapScaler.scaleToFitWidth(BitmapFactory.decodeFile(filePath), 500);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                    bm_resized.compress(Bitmap.CompressFormat.JPEG, 40, bytes);
                    Glide.with(context).load(bm_resized).apply(RequestOptions.bitmapTransform(new RoundedCornersTransformation(25, 0, RoundedCornersTransformation.CornerType.ALL)))
                            .into(attachedImage);
                } else {
                    Drawable myDrawable = context.getResources().getDrawable(R.drawable.asfalt_light);
                    attachedImage.setImageDrawable(myDrawable);
                }
            }

            timeText.setText(message.getCreatedAtString());

//            Glide.with(context).load(message.getProfileImage())
//                    .apply(RequestOptions.placeholderOf(R.mipmap.blank_profile).error(R.mipmap.blank_profile).fitCenter())
//                    .apply(RequestOptions.bitmapTransform(new RoundedCornersTransformation(25,0, RoundedCornersTransformation.CornerType.ALL)))
//                    .into(profileImage);
            final int numberOfLikes = message.getLikes();
            tvNumberRec.setText(numberOfLikes + " ");


            likeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if ((ivThumbsDown.getDrawable().getConstantState().equals(context.getResources().getDrawable(R.drawable.outline_thumb_down).getConstantState()))) {
                        if ((ivThumbsUp.getDrawable().getConstantState().equals(context.getResources().getDrawable(R.drawable.outline_thumb_up).getConstantState()))) {
                            ivThumbsUp.setImageResource(R.drawable.filled_thumb_up);
                            int moreLikes = message1.getLikes();
                            moreLikes = moreLikes + 1;
                            message1.setLikes(moreLikes);

                            message1.saveInBackground(new SaveCallback() {
                                @Override
                                public void done(ParseException e) {
                                    if (e == null) {
                                        Log.d("MessageAdapter", "Like post success");
                                    } else {
                                        e.printStackTrace();
                                    }
                                }
                            });

                            ParseUser user = ParseUser.getCurrentUser();
                            MessageUserLikes messageUserLikes = new MessageUserLikes();
                            messageUserLikes.setMessageId(message1.getObjectId());
                            messageUserLikes.setUser(user.getObjectId());
                            messageUserLikes.setLiked(true);
                            messageUserLikes.setDisliked(false);
                            messageUserLikes.saveInBackground();

                            //badging test starts here
                            ParseUser msgSender = message1.getCreatedBy();
                            String userId = msgSender.getObjectId();
                            Log.d("This is the user ID", userId);
//query for messages where createdAt is that userId.
                            //this has error of indexoutofbounds exception
                            ParseQuery<UsersPoints> query = ParseQuery.getQuery(UsersPoints.class);
                            query.whereEqualTo("userId", userId);
                            try {
                                List<UsersPoints> result = query.find();
                                Log.d("This is result", "" + result);
                                ParseObject obj = result.get(0);
                                int userMorePoints = obj.getInt("totalPoints");
                                Log.d("totalPoints count", "" + userMorePoints);
                                //int userMorePoints = result.get(0).getInt("totalPoints");
                                obj.put("totalPoints", userMorePoints + 1);
                                obj.saveInBackground();
                                Log.d("This is parse points", "" + obj.get("totalPoints"));
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }

                            tvNumberRec.setText(Integer.toString(moreLikes) + " ");
                        } else {
                            ivThumbsUp.setImageResource(R.drawable.outline_thumb_up);
                            int lessLikes = message1.getLikes();
                            lessLikes = lessLikes - 1;
                            message1.setLikes(lessLikes);
                            message1.saveInBackground(new SaveCallback() {
                                @Override
                                public void done(ParseException e) {
                                    if (e == null) {
                                        Log.d("MessageAdapter", "Like post success");
                                    } else {
                                        e.printStackTrace();
                                    }
                                }
                            });

                            //deleting the row from Parse if unliked
                            ParseQuery<MessageUserLikes> query1 = ParseQuery.getQuery(MessageUserLikes.class);
                            query1.whereEqualTo("userId", ParseUser.getCurrentUser().getObjectId());
                            query1.whereEqualTo("messageId", message1.getObjectId());
                            query1.findInBackground(new FindCallback<MessageUserLikes>() {
                                public void done(List<MessageUserLikes> objects, ParseException e) {
                                    if (e == null) {
                                        objects.get(0).deleteInBackground();
                                        objects.get(0).saveInBackground();
                                    }
                                }
                            });
                            tvNumberRec.setText(Integer.toString(lessLikes) + " ");
                        }
                    }
                }
            });


            dislikeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if ((ivThumbsUp.getDrawable().getConstantState().equals(context.getResources().getDrawable(R.drawable.outline_thumb_up).getConstantState()))) {
                        if ((ivThumbsDown.getDrawable().getConstantState().equals(context.getResources().getDrawable(R.drawable.outline_thumb_down).getConstantState()))) {
                            ivThumbsDown.setImageResource(R.drawable.filled_thumb_down);
                            int lessLikes = message1.getLikes();
                            lessLikes = lessLikes - 1;
                            message1.setLikes(lessLikes);
                            message1.saveInBackground(new SaveCallback() {
                                @Override
                                public void done(ParseException e) {
                                    if (e == null) {
                                        Log.d("MessageAdapter", "Dislike post success");
                                    } else {
                                        e.printStackTrace();
                                    }
                                }
                            });

                            ParseUser user = ParseUser.getCurrentUser();
                            MessageUserLikes messageUserLikes = new MessageUserLikes();
                            messageUserLikes.setMessageId(message1.getObjectId());
                            messageUserLikes.setUser(user.getObjectId());
                            messageUserLikes.setDisliked(true);
                            messageUserLikes.setLiked(false);
                            messageUserLikes.saveInBackground();

                            //badging test starts here
                            ParseUser msgSender = message1.getCreatedBy();
                            String userId = msgSender.getObjectId();
                            Log.d("This is the user ID", userId);
//query for messages where createdAt is that userId.
                            //this has error of indexoutofbounds exception
                            ParseQuery<UsersPoints> query = ParseQuery.getQuery(UsersPoints.class);
                            query.whereEqualTo("userId", userId);
                            try {
                                List<UsersPoints> result = query.find();
                                Log.d("This is result", "" + result);
                                ParseObject obj = result.get(0);
                                int userMorePoints = obj.getInt("totalPoints");
                                Log.d("totalPoints count", "" + userMorePoints);
                                //int userMorePoints = result.get(0).getInt("totalPoints");
                                obj.put("totalPoints", userMorePoints - 1);
                                obj.saveInBackground();
                                Log.d("This is parse points", "" + obj.get("totalPoints"));
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }


                            tvNumberRec.setText(Integer.toString(lessLikes) + " ");
                        } else {
                            ivThumbsDown.setImageResource(R.drawable.outline_thumb_down);
                            int moreLikes = message1.getLikes();
                            moreLikes = moreLikes + 1;
                            message1.setLikes(moreLikes);
                            message1.saveInBackground(new SaveCallback() {
                                @Override
                                public void done(ParseException e) {
                                    if (e == null) {
                                        Log.d("MessageAdapter", "Dislike post success");
                                    } else {
                                        e.printStackTrace();
                                    }
                                }
                            });

//                            ParseUser user = ParseUser.getCurrentUser();
//                            MessageUserLikes messageUserLikes = new MessageUserLikes();
//                            messageUserLikes.setMessageId(message1.getObjectId());
//                            messageUserLikes.setUser(user.getObjectId());
//                            messageUserLikes.setDisliked(false);
//                            messageUserLikes.setLiked(false);
//                            messageUserLikes.saveInBackground();

                            //deleting the row from Parse if un-disliked
                            ParseQuery<MessageUserLikes> query1 = ParseQuery.getQuery(MessageUserLikes.class);
                            query1.whereEqualTo("userId", ParseUser.getCurrentUser().getObjectId());
                            query1.whereEqualTo("messageId", message1.getObjectId());
                            query1.findInBackground(new FindCallback<MessageUserLikes>() {
                                public void done(List<MessageUserLikes> objects, ParseException e) {
                                    if (e == null) {
                                        objects.get(0).deleteInBackground();
                                        objects.get(0).saveInBackground();
                                    }
                                }
                            });

                            tvNumberRec.setText(Integer.toString(moreLikes) + " ");
                        }
                    }
                }
            });
        }
    }

}

