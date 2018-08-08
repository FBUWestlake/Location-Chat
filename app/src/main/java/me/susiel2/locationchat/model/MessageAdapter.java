package me.susiel2.locationchat.model;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import android.widget.Button;


import java.util.List;

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
        ImageView ivHeartSent;

        SentMessageHolder(View itemView) {
            super(itemView);

            messageText = (TextView) itemView.findViewById(R.id.text_message_body);
            timeText = (TextView) itemView.findViewById(R.id.text_message_time);
            tvNumberSent = (TextView) itemView.findViewById(R.id.tvNumberSent);
            ivHeartSent = (ImageView) itemView.findViewById(R.id.ivHeartSent);

        }

        void bind(Message message) {
            messageText.setText(message.getContent());
//            timeText.setText(message.getCreatedAtString());

            final int numberOfLikes = message.getLikes();
            tvNumberSent.setText(numberOfLikes + " ");

        }
    }

    // Messages sent by others display a profile image and nickname.
    private class ReceivedMessageHolder extends RecyclerView.ViewHolder {
        TextView messageText, timeText, nameText, tvNumberRec;
        Button likeButton;
        ImageView ivHeart;
        Button dislikeButton;
        ImageView ivThumbsDown;

        ReceivedMessageHolder(View itemView) {
            super(itemView);

            messageText = (TextView) itemView.findViewById(R.id.text_message_body);
            timeText = (TextView) itemView.findViewById(R.id.text_message_time);
            nameText = (TextView) itemView.findViewById(R.id.text_message_name);
            tvNumberRec = (TextView) itemView.findViewById(R.id.tvNumberRec);
            ivHeart = (ImageView) itemView.findViewById(R.id.ivHeart);
            likeButton = itemView.findViewById(R.id.likeButton);

            ivThumbsDown = (ImageView) itemView.findViewById(R.id.ivThumbsDown);
            dislikeButton = itemView.findViewById(R.id.dislikeButton);

        }

        void bind(Message message) {
            final Message message1 = message;
            messageText.setText(message.getContent());

            ParseQuery<ParseUser> query = ParseQuery.getQuery(ParseUser.class);
            query.whereEqualTo("objectId", message.getCreatedBy().getObjectId());

            query.findInBackground(new FindCallback<ParseUser>() {
                public void done(List<ParseUser> objects, ParseException e) {
                    if (objects != null) {
                        nameText.setText(objects.get(0).getString("name"));
                    }
                    // query SQL for name. have else for if this is deleted
//                    else if (){
//                        // Something went wrong.
//                    }
                }
            });

//            timeText.setText(message.getCreatedAtString());

//            Glide.with(context).load(message.getProfileImage())
//                    .apply(RequestOptions.placeholderOf(R.mipmap.blank_profile).error(R.mipmap.blank_profile).fitCenter())
//                    .apply(RequestOptions.bitmapTransform(new RoundedCornersTransformation(25,0, RoundedCornersTransformation.CornerType.ALL)))
//                    .into(profileImage);
            final int numberOfLikes = message.getLikes();
            tvNumberRec.setText(numberOfLikes + " ");

            likeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if ((ivHeart.getDrawable().getConstantState().equals(context.getResources().getDrawable(R.drawable.ufi_heart).getConstantState()))) {
                        ivHeart.setImageResource(R.drawable.ufi_heart_active);
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
                        tvNumberRec.setText(Integer.toString(moreLikes) + " ");
                    } else {
                        ivHeart.setImageResource(R.drawable.ufi_heart);
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
                        tvNumberRec.setText(Integer.toString(lessLikes) + " ");
                    }
                }
            });


            dislikeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
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
                        tvNumberRec.setText(Integer.toString(moreLikes) + " ");
                    }
                }
            });
            dislikeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
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
                        tvNumberRec.setText(Integer.toString(moreLikes) + " ");
                    }
                }
            });
        }
    }




}
