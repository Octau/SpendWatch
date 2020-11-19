package com.octavius.spendwatch.balanceflow;

import android.content.Context;
import android.os.Build;
import android.os.Environment;
import android.util.Log;

import androidx.annotation.RequiresApi;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.Date;

public class ReadBalance{
    private Scanner sc;
    private String[] parsed;
    private File file, path;
    private Integer tmp=0, lastId=0;
    private List<BalanceFlow> listBalance = new ArrayList<BalanceFlow>(), current_month_list;
    private ArrayList<BalanceFlow> arrayListBalance = new ArrayList<BalanceFlow>();
    private SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");

    public ReadBalance(Context context, String file_name) throws IOException{
        path = new File(context.getFilesDir().getPath()+"/balance");
        file = new File(context.getFilesDir().getPath()+ "/balance/" + file_name + ".txt");
        Log.i("File", "go");
        if(!path.exists()){
            path.mkdirs();
            if(path.isDirectory()){
                Log.i("File", "created dir");
            }
        }
        if(!file.exists()){
            file.createNewFile();
            Log.i("File", "File created");
        }
        this.addToListBalance();
        this.fetchThisMonthBalance();
    }

    public List<BalanceFlow> getListBalance(String mode){
        if(mode=="desc") Collections.reverse(listBalance);
        return listBalance;
    }
    public ArrayList<BalanceFlow> getArrayListBalance(String mode){
        if(mode=="desc") Collections.reverse(arrayListBalance);
        return arrayListBalance;
    }

    private void addToListBalance() throws FileNotFoundException {
        sc = new Scanner(file);
        Integer n=0;
        while(sc.hasNextLine()){
            String line = sc.nextLine();
            parsed = line.trim().split(";");
            Integer curr_id = Integer.parseInt(parsed[0]);
            try{
                Date curr_date = sdf.parse(parsed[3]);
                //id, desc, balance ,date
                listBalance.add(new BalanceFlow(curr_id, parsed[1], Integer.parseInt(parsed[2]), curr_date));
                arrayListBalance.add(new BalanceFlow(curr_id, parsed[1], Integer.parseInt(parsed[2]), curr_date));
            }
            catch(Exception e){
                System.out.println(e);
            }

            tmp = curr_id;
            n++;
        }
        lastId = tmp;
        Log.i("FETCH", "fetched " + n + " line");

        Collections.sort(listBalance, new CustomComparator());
        Collections.sort(arrayListBalance, new CustomComparator());
    }

    public void refreshList() throws FileNotFoundException {
        listBalance = new ArrayList<BalanceFlow>();
        arrayListBalance = new ArrayList<BalanceFlow>();
        sc = new Scanner(file);
        addToListBalance();
    }
    public Integer[] getThisMonthTotal(){
        Date current_date = new Date();
        Integer totalFlow = 0;
        Integer totalBalance = 0;
        Integer totalSpending = 0;
        for (BalanceFlow balance_flow : listBalance) {
            if(balance_flow.getDate().before(current_date)|| balance_flow.getDate() == current_date) totalBalance+= balance_flow.getBalance();
            if(balance_flow.getDate().getMonth() == current_date.getMonth()){
                 totalFlow+=balance_flow.getBalance();
                 if(balance_flow.getBalance() < 0){
                     totalSpending+=balance_flow.getBalance();
                 }
            }
        }
        Integer[] stats = {totalFlow, totalSpending, totalBalance};
        return stats;
    }

    public void fetchThisMonthBalance() throws FileNotFoundException {
        current_month_list = new ArrayList<BalanceFlow>();
        Date current_date = new Date();
        for (BalanceFlow balance_flow: listBalance) {
            Date record_date = balance_flow.getDate();
            if(record_date.getMonth() == current_date.getMonth() && record_date.getYear() == current_date.getYear()){
                current_month_list.add(balance_flow);
            }
        }
        Collections.sort(current_month_list, new CustomComparator());
    }

    public List<BalanceFlow> getThisMonthBalance(String mode){
        if(mode=="desc") Collections.reverse(current_month_list);
        return current_month_list;
    }

    public Integer getLastId() {
        return lastId;
    }

    public class CustomComparator implements Comparator<BalanceFlow> {
        @Override
        public int compare(BalanceFlow o1, BalanceFlow o2) {
            return o1.getDate().compareTo(o2.getDate());
        }
    }
}


