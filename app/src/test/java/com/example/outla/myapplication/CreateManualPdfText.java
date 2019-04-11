package com.example.outla.myapplication;

import android.graphics.Bitmap;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class CreateManualPdfText {


    @Mock
    private Bitmap test;

    @Mock
    private String filename;

    @Mock
    CreateManualPdf createManualPdf;

    @Test
    public void createManualPdfTest() {

        assertEquals(filename,createManualPdf.pdfSave(test) );
    }
}
