package me.susiel2.locationchat.model;

import org.parceler.Parcel;

@Parcel
public class Chat {

    public String name;
    public String description;
    public int numberOfMembers;
    public String imageUrl;
    public String category;

    public Chat(){
        name = "";
        numberOfMembers = 0;
        imageUrl = "";
        category = "";
    }

    public Chat(String name, String chatImage, String description, String category, int numberOfMembers) {
        this.name = name;
        this.imageUrl = chatImage;
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
