package com.kailawn.recipe.Model;

public class Post_item_model {
    String chawhmeh_hming;
    String id;
    String image;
    String desc;
    String siam_dan1;
    String siam_dan2;
    public Post_item_model(){
        // default constructor
    }

    public String getChawhmeh_hming() {
        return chawhmeh_hming;
    }

    public void setChawhmeh_hming(String chawhmeh_hming) {
        this.chawhmeh_hming = chawhmeh_hming;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getSiam_dan1() {
        return siam_dan1;
    }

    public void setSiam_dan1(String siam_dan1) {
        this.siam_dan1 = siam_dan1;
    }
    public String getSiam_dan2() {
        return siam_dan2;
    }

    public void setSiam_dan2(String siam_dan2) {
        this.siam_dan2 = siam_dan2;
    }

    public Post_item_model(String chawhmeh_hming, String id, String image, String desc, String siam_dan1, String siam_dan2) {
        this.chawhmeh_hming = chawhmeh_hming;
        this.id = id;
        this.image = image;
        this.desc = desc;
        this.siam_dan1 = siam_dan1;
        this.siam_dan2 = siam_dan2;
    }
}
