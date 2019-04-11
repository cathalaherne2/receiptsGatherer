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
            String timeDate = "null";
            String uuid = "null";
            String subtotal = "null";
            String taxTotal = "null";
            String total = "null";
            String cash = "null";
            String changedue = "null";

            JSONObject location =  new JSONObject();
            JSONArray items = new JSONArray();

            try {
                subtotal = Json.getString("SubTotal");
                taxTotal = Json.getString("TaxTotal");
                total = Json.getString("Total");
                cash = Json.getString("Cash");
                changedue = Json.getString("ChangeDue");
                JSONObject object = Json.getJSONObject("shopInfo");
                location = Json.getJSONObject("location");
                items = Json.getJSONArray("items");
                shopId = object.getString("shopId");
                vatNum = object.getString("vatNum");
                message1 = object.getString("message1");
                message2 = object.getString("message2");
                message3 = object.getString("message3");
                barcode = object.getString("barcode");
                uuid = object.getString("uuid");
                shopLogo = object.getString("shopLogo");

                timeDate = object.getString("timeDate");
            } catch (Exception e) {
                Log.e("my app", "the json cannot be converted");
            }
            PdfPTable Logo = imageAdder(shopLogo);
            mDoc.add(Logo);
            mDoc.add( new Paragraph("               ") );
            mDoc.add( new Paragraph("               ") );
            String[] itemNames = itemsNamesInReceipt(items);
            float[] itemPrices = itemsPricesInReceipt(items);

            PdfPTable contents = itemsPurchased(itemNames,itemPrices);
            mDoc.add(contents);

            mDoc.add( new Paragraph("               ") );
            mDoc.add( new Paragraph("               ") );
            PdfPTable ChangeDue = changeDue(subtotal,taxTotal,total,cash,changedue);
            mDoc.add(ChangeDue);
            mDoc.add( new Paragraph(divider) );
            PdfPTable Message1 = messageStyle1(message1);
            mDoc.add(Message1);
            PdfPTable Message2 = messageStyle2(message2);
            mDoc.add(Message2);
            mDoc.add( new Paragraph(divider) );
            PdfPTable Message3 = messageStyle2(message3);
            mDoc.add(Message3);
            mDoc.add( new Paragraph("               ") );
            PdfPTable Location = locationStyle(location);
            mDoc.add(Location);

            mDoc.add( new Paragraph("               ") );
            mDoc.add( new Paragraph("               ") );
            PdfPTable Uuid = messageStyle3(uuid);
            mDoc.add(Uuid);
            mDoc.close();
        } catch (Exception e){

        }

        pdfEditor();

        return(mFileName);
    }

    private PdfPTable messageStyle1(String message){

        PdfPCell line;

        PdfPTable contents = new PdfPTable(1);
        contents.setWidthPercentage(100);
        line = new PdfPCell(new Paragraph(message, FontFactory.getFont(FontFactory.HELVETICA, 12, Font.BOLD)));
        line.setHorizontalAlignment(Paragraph.ALIGN_CENTER);
        line.setBorder(0);
        contents.addCell(line);
        return contents;

    }


    private PdfPTable messageStyle2(String message){

        PdfPCell line;

        PdfPTable contents = new PdfPTable(1);
        contents.setWidthPercentage(100);
        line = new PdfPCell(new Paragraph(message, FontFactory.getFont(FontFactory.HELVETICA, 8, Font.NORMAL)));
        line.setHorizontalAlignment(Paragraph.ALIGN_CENTER);
        line.setBorder(0);
        contents.addCell(line);
        return contents;

    }



    private PdfPTable messageStyle3(String message){

        PdfPCell line;

        PdfPTable contents = new PdfPTable(1);
        contents.setWidthPercentage(100);
        line = new PdfPCell(new Paragraph(message, FontFactory.getFont(FontFactory.HELVETICA, 6, Font.NORMAL)));
        line.setHorizontalAlignment(Paragraph.ALIGN_RIGHT);
        line.setBorder(0);
        contents.addCell(line);
        return contents;

    }

    private PdfPTable locationStyle(JSONObject Location){

        PdfPCell line;
        String location = "null";
        String locationAddress = "null";
        String phoneNumber = "null";
        try {
            location = Location.getString("Location");
            locationAddress = Location.getString("Address");
            phoneNumber = Location.getString("PhoneNumber");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        PdfPTable contents = new PdfPTable(1);
        contents.setWidthPercentage(100);
        line = new PdfPCell(new Paragraph(location, FontFactory.getFont(FontFactory.HELVETICA, 6, Font.NORMAL)));
        line.setHorizontalAlignment(Paragraph.ALIGN_CENTER);
        line.setBorder(0);
        contents.addCell(line);
        line = new PdfPCell(new Paragraph(locationAddress, FontFactory.getFont(FontFactory.HELVETICA, 6, Font.NORMAL)));
        line.setHorizontalAlignment(Paragraph.ALIGN_CENTER);
        line.setBorder(0);
        contents.addCell(line);
        line = new PdfPCell(new Paragraph(phoneNumber, FontFactory.getFont(FontFactory.HELVETICA, 6, Font.NORMAL)));
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

            price = new PdfPCell(new Paragraph("EUR" + String.format("%.02f", prices[aw]), FontFactory.getFont(FontFactory.HELVETICA, 6, Font.NORMAL)));
            price.setHorizontalAlignment(Paragraph.ALIGN_RIGHT);
            price.setBorder(0);
            contents.addCell(price);
        }
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

    public PdfPTable changeDue(String subtotal, String taxTotal,String total, String cash, String changedue){
        PdfPCell price;
        PdfPCell name;

        String names[] = {"SUBTOTAL","TAX APPLICABLE","TOTAL","CASH","CHANGE"};


        PdfPTable contents = new PdfPTable(2);
        contents.setWidthPercentage(100);



        name = new PdfPCell(new Paragraph(names[0], FontFactory.getFont(FontFactory.HELVETICA, 6, Font.NORMAL)));
        name.setHorizontalAlignment(Paragraph.ALIGN_LEFT);
        name.setBorder(0);
        contents.addCell(name);


        price = new PdfPCell(new Paragraph("EUR" + subtotal, FontFactory.getFont(FontFactory.HELVETICA, 6, Font.NORMAL)));
        price.setHorizontalAlignment(Paragraph.ALIGN_RIGHT);
        price.setBorder(0);
        contents.addCell(price);


        name = new PdfPCell(new Paragraph(names[1], FontFactory.getFont(FontFactory.HELVETICA, 6, Font.NORMAL)));
        name.setHorizontalAlignment(Paragraph.ALIGN_LEFT);
        name.setBorder(0);
        contents.addCell(name);


        price = new PdfPCell(new Paragraph("EUR" + taxTotal, FontFactory.getFont(FontFactory.HELVETICA, 6, Font.NORMAL)));
        price.setHorizontalAlignment(Paragraph.ALIGN_RIGHT);
        price.setBorder(0);
        contents.addCell(price);


        name = new PdfPCell(new Paragraph(names[2], FontFactory.getFont(FontFactory.HELVETICA, 6, Font.NORMAL)));
        name.setHorizontalAlignment(Paragraph.ALIGN_LEFT);
        name.setBorder(0);
        contents.addCell(name);

        price = new PdfPCell(new Paragraph("EUR" + total, FontFactory.getFont(FontFactory.HELVETICA, 6, Font.NORMAL)));
        price.setHorizontalAlignment(Paragraph.ALIGN_RIGHT);
        price.setBorder(0);
        contents.addCell(price);



        name = new PdfPCell(new Paragraph(names[3], FontFactory.getFont(FontFactory.HELVETICA, 6, Font.NORMAL)));
        name.setHorizontalAlignment(Paragraph.ALIGN_LEFT);
        name.setBorder(0);
        contents.addCell(name);


        price = new PdfPCell(new Paragraph("EUR" + cash, FontFactory.getFont(FontFactory.HELVETICA, 6, Font.NORMAL)));
        price.setHorizontalAlignment(Paragraph.ALIGN_RIGHT);
        price.setBorder(0);
        contents.addCell(price);

        name = new PdfPCell(new Paragraph(names[4], FontFactory.getFont(FontFactory.HELVETICA, 6, Font.NORMAL)));
        name.setHorizontalAlignment(Paragraph.ALIGN_LEFT);
        name.setBorder(0);
        contents.addCell(name);


        price = new PdfPCell(new Paragraph("EUR" + changedue, FontFactory.getFont(FontFactory.HELVETICA, 6, Font.NORMAL)));
        price.setHorizontalAlignment(Paragraph.ALIGN_RIGHT);
        price.setBorder(0);
        contents.addCell(price);



        return contents;
    }

    public float[] itemsPricesInReceipt(JSONArray items){

        float[] itemPrices = new float[items.length()];
        for(int i = 0; i<items.length();i++) {
            float itemPrice = 0.00f;
            try {
                JSONObject objectInstance = items.getJSONObject(i);
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

    public String[] itemsNamesInReceipt(JSONArray items){

        String[] itemNames = new String[items.length()];
        for(int i = 0; i<items.length();i++) {
            String itemName;
            try {
                JSONObject objectInstance = items.getJSONObject(i);
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
    public class EnvironmentHelper {
        public String getStorageState() {
            return Environment.getExternalStorageState();
        }
    }


}
