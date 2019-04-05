package com.example.outla.myapplication;

import android.graphics.Bitmap;
import android.os.Environment;

import com.itextpdf.text.Document;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Locale;


public class CreateManualPdf {



    public String pdfSave(Bitmap bitmap){

        String mFolder = Environment.getExternalStorageDirectory()+"/"+"pdf_viewer"+"/";
        String mFileName;
        Rectangle pageSize = new Rectangle(PageSize.A4);
        Document mDoc =new Document(pageSize);
        mFileName = new SimpleDateFormat("ddMMyyyy_HHmmss",
                Locale.getDefault()).format(System.currentTimeMillis());
        mFileName = mFileName+".pdf";
        String mFilePath =mFolder+mFileName;
        File dir = new File(mFilePath);
        if(!dir.exists()){
            dir.getParentFile().mkdir();
        }
        try {
            PdfWriter.getInstance(mDoc, new FileOutputStream(mFolder+mFileName));
            mDoc.setMargins(10, 10, 10, 10);
            mDoc.open();
            PdfPTable Logo = new PdfPTable(1);
            Logo.setWidthPercentage(100);
            Logo.setHorizontalAlignment(Paragraph.ALIGN_CENTER);
            PdfPCell headCell;
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            Image logo = null;
            try {
                Image image = Image.getInstance(stream.toByteArray());
                image.scaleToFit(PageSize.A4);
                mDoc.add(image);
            }catch (Exception e ){

            }

            mDoc.close();
        } catch (Exception e){

        }
        return(mFileName);
    }
}
