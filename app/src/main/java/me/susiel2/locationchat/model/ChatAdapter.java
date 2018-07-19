package me.susiel2.locationchat.model;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.view.menu.MenuView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.lang.ref.WeakReference;
import java.util.List;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;
import me.susiel2.locationchat.R;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder>{

    private List<Chat> chats;
    private Context context;
    private ClickListener listen;

    public ChatAdapter(List<Chat> chatGroups, ClickListener listen) {
        chats = chatGroups;
        this.listen = listen;
    }

    @NonNull
    @Override
    public ChatAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        context = viewGroup.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View chatView = inflater.inflate(R.layout.item_chat, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(chatView, listen);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ChatAdapter.ViewHolder viewHolder, int i) {
        Log.d("chat adapter", String.valueOf(chats.size()));
        Chat chat = chats.get(i);

        viewHolder.tv_chat_name.setText(chat.getName());
        viewHolder.tvNumberOfMembers.setText(String.valueOf(chat.getNumberOfMembers()));
        // TODO - Glide for image.
        Glide.with(context).load(chat.getImageUrl()).apply(RequestOptions.bitmapTransform(new RoundedCornersTransformation(25,0, RoundedCornersTransformation.CornerType.ALL)))
                .into(viewHolder.iv_chat_image);
    }

    @Override
    public int getItemCount() {
        return chats.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public ImageView iv_chat_image;
        public TextView tv_chat_name;
        public TextView tvNumberOfMembers;
        private WeakReference<ClickListener> listenerRef;

        public ViewHolder(View itemView, ClickListener listener) {
            super(itemView);

            listenerRef = new WeakReference<>(listener);

            iv_chat_image = itemView.findViewById(R.id.iv_chat_image);
            tv_chat_name = itemView.findViewById(R.id.tv_chat_name);
            tvNumberOfMembers = itemView.findViewById(R.id.tvNumberOfMembers);

            tv_chat_name.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            listenerRef.get().onChatClicked(getAdapterPosition());
        }

    }

    public interface ClickListener {

        void onChatClicked(int position);

    }

}
