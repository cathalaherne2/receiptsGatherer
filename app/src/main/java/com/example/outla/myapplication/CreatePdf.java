package com.example.outla.myapplication;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;

import com.itextpdf.text.Document;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfArray;
import com.itextpdf.text.pdf.PdfDictionary;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.parser.PdfReaderContentParser;
import com.itextpdf.text.pdf.parser.TextMarginFinder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;


public class CreatePdf{

    private static String mFolder = Environment.getExternalStorageDirectory()+"/"+"pdf_viewer"+"/";
    private static final String divider = "**************************************";
    private static final int BORDER = 10;
    private static final int TOP = 64;
    private String mFileName;


    public String pdfSave(JSONObject Json){

        float pageWidth = 200f;
        float pageHeight = 14400f;
        Rectangle pageSize = new Rectangle(pageWidth, pageHeight);
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
            PdfWriter.getInstance(mDoc, new FileOutputStream(mFolder+"example.pdf"));
            mDoc.setMargins(10, 10, 130, 10);
            mDoc.open();

            String shopId = "null";
            String vatNum = "null";
            String message1 = "null";
            String message2 = "null";
            String message3 = "null";
            String barcode = "null";
            String shopLogo = "null";


            try {
                JSONObject object = Json.getJSONObject("shopInfo");
                shopId = object.getString("shopId");
                vatNum = object.getString("vatNum");
                message1 = object.getString("message1");
                message2 = object.getString("message2");
                message3 = object.getString("message3");
                barcode = object.getString("barcode");
                shopLogo = object.getString("shopLogo");
            } catch (Exception e) {
                Log.e("my app", "the json cannot be converted");
            }
            PdfPTable Logo = imageAdder(shopLogo);
            mDoc.add(Logo);
            mDoc.add( new Paragraph("               ") );
            mDoc.add( new Paragraph("               ") );
            String[] itemNames = itemsNamesInReceipt(Json);
            float[] itemPrices = itemsPricesInReceipt(Json);

            PdfPTable contents = itemsPurchased(itemNames,itemPrices);
            mDoc.add(contents);

            PdfPTable ChangeDue = changeDue();
            mDoc.add(ChangeDue);
            mDoc.add( new Paragraph(divider) );
            PdfPTable Message1 = message1(Json);
            mDoc.add(Message1);


            mDoc.close();
        } catch (Exception e){

        }

        pdfEditor();

