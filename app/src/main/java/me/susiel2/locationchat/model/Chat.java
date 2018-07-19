package me.susiel2.locationchat.model;

import android.widget.ImageView;

import org.parceler.Parcel;

@Parcel
public class Chat {

    private String name;
    private String chatImage;
    private String description;
    private int numberOfMembers;
    private String imageUrl;
    private String category;

    public Chat(){
        name = "";
        numberOfMembers = 0;
        imageUrl = "";
        category = "";
    }

    public Chat(String name, int numberOfMembers, String imageUrl, String category){
    // TODO - add field for image address
    private String category;

    public Chat() {

    }

    public Chat(String name, int numberOfMembers) {
        this.name = name;
        this.numberOfMembers = numberOfMembers;
        this.imageUrl = imageUrl;
        this.category = category;
    }

    public Chat(String name, String chatImage, String description, String category, int numberOfMembers) {
        this.name = name;
        this.chatImage = chatImage;
        this.description = description;
        this.category = category;
        this.numberOfMembers = numberOfMembers;
    }


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

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    public String getChatImage() {
        return chatImage;
    }

    public void setChatImage(String chatImage) {
        this.chatImage = chatImage;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
