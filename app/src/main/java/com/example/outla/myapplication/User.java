package com.example.outla.myapplication;



public class User {
    private String Total;
    private String ShopName;
    private String URL;
    private String TimeDate;
    private String Logo;

    public User(String shopName, String total, String url,String logo ,String timeDate ) {
        ShopName= shopName;
        Total = total;
        URL = url;
        TimeDate = timeDate;
        Logo = logo;
    }


    public String getTotal() {
        return Total;
    }

    public void setTotal(String total) {
        Total = total;
    }

    public String getShopName() {
        return ShopName;
    }

    public void setShopName(String shopName) {
        ShopName = shopName;
    }

    public void setURL(String URL) {
        this.URL = URL;
    }

    public String getTimeDate() {
        return TimeDate;
    }

    public void setTimeDate(String timeDate) {
        TimeDate = timeDate;
    }

    public String getDate() {
        return TimeDate;
    }

    public void setDate(String date) {
        TimeDate = date;
    }

    public String getLogo() {
        return Logo;
    }

    public void setLogo(String logo) {
        Logo = logo;
    }

    public String getURL() {
        return URL;
    }
}