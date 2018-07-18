package me.susiel2.locationchat.model;

import android.widget.ImageView;

public class Chat {

    private String name;
    private int numberOfMembers;

    public Chat(String name, int numberOfMembers){
        this.name = name;
        this.numberOfMembers = numberOfMembers;
    }

    // TODO - add field for image address
    private ImageView chatImage;
    private String category;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNumberOfMembers() {
        return numberOfMembers;
    }

    public void setNumberOfMembers(int numberOfMembers) {
        this.numberOfMembers = numberOfMembers;
    }

    public void incrementNumberOfMembers(){
        numberOfMembers++;
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
