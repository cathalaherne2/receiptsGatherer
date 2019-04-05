package com.example.outla.myapplication;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;

public class ThreeColumn_ListAdapter extends ArrayAdapter<User> {

    private static String mFolder = Environment.getExternalStorageDirectory()+"/"+"pdf_viewer"+"/";
    private LayoutInflater mInflater;
    private ArrayList<User> users;
    private int mViewResourceId;

    public ThreeColumn_ListAdapter(Context context, int textViewResourceId, ArrayList<User> users) {
        super(context, textViewResourceId, users);
        this.users = users;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mViewResourceId = textViewResourceId;
    }


    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = mInflater.inflate(mViewResourceId, null);

        User user = users.get(position);

        if (user != null) {
            ImageView logo =  convertView.findViewById(R.id.logo);
            TextView total = (TextView) convertView.findViewById(R.id.total);
            TextView shopName = (TextView) convertView.findViewById(R.id.shopName);
            TextView timeDate = (TextView) convertView.findViewById(R.id.timeDate);
            if (logo != null) {

                String logoString = user.getLogo();
                File f= new File(mFolder, logoString);

                Bitmap bmp = null;
                try{
                    bmp = BitmapFactory.decodeStream(new FileInputStream(f));
                }catch (Exception e){

                }
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bmp = Bitmap.createScaledBitmap(bmp, 130, 34, false);
                bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
                logo.setImageBitmap(bmp);
            }
            if (total != null) {
                String euroTotal = "€"+user.getTotal();
                total.setText((euroTotal));
            }
            if (shopName != null) {
                shopName.setText((user.getShopName()));
            }
            if (timeDate != null) {
                timeDate.setText((user.getTimeDate()));
            }
        }

        return convertView;
    }
}