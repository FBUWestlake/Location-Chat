package me.susiel2.locationchat.model;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;
import me.susiel2.locationchat.R;
import me.susiel2.locationchat.model.Message;

public class MessageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int VIEW_TYPE_MESSAGE_SENT = 1;
    private static final int VIEW_TYPE_MESSAGE_RECEIVED = 2;

    private ArrayList<Message> mMessageList;
    private Context context;

    public MessageAdapter(ArrayList<Message> messages) {
        mMessageList = messages;
    }

    void appendMessage(Message message) {
        mMessageList.add(0, message);
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        Message message = (Message) mMessageList.get(position);

        if (message.getUser().equals(null /* TODO - INSERT getCurrentUser() HERE */)) {
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
        TextView messageText, timeText;

        SentMessageHolder(View itemView) {
            super(itemView);

            messageText = (TextView) itemView.findViewById(R.id.text_message_body);
            timeText = (TextView) itemView.findViewById(R.id.text_message_time);
        }

        void bind(Message message) {
            messageText.setText(message.getMessage());
            timeText.setText(message.getTime());
        }
    }

    // Messages sent by others display a profile image and nickname.
    private class ReceivedMessageHolder extends RecyclerView.ViewHolder {
        TextView messageText, timeText, nameText;
        ImageView profileImage;

        ReceivedMessageHolder(View itemView) {
            super(itemView);

            messageText = (TextView) itemView.findViewById(R.id.text_message_body);
            timeText = (TextView) itemView.findViewById(R.id.text_message_time);
            nameText = (TextView) itemView.findViewById(R.id.text_message_name);
            profileImage = (ImageView) itemView.findViewById(R.id.image_message_profile);
        }

        void bind(Message message) {
            messageText.setText(message.getMessage());
            nameText.setText(message.getUser());
            timeText.setText(message.getTime());

            Glide.with(context).load(message.getUserImage())
                    .apply(RequestOptions.placeholderOf(R.mipmap.blank_profile).error(R.mipmap.blank_profile).fitCenter())
                    .apply(RequestOptions.bitmapTransform(new RoundedCornersTransformation(25,0, RoundedCornersTransformation.CornerType.ALL)))
                    .into(profileImage);

        }
    }


}