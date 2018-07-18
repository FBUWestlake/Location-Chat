package me.susiel2.locationchat.model;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import me.susiel2.locationchat.R;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder>{

    private List<Chat> chats;
    private Context context;

    public ChatAdapter(List<Chat> chatGroups) {
        chats = chatGroups;
    }

    @NonNull
    @Override
    public ChatAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        context = viewGroup.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View chatView = inflater.inflate(R.layout.item_chat, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(chatView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ChatAdapter.ViewHolder viewHolder, int i) {
        Chat chat = chats.get(i);

        viewHolder.tv_chat_name.setText(chat.getName());
        viewHolder.tvNumberOfMembers.setText(String.valueOf(chat.getNumberOfMembers()));
        // TODO - Glide for image.
    }

    @Override
    public int getItemCount() {
        return chats.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView iv_chat_image;
        public TextView tv_chat_name;
        public TextView tvNumberOfMembers;

        public ViewHolder(View itemView) {
            super(itemView);

            iv_chat_image = itemView.findViewById(R.id.iv_chat_image);
            tv_chat_name = itemView.findViewById(R.id.tv_chat_name);
            tvNumberOfMembers = itemView.findViewById(R.id.tvNumberOfMembers);
        }
    }
}
