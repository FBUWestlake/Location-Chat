package me.susiel2.locationchat.model;

import android.widget.ImageView;

public class Chat {
    private String name;
    private ImageView chatImage;
    private String category;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ImageView getChatImage() {
        return chatImage;
    }

    public void setChatImage(ImageView chatImage) {
        this.chatImage = chatImage;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
