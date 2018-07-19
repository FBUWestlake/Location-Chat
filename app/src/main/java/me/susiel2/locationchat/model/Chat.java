package me.susiel2.locationchat.model;

import org.parceler.Parcel;

@Parcel
public class Chat {
    private String name;
    private String chatImage;
    private String description;
    private String category;
    private int numberOfMembers;

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

    public int getNumberOfMembers() {
        return numberOfMembers;
    }

    public void setNumberOfMembers(int numberOfMembers) {
        this.numberOfMembers = numberOfMembers;
    }
}
