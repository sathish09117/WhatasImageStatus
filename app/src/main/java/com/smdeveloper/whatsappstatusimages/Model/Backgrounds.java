package com.smdeveloper.whatsappstatusimages.Model;

public class Backgrounds {
    private String Categoryid;
    private String Imagelink;

    public Backgrounds() {
    }

    public Backgrounds(String categoryid, String imagelink) {
        Categoryid = categoryid;
        Imagelink = imagelink;
    }

    public String getCategoryid() {
        return Categoryid;
    }

    public void setCategoryid(String categoryid) {
        Categoryid = categoryid;
    }

    public String getImagelink() {
        return Imagelink;
    }

    public void setImagelink(String imagelink) {
        Imagelink = imagelink;
    }
}