package me.susiel2.locationchat.model;

import android.graphics.Bitmap;

import org.parceler.Parcel;

import java.io.File;

@Parcel
public class Chat {

    public String name;
    public String description;
    public int numberOfMembers;
    public String imageUrl;
    public Bitmap bitmapImage;
    public String category;
    public File imageFile;

    public Chat(){
        name = "";
        numberOfMembers = 0;
        imageUrl = "";
        category = "";
        description = "";
        bitmapImage = null;
    }

    public Chat(String name, String chatImage, String description, String category, int numberOfMembers) {
        this.name = name;
        this.imageUrl = chatImage;
        this.description = description;
        this.category = category;
        this.numberOfMembers = numberOfMembers;
    }

    public Chat(String name, Bitmap bitmapImage, String description, String category, int numberOfMembers) {
        this.name = name;
        this.bitmapImage = bitmapImage;
        this.description = description;
        this.category = category;
        this.numberOfMembers = numberOfMembers;
    }

    public Chat(String name, File imageFile, String description, String category, int numberOfMembers) {
        this.name = name;
        this.imageFile = imageFile;
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

    public Bitmap getBitmapImage() {
        return bitmapImage;
    }

    public void setBitmapImage(Bitmap bitmapImage) {
        this.bitmapImage = bitmapImage;
    }

    public File getImageFile() {
        return imageFile;
    }

    public void setImageFile(File image) {
        this.imageFile = imageFile;
    }
    
    /*
    private static final String KEY_DESCRIPTION = "description";
    private static final String KEY_IMAGE = "image";
    private static final String KEY_USER = "user";

    public String getDescription(){
        return getString(KEY_DESCRIPTION);
    }

    public void setDescription(String description){
        put(KEY_DESCRIPTION, description);
    }

    public ParseFile getImage(){
        return getParseFile(KEY_IMAGE);
    }

    public void setImage(ParseFile image){
        put(KEY_IMAGE, image);
    }

    public ParseUser getUser() {
        return getParseUser(KEY_USER);
    }

    public void setUser(ParseUser user) {
        put(KEY_USER, user);
    }
    
    public static class Query extends ParseQuery<Post>{
    public Query() {
        super(Chat.class);
    }

    public Query withGroup(){
        include(KEY_GROUP);
        return this;
    }

    public Query withUser(){
        include(KEY_USER);
        return this;
    }
}
    */
}


