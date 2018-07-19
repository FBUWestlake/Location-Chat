package me.susiel2.locationchat.model;

public class Chat {

    private String name;
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
        this.name = name;
        this.numberOfMembers = numberOfMembers;
        this.imageUrl = imageUrl;
        this.category = category;
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
}
