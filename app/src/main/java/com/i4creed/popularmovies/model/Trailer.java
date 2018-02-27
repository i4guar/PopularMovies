package com.i4creed.popularmovies.model;

/**
 * Created by i4gua on 24-Feb-18 at 09:46.
 * This class models a trailer
 */
public class Trailer {
    private String name;
    private String key;

    public Trailer(String name, String key) {
        this.name = name;
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
