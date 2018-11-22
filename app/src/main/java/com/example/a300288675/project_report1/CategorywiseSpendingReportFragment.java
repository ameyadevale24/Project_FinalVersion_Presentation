package com.example.a300288675.project_report1;

import android.database.Cursor;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;


public class CategorywiseSpendingReportFragment extends Fragment {

    Button btnGenerateReport;
    PieChart pieChart;
    DatabaseHelper db;
    Spinner spnMonth, spnYear;
    int reportMonth;
    int reportYear;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.fragment_categorywise_spending, container, false);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //spinner for month
        spnMonth = (Spinner)getView().findViewById(R.id.r2spnMonth);
        try {
            Field popup = Spinner.class.getDeclaredField("mPopup");
            popup.setAccessible(true);
            android.widget.ListPopupWindow popupWindow = (android.widget.ListPopupWindow) popup.get(spnMonth);
            popupWindow.setHeight(500);
        }
        catch (NoClassDefFoundError | ClassCastException | NoSuchFieldException | IllegalAccessException e) {}

        //spinner for year
        spnYear = (Spinner)getView().findViewById(R.id.r2spnYear);
        try {
            Field popup = Spinner.class.getDeclaredField("mPopup");
            popup.setAccessible(true);
            android.widget.ListPopupWindow popupWindow = (android.widget.ListPopupWindow) popup.get(spnYear);
            popupWindow.setHeight(500);
        }
        catch (NoClassDefFoundError | ClassCastException | NoSuchFieldException | IllegalAccessException e) {}

        //set current month and year by default for the spinners
        Date date = new Date();
        LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        final int currentMonth = localDate.getMonthValue();
        final int currentYear = localDate.getYear();
        spnMonth.setSelection(currentMonth-1);      //subtract 1 from month because index starts from 0
        spnYear.setSelection(currentYear-2000);     //subtract 2000 from month because index starts from 0 (index 0 -> value 2000)

        //get button
        btnGenerateReport = (Button)getView().findViewById(R.id.r2btnGenerateReport);

        //get chart
        pieChart = (PieChart)getView().findViewById(R.id.report2CategoryPie);

        db = new DatabaseHelper(this.getContext());

        //generate report for the month and year selected
        btnGenerateReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //clear chart for new use
                pieChart.clear();

                double[] expenses;
                String[] days;

                //get values of spinners
                reportMonth = getMonthNumber(spnMonth.getSelectedItem().toString());
                reportYear = Integer.parseInt(spnYear.getSelectedItem().toString());

                if (reportMonth<=currentMonth && reportYear<=currentYear) {
                    pieChart.setHoleRadius(25f);
                    pieChart.setCenterText("Expenses");
                    pieChart.setTransparentCircleAlpha(0);

                    addDataInPie(pieChart);

                } else {
                    Toast.makeText(getContext(),"Cannot generate report for the future months", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void addDataInPie(PieChart chart) {

        String loginId = "";
        float[] yData;
        String[] xData;

        ArrayList<PieEntry> yEntry = new ArrayList<>();
        ArrayList<String> xEntry = new ArrayList<>();

        loginId = getActivity().getIntent().getExtras().get("loginid").toString();
        CategorywiseSpendingReport categorywiseSpendingReport = db.getExpensesCategorywiseForMonth(loginId, reportMonth, reportYear);
        yData = categorywiseSpendingReport.getamount();
        xData = categorywiseSpendingReport.getCategories();

        if (yData!=null && xData!=null){
            for(int i=0; i<yData.length; i++){
                yEntry.add(new PieEntry(yData[i],i));
            }

            for(int i=0; i<xData.length; i++){
                xEntry.add(xData[i]);
            }

            // create dataset
            PieDataSet pieDataSet = new PieDataSet(yEntry,"Expenses");
            pieDataSet.setSliceSpace(2);
            pieDataSet.setValueTextSize(12);

            //set colors
            ArrayList<Integer> color = new ArrayList<>();
            color.add(Color.LTGRAY);
            color.add(Color.BLUE);
            color.add(Color.GREEN);
            color.add(Color.YELLOW);
            color.add(Color.RED);
            color.add(Color.CYAN);
            color.add(Color.DKGRAY);
            color.add(Color.MAGENTA);

            pieDataSet.setColors(color);

            //add legend
            Legend legend = pieChart.getLegend();
            legend.setForm(Legend.LegendForm.CIRCLE);

            //create pie data object
            PieData pieData = new PieData(pieDataSet);
            pieChart.setData(pieData);
            pieChart.invalidate();
        }
    }


    public int getMonthNumber(String monthName){
        //return month number from month name
        int monthNumber=0;
        switch (monthName){
            case "Jan":
                monthNumber = 1;
                break;
            case "Feb":
                monthNumber = 2;
                break;
            case "March":
                monthNumber = 3;
                break;
            case "April":
                monthNumber = 4;
                break;
            case "May":
                monthNumber = 5;
                break;
            case "June":
                monthNumber = 6;
                break;
            case "July":
                monthNumber = 7;
                break;
            case "Aug":
                monthNumber = 8;
                break;
            case "Sep":
                monthNumber = 9;
                break;
            case "Oct":
                monthNumber = 10;
                break;
            case "Nov":
                monthNumber = 11;
                break;
            case "Dec":
                monthNumber = 12;
                break;
        }
        return  monthNumber;
    }


    public String getMonthName(int monthNumber){
        //return month name from month number
        String monthName="";
        switch (monthNumber){
            case 1:
                monthName = "Jan";
                break;
            case 2:
                monthName = "Feb";
                break;
            case 3:
                monthName = "March";
                break;
            case 4:
                monthName = "April";
                break;
            case 5:
                monthName = "May";
                break;
            case 6:
                monthName = "June";
                break;
            case 7:
                monthName = "July";
                break;
            case 8:
                monthName = "Aug";
                break;
            case 9:
                monthName = "Sep";
                break;
            case 10:
                monthName = "Oct";
                break;
            case 11:
                monthName = "Nov";
                break;
            case 12:
                monthName = "Dec";
                break;
        }
        return  monthName;
    }


}
