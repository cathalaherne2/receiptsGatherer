package com.example.outla.myapplication;

import android.util.Log;

import org.json.JSONObject;


public class JsonConverter {

    public static JSONObject convertJson (String unFormatedJson) {

        try {
            JSONObject niceFormattedString = new JSONObject(unFormatedJson);

            return niceFormattedString;
        }catch (Exception e){
            Log.e("my app","the json cannot be converted");
            return null;
        }
    }

    public String[] getShopNameShopTotal(JSONObject Json) {

        String shopName = "null";
        String shopTotal = "null";
        String date = "null";
        String logo = "null.jpg";
        try {
            JSONObject object = Json.getJSONObject("shopInfo");
            shopName = object.getString("shopName");
            logo = object.getString("shopLogo");
            date = object.getString("timeDate");
            System.out.println(shopName);
            shopTotal = Json.getString("Total");
            System.out.println(shopTotal);
        } catch (Exception e) {
            Log.e("my app", "the json cannot be converted");
        }
        String[] info = new String[4];
        info[0] = shopName;
        info[1] = shopTotal;
        info[2] = date;
        info[3] = logo;

        return info;
    }
}