package com.octavius.spendwatch;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.octavius.spendwatch.balanceflow.BalanceFlow;
import com.octavius.spendwatch.balanceflow.BalanceProfile;
import com.octavius.spendwatch.balanceflow.ReadBalance;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class StatisticFragment extends Fragment {
    private LineChart chart;
    private ReadBalance rb;
    private List<Entry> entriesTotalPerDay = new ArrayList<Entry>(), entriesSpendingPerDay = new ArrayList<Entry>();
    private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    private HashMap<Date, Integer> totalBalance = new HashMap<>(), spending = new HashMap<>();
    private String TAG = "StatisticFragment";
    private ArrayList<LineDataSet> lines = new ArrayList<LineDataSet> ();
    private Integer[] xAxis;
    private BalanceProfile bp;
    private String file_name;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_statistic, container, false);
        // in this example, a LineChart is initialized from xml
        chart = (LineChart) view.findViewById(R.id.chart);
        chart.getDescription().setText("This month balance");

        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1);

        YAxis rightAxis = chart.getAxisRight();
        rightAxis.setDrawLabels(false);
//        try {
//            bp = new BalanceProfile(getContext());
//            file_name = bp.getConfigFile();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        try {
//            rb = new ReadBalance(getContext(), file_name);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        for (BalanceFlow balance: rb.getThisMonthBalance("")) {
//            if(totalBalance.get(balance.getDate()) == null) totalBalance.put(balance.getDate(), 0);
//            totalBalance.put(balance.getDate(), (totalBalance.get(balance.getDate()) + balance.getBalance()));
//            if(balance.getBalance() > 0 || balance.getBalance() == 0){
//                spending.put(balance.getDate(),0);
//            }
//            else if(balance.getBalance() < 0) {
//                if(spending.get(balance.getDate()) == null) spending.put(balance.getDate(),0);
//                spending.put(balance.getDate(), (spending.get(balance.getDate()) + Math.abs(balance.getBalance())));
//            }
//            Log.i(TAG, sdf.format(balance.getDate()));
//        }
//
//        for (BalanceFlow balance : rb.getThisMonthBalance("")) {
//            // turn your data into Entry objects
//            entriesTotalPerDay.add(new Entry(balance.getDate().getDate(), totalBalance.get(balance.getDate())));
//            entriesSpendingPerDay.add(new Entry(balance.getDate().getDate(), spending.get(balance.getDate())));
//            //Log.i(TAG, "Total Balance for " + balance.getDate().getDate() + " is " + totalBalance.get(balance.getDate()));
//            Log.i(TAG, "Spending for " + balance.getDate().getDate() + " is " + spending.get(balance.getDate()));
//        }
//
//        LineDataSet dataSetTotal = new LineDataSet(entriesTotalPerDay, "Total Income + Spending");
//        dataSetTotal.setColor(getContext().getColor(R.color.colorBalanceGreen));
//
//        LineDataSet dataSetSpending = new LineDataSet(entriesSpendingPerDay, "Spending");
//        dataSetSpending.setColor(getContext().getColor(R.color.colorBalanceRed));
//
//        LineData lineData = new LineData();
//        lineData.addDataSet(dataSetTotal);
//        lineData.addDataSet(dataSetSpending);
//
//        chart.setData(lineData);
//        chart.invalidate(); // refresh
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        try {
            bp = new BalanceProfile(getContext());
            file_name = bp.getConfigFile();
            getActivity().setTitle("Profile " + file_name);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            rb = new ReadBalance(getContext(), file_name);
        } catch (IOException e) {
            e.printStackTrace();
        }

        List<BalanceFlow> this_month_balance = rb.getThisMonthBalance("");
        for (BalanceFlow balance: this_month_balance) {
            if(totalBalance.get(balance.getDate()) == null) totalBalance.put(balance.getDate(), 0);
            totalBalance.put(balance.getDate(), (totalBalance.get(balance.getDate()) + balance.getBalance()));
            if(balance.getBalance() > 0 || balance.getBalance() == 0){
                spending.put(balance.getDate(),0);
            }
            else if(balance.getBalance() < 0) {
                if(spending.get(balance.getDate()) == null) spending.put(balance.getDate(),0);
                spending.put(balance.getDate(), (spending.get(balance.getDate()) + Math.abs(balance.getBalance())));
            }
            Log.i(TAG, sdf.format(balance.getDate()));
        }

        for (BalanceFlow balance : this_month_balance) {
            // turn your data into Entry objects
            entriesTotalPerDay.add(new Entry(balance.getDate().getDate(), totalBalance.get(balance.getDate())));
            entriesSpendingPerDay.add(new Entry(balance.getDate().getDate(), spending.get(balance.getDate())));
            //Log.i(TAG, "Total Balance for " + balance.getDate().getDate() + " is " + totalBalance.get(balance.getDate()));
           // Log.i(TAG, "Spending for " + balance.getDate().getDate() + " is " + spending.get(balance.getDate()));
            Log.i(TAG, "balance for " + balance.getDate().getDate() + " is " + totalBalance.get(balance.getDate()));
        }

        LineDataSet dataSetTotal = new LineDataSet(entriesTotalPerDay, "Total Income + Spending");
        dataSetTotal.setColor(getContext().getColor(R.color.colorBalanceGreen));

        LineDataSet dataSetSpending = new LineDataSet(entriesSpendingPerDay, "Spending");
        dataSetSpending.setColor(getContext().getColor(R.color.colorBalanceRed));

        LineData lineData = new LineData();
        lineData.addDataSet(dataSetTotal);
        lineData.addDataSet(dataSetSpending);

        chart.setData(lineData);
        chart.invalidate(); // refresh
    }
}



