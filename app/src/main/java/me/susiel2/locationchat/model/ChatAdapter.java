package me.susiel2.locationchat.model;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
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
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseUser;

import java.io.ByteArrayOutputStream;
import java.lang.ref.WeakReference;
import java.util.List;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;
import me.susiel2.locationchat.MainActivity;
import me.susiel2.locationchat.R;
import me.susiel2.locationchat.SearchExistingActivity;
import me.susiel2.locationchat.database.ParseOperations;

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
        if(context instanceof MainActivity && !ParseOperations.isChatRead(chat, ParseUser.getCurrentUser()))
            viewHolder.tv_chat_name.setTypeface(null, Typeface.BOLD);
        else
            viewHolder.tv_chat_name.setTypeface(null, Typeface.NORMAL);
        viewHolder.tvNumberOfMembers.setText(String.valueOf(ParseOperations.getNumberOfMembersInGroup(chat)) + " members");

        if(context instanceof SearchExistingActivity){
            viewHolder.ivAddButton.setVisibility(View.VISIBLE);
        } else {
            viewHolder.ivAddButton.setVisibility(View.INVISIBLE);
        }

        Bitmap bm_resized = null;
        try {
            String filePath = chat.getImage().getFile().getAbsolutePath();
            bm_resized = BitmapScaler.scaleToFitWidth(BitmapFactory.decodeFile(filePath), 500);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bm_resized.compress(Bitmap.CompressFormat.JPEG, 40, bytes);
        Glide.with(context).load(bm_resized).apply(RequestOptions.bitmapTransform(new RoundedCornersTransformation(25, 0, RoundedCornersTransformation.CornerType.ALL)))
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
        public ImageView ivAddButton;
        private WeakReference<ClickListener> listenerRef;

        public ViewHolder(View itemView, ClickListener listener) {
            super(itemView);

            listenerRef = new WeakReference<>(listener);

            iv_chat_image = itemView.findViewById(R.id.iv_chat_image);
            tv_chat_name = itemView.findViewById(R.id.tv_chat_name);
            tvNumberOfMembers = itemView.findViewById(R.id.tvNumberOfMembers);
            ivAddButton = itemView.findViewById(R.id.ivAddButton);

            itemView.setOnClickListener(this);
            ivAddButton.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if(view.getId() == ivAddButton.getId())
                listenerRef.get().onAddClicked(getAdapterPosition());
            else
                listenerRef.get().onChatClicked(getAdapterPosition());
        }

    }

    public interface ClickListener {

        void onAddClicked(int position);
        void onChatClicked(int position);

    }

}