        return(mFileName);
    }

    private PdfPTable message1(JSONObject json){
        String message = "blank";
        try{
            message = json.getString("shopName");
        }
        catch(JSONException e){

        }
        PdfPCell line;

        PdfPTable contents = new PdfPTable(1);
        contents.setWidthPercentage(100);
        line = new PdfPCell(new Paragraph(message, FontFactory.getFont(FontFactory.HELVETICA, 12, Font.BOLD)));
        line.setHorizontalAlignment(Paragraph.ALIGN_CENTER);
        line.setBorder(0);
        contents.addCell(line);
        return contents;

    }
    public PdfPTable  itemsPurchased(String names[],float prices[]){


        PdfPCell name;
        PdfPCell price;

//        String names[] = {"APPLE","MANGO","PEACH","BANANA","ORANGE","GRAPES","WATERMELON","TOMATO"};
//        Float prices[] = {1.25f, 1.25f,1.25f,1.25f, 1.25f,1.25f,1.25f, 1.25f};


        PdfPTable contents = new PdfPTable(2);
        contents.setWidthPercentage(100);

        for(int aw = 0; aw < names.length; aw++){
            name = new PdfPCell(new Paragraph(names[aw], FontFactory.getFont(FontFactory.HELVETICA, 6, Font.NORMAL)));
            name.setHorizontalAlignment(Paragraph.ALIGN_LEFT);
            name.setBorder(0);
            contents.addCell(name);

            price = new PdfPCell(new Paragraph("EUR"+Float.toString(prices[aw]), FontFactory.getFont(FontFactory.HELVETICA, 6, Font.NORMAL)));
            price.setHorizontalAlignment(Paragraph.ALIGN_RIGHT);
            price.setBorder(0);
            contents.addCell(price);
        }
        PdfPCell blank = new PdfPCell(new Paragraph("  "));
        blank.setBorder(0);
        contents.addCell(blank);
        contents.addCell(blank);
        name = new PdfPCell(new Paragraph("TOTAL", FontFactory.getFont(FontFactory.HELVETICA, 6, Font.NORMAL)));
        name.setHorizontalAlignment(Paragraph.ALIGN_LEFT);
        name.setBorder(0);
        contents.addCell(name);


        price = new PdfPCell(new Paragraph("EUR10.00", FontFactory.getFont(FontFactory.HELVETICA, 6, Font.NORMAL)));
        price.setHorizontalAlignment(Paragraph.ALIGN_RIGHT);
        price.setBorder(0);
        contents.addCell(price);



        return contents;
    }

    private PdfPTable imageAdder(String image){

        File f= new File(mFolder, image);
        Bitmap bmp = null;
        try{
            bmp = BitmapFactory.decodeStream(new FileInputStream(f));
        }catch (Exception e){

        }
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bmp = Bitmap.createScaledBitmap(bmp, 130, 34, false);
        bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
        Image logo = null;
        try {
            logo = com.itextpdf.text.Image.getInstance(stream.toByteArray());
            logo.scalePercent(100f);
        }catch (Exception e ){

        }

        PdfPTable Logo = new PdfPTable(1);
        Logo.setWidthPercentage(100);
        Logo.setHorizontalAlignment(Paragraph.ALIGN_CENTER);
        PdfPCell headCell;
        headCell = new PdfPCell(logo);
        headCell.setHorizontalAlignment(Paragraph.ALIGN_CENTER);
        headCell.setBorderWidthLeft(0);
        headCell.setBorderWidthRight(0);
        headCell.setBorderWidthTop(0);
        headCell.setBorderWidthBottom(0);
        headCell.setPadding(3);
        Logo.addCell(headCell);
        return Logo;
    }


    private void pdfEditor() {
        try {

            String mFilePath = Environment.getExternalStorageDirectory()+"/"+"pdf_viewer"+"/"+"example.pdf";
            String mFilePath2 = Environment.getExternalStorageDirectory()+"/"+"pdf_viewer"+"/"+mFileName;
            PdfReader reader = new PdfReader(mFilePath);
            PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(mFilePath2));

            // Go through all pages
            int n = reader.getNumberOfPages();
            for (int i = 1; i <= n; i++) {
                Rectangle pageSize = reader.getPageSize(i);
                Rectangle rect = getOutputPageSize(pageSize, reader, i);

                PdfDictionary page = reader.getPageN(i);
                page.put(PdfName.CROPBOX, new PdfArray(new float[]{rect.getLeft()-BORDER, rect.getBottom()-BORDER, rect.getRight()+BORDER, rect.getTop()+TOP}));
                stamper.markUsed(page);
            }
            stamper.close();
        }catch(Exception e){
        }
    }

    private Rectangle getOutputPageSize(Rectangle pageSize, PdfReader reader, int page) throws IOException
    {
        PdfReaderContentParser parser = new PdfReaderContentParser(reader);
        TextMarginFinder finder = parser.processContent(page, new TextMarginFinder());
        Rectangle result = new Rectangle(finder.getLlx(), finder.getLly(), finder.getUrx(), finder.getUry());
        System.out.printf("Text/bitmap boundary: %f,%f to %f, %f\n", finder.getLlx(), finder.getLly(), finder.getUrx(), finder.getUry());
        return result;
    }

    public PdfPTable changeDue(){
        PdfPCell name;
        PdfPCell price;

        String names[] = {"CASH","CHANGE ROUNDING","CASH DUE"};
        Float values[] = {10.00f, 0.00f,5.00f};
        DecimalFormat decimalFormat = new DecimalFormat("#.##");


        PdfPTable contents = new PdfPTable(2);
        contents.setWidthPercentage(100);
        for(int aw = 0; aw < names.length; aw++) {
            name = new PdfPCell(new Paragraph(names[aw], FontFactory.getFont(FontFactory.HELVETICA, 6, Font.NORMAL)));
            name.setHorizontalAlignment(Paragraph.ALIGN_LEFT);
            name.setBorder(0);
            contents.addCell(name);


            price = new PdfPCell(new Paragraph("EUR" + String.format("%.02f", values[aw]), FontFactory.getFont(FontFactory.HELVETICA, 6, Font.NORMAL)));
            price.setHorizontalAlignment(Paragraph.ALIGN_RIGHT);
            price.setBorder(0);
            contents.addCell(price);
        }

        return contents;
    }

    public float[] itemsPricesInReceipt(JSONObject items){

        float[] itemPrices = new float[items.length()];
        for(int i = 0; i<items.length();i++) {
            float itemPrice = 0.00f;
            try {
                JSONArray jsonArray = items.getJSONArray("items");
                JSONObject objectInstance = jsonArray.getJSONObject(i);
                itemPrice = Float.parseFloat(objectInstance.getString("totalPrice"));
//                itemCategory = objectInstance.getString("itemCategory");
//                itemPrice = objectInstance.getString("itemPrice");
//                quantity = objectInstance.getString("quantity");
//                totalPrice = objectInstance.getString("totalPrice");
//                addInfo = objectInstance.getString("addInfo");
                itemPrices[i] = itemPrice;
            } catch (Exception e) {
                Log.e("my app", "the json cannot be converted");
            }
        }

        return itemPrices;

    }
    public String[] itemsNamesInReceipt(JSONObject items){

        String[] itemNames = new String[items.length()];
        for(int i = 0; i<items.length();i++) {
            String itemName = "null";
            try {
                JSONArray jsonArray = items.getJSONArray("items");
                JSONObject objectInstance = jsonArray.getJSONObject(i);
                itemName = objectInstance.getString("itemName");
//                itemCategory = objectInstance.getString("itemCategory");
//                itemPrice = objectInstance.getString("itemPrice");
//                quantity = objectInstance.getString("quantity");
//                totalPrice = objectInstance.getString("totalPrice");
//                addInfo = objectInstance.getString("addInfo");
                itemNames[i] = itemName;
            } catch (Exception e) {
                Log.e("my app", "the json cannot be converted");
            }
        }

        return itemNames;

    }


}
