package com.device.storeplus.storeplus.models;

import android.content.ClipData;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Tomer on 25-Jun-15.
 */
public class SingleItem {
    public String id;
    public String title;
    public double price;
    public String currency;
    public String imageUrl;
    public ArrayList<ItemSize> itemSizes;
    public Date   date;
    public String location;

    public SingleItem() {
        this("1", "Great shirt", 25.99, "", "$");
    }

    public SingleItem(String id, String title, Double price, String imageUrl) {
        this(id, title, price, imageUrl, "$");
    }

    public SingleItem(String id, String title, Double price, String imageUrl, String currency) {
        this(id, title, price, imageUrl, currency, new ArrayList<ItemSize>());
    }

    public SingleItem(String id, String title, Double price, String imageUrl, String currency, ArrayList<ItemSize> itemSizes) {
        this(id, title, price, imageUrl, currency, itemSizes, null, "");
    }

    public SingleItem(String id, String title, Double price, String imageUrl, String currency, Date date, String location) {
        this(id, title, price, imageUrl, currency, new ArrayList<ItemSize>(), null, "");
    }

    public SingleItem(String id, String title, Double price, String imageUrl, String currency, ArrayList<ItemSize> itemSizes, Date date, String location) {
        this.id = id;
        this.currency = currency;
        this.title = title;
        this.price = price;
        this.imageUrl = imageUrl;
        this.itemSizes = itemSizes;
        this.date = date;
        this.location = location;
    }

    public static class ItemSize {
        public String name;
        public String title;
        public String status;
        public String inbagsStock;
        public String availbleStock;

        public ItemSize(String name, String title, String inbagsStock, String availbleStock) {
            this(name, title, "", inbagsStock, availbleStock);
        }

        public ItemSize(String name, String title, String status) {
            this(name, title, status, "", "");
        }

        public ItemSize(String name, String title, String status, String inbagsStock, String availbleStock) {
            this.name = name;
            this.title = title;
            this.status = status;
            this.inbagsStock = inbagsStock;
            this.availbleStock = availbleStock;
        }
    }
}
