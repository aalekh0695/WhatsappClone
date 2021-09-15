package com.example.whatsappclone.models;

public class User {

    private String profilePic;
    private String lastMessage;
    private String username;
    private String email;
    private String password;
    private String userId;

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    private String about;

    public User() {
    }

    public User(String profilePic, String lastMessage, String username, String email, String password, String userId) {
        this.profilePic = profilePic;
        this.lastMessage = lastMessage;
        this.username = username;
        this.email = email;
        this.password = password;
        this.userId = userId;
    }

    //SignUp Constructor
    public User(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "User{" +
                "lastMessage='" + lastMessage + '\'' +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", userId='" + userId + '\'' +
                '}';
    }
}
