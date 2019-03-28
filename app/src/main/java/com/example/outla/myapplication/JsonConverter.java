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
        try {
            JSONObject object = Json.getJSONObject("shopInfo");
            shopName = object.getString("shopName");
            System.out.println(shopName);
            shopTotal = Json.getString("Total");
            System.out.println(shopTotal);
        } catch (Exception e) {
            Log.e("my app", "the json cannot be converted");
        }
        String[] info = new String[2];
        info[0] = shopName;
        info[1] = shopTotal;
        return info;
    }
}