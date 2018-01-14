package com.example.himmat.outsider;

/**
 * Created by Hi on 8/14/2017.
 */

public class IslandModel {

    public String name;
    public int img;

    public IslandModel(String name, int img){
        this.name = name;
        this.img = img;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getImg() {
        return img;
    }

    public void setImg(int img) {
        this.img = img;
    }
}
