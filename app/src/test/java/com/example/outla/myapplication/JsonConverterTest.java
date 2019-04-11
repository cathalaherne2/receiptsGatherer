package com.example.outla.myapplication;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import static org.junit.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class JsonConverterTest {


    @Mock
    private JSONObject test;


    @Mock
    JsonConverter jsonConverter;

    @Test
    public void createManualPdfTest() {

        String filename = "{\"test\":\"test\"}";
        try {
            test = new JSONObject(filename);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        assertEquals(test,jsonConverter.convertJson(filename) );
    }
}

