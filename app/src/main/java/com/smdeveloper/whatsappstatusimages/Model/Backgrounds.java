package com.smdeveloper.whatsappstatusimages.Model;

public class Backgrounds {
    private String categoryid;
    private String imagelink;

    public Backgrounds() {
    }

    public Backgrounds(String categoryid, String imagelink) {
        this.categoryid = categoryid;
        this.imagelink = imagelink;
    }

    public String getCategoryid() {
        return categoryid;
    }

    public void setCategoryid(String categoryid) {
        this.categoryid = categoryid;
    }

    public String getImagelink() {
        return imagelink;
    }

    public void setImagelink(String imagelink) {
        this.imagelink = imagelink;
    }
}