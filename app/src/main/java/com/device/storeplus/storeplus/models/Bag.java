package com.device.storeplus.storeplus.models;

import java.util.ArrayList;

/**
 * Created by Tomer on 26-Jun-15.
 */
public class Bag {
    public ArrayList<SingleItem> items;
    public User user;

    public Bag() {
        this(new User(), new ArrayList<SingleItem>());
    }

    public Bag(User user, ArrayList<SingleItem> items) {
        this.items = items;
        this.user = user;
    }

}
