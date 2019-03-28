package com.example.outla.myapplication;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.WindowManager;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

public class ViewPieChart extends AppCompatActivity implements OnChartValueSelectedListener{

    DatabaseHelper myDB;
    PieChart pieChart;
    private Typeface tf;
    ArrayList<String> shopNames = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.pie_chart);
        setTitle("Breakdown of Spending");

        myDB = new DatabaseHelper(this);

        pieChart = findViewById(R.id.idPieChart);
        pieChart.setUsePercentValues(true);
        pieChart.getDescription().setEnabled(false);
        pieChart.setExtraOffsets(2, 10, 2, 5);
        pieChart.setDragDecelerationFrictionCoef(0.95f);

        tf = Typeface.createFromAsset(getAssets(), "OpenSans-Regular.ttf");
        pieChart.setCenterTextTypeface(Typeface.createFromAsset(getAssets(), "OpenSans-Light.ttf"));


        pieChart.setDrawHoleEnabled(true);
        pieChart.setHoleColor(Color.WHITE);

        pieChart.setTransparentCircleColor(Color.WHITE);
        pieChart.setTransparentCircleAlpha(110);

        pieChart.setHoleRadius(58f);
        pieChart.setTransparentCircleRadius(61f);

        pieChart.setDrawCenterText(true);

        pieChart.setRotationAngle(0);
        // enable rotation of the chart by touch
        pieChart.setRotationEnabled(true);
        pieChart.setHighlightPerTapEnabled(true);

        pieChart.setOnChartValueSelectedListener(this);


        pieChart.animateY(1400, Easing.EaseInOutQuad);



        pieChart.setEntryLabelColor(Color.WHITE);
        pieChart.setEntryLabelTextSize(12f);

        Legend l = pieChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setDrawInside(false);
        l.setXEntrySpace(7f);
        l.setYEntrySpace(0f);
        l.setYOffset(0f);
        setData();




    }

    private void setData() {

        ArrayList<PieEntry> yEntrys = new ArrayList<>();
        ArrayList<Float> shopTotals = new ArrayList<>();
        Cursor data = myDB.getUniqueListContents();

        while (data.moveToNext()) {
            String shopName = data.getString(0);
            shopNames.add(shopName);
        }
        for(int i = 0;i<shopNames.size();i++) {

            data = myDB.getUniqueTotalContents(shopNames.get(i));
            while (data.moveToNext()) {
                String shopTotal = data.getString(0);
                shopTotals.add(Float.valueOf(shopTotal));
            }

        }


        for(int i = 0; i< shopTotals.size();i++){
            float total = shopTotals.get(i);
            yEntrys.add(new PieEntry(total , shopNames.get(i)));
        }

        PieDataSet pieDataSet = new PieDataSet(yEntrys, "Shops");
        pieDataSet.setSliceSpace(3f);
        pieDataSet.setSelectionShift(5f);
        pieChart.setEntryLabelTextSize(20f);
        int blackColorValue = Color.BLACK;
        pieChart.setEntryLabelColor(blackColorValue);
        ArrayList<Integer> colors = new ArrayList<>();


        for (int c : ColorTemplate.MATERIAL_COLORS)
            colors.add(c);

        colors.add(ColorTemplate.getHoloBlue());

        pieDataSet.setColors(colors);


        PieData pieData = new PieData(pieDataSet);
        pieData.setValueFormatter(new PercentFormatter(pieChart));
        pieData.setValueTextSize(13f);
        pieData.setValueTextColor(Color.BLACK);
        pieData.setValueTypeface(tf);
        pieChart.setData(pieData);
        pieChart.highlightValues(null);
        pieChart.invalidate();
    }



    @Override
    public void onValueSelected(Entry e, Highlight h) {

        if (e == null)
            return;
        Log.i("VAL SELECTED",
                "Value: " + e.getY() + ", xIndex: "
                        + ", DataSet index: " + h.getX());

        String clickedShop = shopNames.get((int) h.getX());

        Intent intent = new Intent(ViewPieChart.this,ViewListContents.class);
        String filter = "SHOPNAME = \"" + clickedShop +"\"";
        intent.putExtra("SHOPS_TO_GET", filter);
        startActivity(intent);
    }

    @Override
    public void onNothingSelected() {
        Log.i("PieChart", "nothing selected");
    }

}
