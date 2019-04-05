package com.example.outla.myapplication;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class ReceiptAdder extends AppCompatActivity {


    CreateManualPdf createManualPdf = new CreateManualPdf();
    MainActivity mainActivity = new MainActivity();
    ImageView image;
    EditText editText1;
    EditText editText2;
    Button generate;
    DatabaseHelper myDB;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_imagepicker);

        Uri uri = getIntent().getParcelableExtra("imageUri");



        image = findViewById(R.id.image);
        editText1 = findViewById(R.id.button);
        editText2 = findViewById(R.id.button2);
        generate = findViewById(R.id.generate);
        myDB = new DatabaseHelper(this);
        image.setImageURI(uri);

        generate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String timeDate =  new SimpleDateFormat("EEE, d MMM yyyy HH:mm",
                        Locale.getDefault()).format(System.currentTimeMillis());
                image.buildDrawingCache();
                Bitmap bitmap =image.getDrawingCache();
                String logo = "custom_receipt.png";
                String fileName = createManualPdf.pdfSave(bitmap);
                String shopName = editText1.getText().toString();
                String shopTotal = editText2.getText().toString();

                boolean insertData = myDB.addData(shopName,shopTotal+" ",fileName, logo, timeDate);

                if(insertData)
                    Toast.makeText(ReceiptAdder.this, "Successfully Entered Data!", Toast.LENGTH_LONG).show();
                else
                    Toast.makeText(ReceiptAdder.this, "Something went wrong :(.", Toast.LENGTH_LONG).show();



                Intent intent = new Intent(ReceiptAdder.this, MainActivity.class);
                startActivity(intent);
            }
        });











    }


}
