package me.susiel2.locationchat.model;

public class Chat {

    private String name;
    private int numberOfMembers;

    public Chat(String name, int numberOfMembers){
        this.name = name;
        this.numberOfMembers = numberOfMembers;
    }

    // TODO - add field for image address

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

}
