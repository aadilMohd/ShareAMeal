package com.example.share_meal;

public class userdata {
    String fullname,phone,emailid;
    String lat,lon;

    public userdata() {
    }

    public userdata(String fullname, String phone, String emailid) {
        this.fullname = fullname;
        this.phone = phone;
        this.emailid = emailid;
    }

    public userdata(String lat, String lon) {
        this.lat = lat;
       this.lon=lon;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmailid() {
        return emailid;
    }

    public void setEmailid(String emailid) {
        this.emailid = emailid;
    }

    public String getLat() { return lat; }

    public void setLat(String lati) { this.lat = lati; }

    public String getLon() { return lon; }

    public void setLon(String lon) { this.lon = lon; }
}
