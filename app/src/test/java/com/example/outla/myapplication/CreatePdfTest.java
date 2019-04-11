package com.example.outla.myapplication;

import android.graphics.Bitmap;
import android.os.Environment;

import com.itextpdf.text.pdf.PdfPTable;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class CreatePdfTest {


    @Mock
    private PdfPTable pdfPTable;
    
    @Mock
    private CreatePdf createPdf;


    @Test
    public void changeDueTest() {

        String subtotal = "10";
        String taxTotal = "10";
        String total = "10";
        String cash = "10";
        String changedue = "10";
        assertEquals(pdfPTable,createPdf.changeDue(subtotal,taxTotal,total,cash,changedue) );
    }

}
