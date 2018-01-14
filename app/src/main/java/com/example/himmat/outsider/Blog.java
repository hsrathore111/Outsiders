package com.example.himmat.outsider;

/**
 * Created by Hi on 7/18/2017.
 */

public class Blog {


    private String available_bed;
    private String Image;
    private String for_gender;
    private String house_number;
    private String phone_number;

    public Blog() {


    }

    public Blog(String available_bed, String image, String for_gender, String house_number, String phone_number) {
        this.available_bed = available_bed;
        Image = image;
        this.for_gender = for_gender;
        this.house_number = house_number;
        this.phone_number = phone_number;
    }

    public String getAvailable_bed() {
        return available_bed;
    }

    public void setAvailable_bed(String available_bed) {
        this.available_bed = available_bed;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public String getFor_gender() {
        return for_gender;
    }

    public void setFor_gender(String for_gender) {
        this.for_gender = for_gender;
    }

    public String getHouse_number() {
        return house_number;
    }

    public void setHouse_number(String house_number) {
        this.house_number = house_number;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }


}