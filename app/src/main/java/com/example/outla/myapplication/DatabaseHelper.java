package com.example.outla.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import static android.content.ContentValues.TAG;


public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "receipts.db";
    private static final String TABLE_NAME = "receipts_data";
    private static final String COL1 = "ID";
    private static final String COL2 = "SHOPNAME";
    private static final String COL3 = "SHOPTOTAL";
    private static final String COL4 = "LOCATIONOFURL";



    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_NAME + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                " SHOPNAME TEXT, SHOPTOTAL TEXT, LOCATIONOFURL TEXT)";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }


    public void deleteFile(String nameOfFile)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM " + TABLE_NAME + " WHERE "
                + COL4 + " = '" + nameOfFile + "'";
        Log.d(TAG, "deleteName: query: " + query);
        Log.d(TAG, "deleteName: Deleting " + nameOfFile + " from database.");
        db.execSQL(query);
    }


    public Cursor getUniqueTotalContents( String shopName) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("SELECT SUM(" + COL3 + ") FROM " + TABLE_NAME + " WHERE " + COL2 + " = '"+ shopName+"';", null);
    }

    public Cursor getUniqueListContents() {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("SELECT DISTINCT "+ COL2 + " FROM " + TABLE_NAME + ";", null);
    }

    public Cursor getListContents(String filter,String request) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + request +" "+  filter, null);
    }

    public boolean addData(String sName, String sValue, String locationOfPdf) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL2, sName);
        contentValues.put(COL3, sValue);
        contentValues.put(COL4, locationOfPdf);


        long result = db.insert(TABLE_NAME, null, contentValues);
        //if date as inserted incorrectly it will return -1
        return result != -1;
    }
}