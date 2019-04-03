package com.example.outla.myapplication;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;
import android.widget.Toast;

import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.util.FitPolicy;

import java.io.File;

public class ViewPdf extends AppCompatActivity {


    public String URL;


    private static final int STORAGE_CODE = 1000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.pdf_view);
        String test = getIntent().getStringExtra("URL_TO_VISIT");
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) ==
                    PackageManager.PERMISSION_DENIED) {

                String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE};
                requestPermissions(permissions, STORAGE_CODE);
            } else {
                pdfView(test);
            }
        } else {
            pdfView(test);
        }
    }


    private void pdfView(String test) {
        //PDF View
        PDFView pdfView = findViewById(R.id.pdfView);
        String mFilePath = Environment.getExternalStorageDirectory() + "/" + "pdf_viewer" + "/" + test;
        File dir = new File(mFilePath);
        pdfView.fromFile(dir)
                .enableSwipe(true) // allows to block changing pages using swipe
                .swipeHorizontal(false)
                .enableDoubletap(false)
                .defaultPage(0)
                .password(null)
                .scrollHandle(null)
                .spacing(0)
                .pageFitPolicy(FitPolicy.WIDTH)
                .load();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case STORAGE_CODE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    pdfView("ERROR.pdf");
                } else {
                    Toast.makeText(this, "permission denied", Toast.LENGTH_SHORT).show();

                }
            }
        }
    }
}