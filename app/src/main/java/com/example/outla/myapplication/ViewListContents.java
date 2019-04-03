package com.example.outla.myapplication;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class ViewListContents extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private static String mFolder = Environment.getExternalStorageDirectory() + "/" + "pdf_viewer" + "/";
    DatabaseHelper myDB;
    ArrayList<User> userList;
    ListView listView;
    User user;
    public String filter;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.viewcontents_layout);
        listView = findViewById(R.id.listView);
        final Spinner spinner = findViewById(R.id.filterSpinner);

        filter = getIntent().getStringExtra("SHOPS_TO_GET");

        myDB = new DatabaseHelper(this);


        List<String> categories = new ArrayList<>();
        categories.add("Filter newest first");
        categories.add("Filter by oldest first");
        categories.add("Filter by Shop Total");
        categories.add("Filter by Shop Name");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categories);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);
        spinner.setOnItemSelectedListener(this);

        userList = new ArrayList<>();
        Cursor data = myDB.getListContents(";", filter);
        int numRows = data.getCount();
        if (numRows == 0) {
            Toast.makeText(ViewListContents.this, "The Database is empty  :(.", Toast.LENGTH_LONG).show();
        }else{
            orderList(data);
        }
        final ThreeColumn_ListAdapter adapter = new ThreeColumn_ListAdapter(this, R.layout.list_adapter_view, userList);
        listView = findViewById(R.id.listView);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long arg) {
                User url = (User) listView.getItemAtPosition(position);
                String URL = url.getURL();
                Intent intent = new Intent(ViewListContents.this, ViewPdf.class);
                intent.putExtra("URL_TO_VISIT", URL);

                startActivity(intent);
            }
        });


        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() { //list is my listView
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long arg) {


                final User instanceOfUrl = (User) listView.getItemAtPosition(position);
                AlertDialog.Builder adb = new AlertDialog.Builder(ViewListContents.this);
                adb.setTitle("Delete?");
                String name = instanceOfUrl.getShopName();
                adb.setMessage("Are you sure you want to delete " + name);
                adb.setNegativeButton("Cancel", null);
                adb.setPositiveButton("Ok", new AlertDialog.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        adapter.remove(instanceOfUrl);
                        String URL = instanceOfUrl.getURL();
                        File file = new File(mFolder + URL);

                        Boolean deleted = file.delete();
                        if (deleted) {
                            Toast.makeText(ViewListContents.this, "item was deleted", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(ViewListContents.this, "item was not deleted", Toast.LENGTH_LONG).show();
                        }
                        myDB.deleteFile(URL);
                        adapter.remove(instanceOfUrl);
                        String query = " ORDER BY LOCATIONOFURL DESC;";
                        Cursor data = myDB.getListContents(query, filter);
                        orderList(data);
                    }
                });
                adb.show();
                return true;
            }
        });
    }

    private void orderList(Cursor data) {
        userList.clear();
        final ThreeColumn_ListAdapter adapter = new ThreeColumn_ListAdapter(this, R.layout.list_adapter_view, userList);
        listView = findViewById(R.id.listView);
        listView.setAdapter(adapter);
        int i = 0;
        while (data.moveToNext()) {
            user = new User(data.getString(1), data.getString(2), data.getString(3),data.getString(4),data.getString(5));
            userList.add(i, user);
            System.out.println(data.getString(1) + " " + data.getString(2));
            System.out.println(userList.get(i).getShopName());
            i++;

        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onItemSelected (AdapterView < ? > parent, View view,int position, long id){
        // On selecting a spinner item
        String item = parent.getItemAtPosition(position).toString();
        switch (item) {
            case "Filter by oldest first": {

                String query = " ORDER BY LOCATIONOFURL ASC;";
                Cursor data = myDB.getListContents(query, filter);
                orderList(data);

                break;
            }
            case "Filter by Shop Total": {

                String query = " ORDER BY SHOPTOTAL DESC;";
                Cursor data = myDB.getListContents(query, filter);
                orderList(data);
                listView = findViewById(R.id.listView);
                break;
            }
            case "Filter by Shop Name": {

                String query = " ORDER BY SHOPNAME ASC;";
                Cursor data = myDB.getListContents(query, filter);
                orderList(data);
                listView = findViewById(R.id.listView);
                break;
            }
            default: {
                String query = " ORDER BY LOCATIONOFURL DESC;";
                Cursor data = myDB.getListContents(query, filter);
                orderList(data);
                listView = findViewById(R.id.listView);
                break;
            }
        }
        Toast.makeText(ViewListContents.this, "Selected" + item, Toast.LENGTH_LONG).show();

    }

    public void onNothingSelected (AdapterView < ? > arg0){
    }
}
