package com.device.storeplus.storeplus.models;

/**
 * Created by Tomer on 26-Jun-15.
 */
public class User {
    public String id;
    public String name;
    public String imageUrl;

    public User() {
    }

    public User(String id, String name, String imageUrl) {
        this.id = id;
        this.name = name;
        this.imageUrl = imageUrl;
    }
}
