package com.example.outla.myapplication;

import org.json.JSONException;
import org.json.JSONObject;

public class FilterJson {

    public JSONObject test;

    public void takeInRow(String row) {
        try {
            test = JsonConverter.convertJson(row);
        } catch (Exception e) {
        }
    }

    public String shopName(){
        try{
            return(test.getString("shopName"));
        }
        catch(JSONException e){
            return "Empty";
        }
    }

    public String message1(JSONObject receipt){
        try{
            return(receipt.getString("message1"));
        }
        catch(Exception e){
            return null;
        }
    }
}
