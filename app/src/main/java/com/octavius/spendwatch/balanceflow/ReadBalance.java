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
import java.util.List;
import java.util.Scanner;
import java.util.Date;

public class ReadBalance{
    private Scanner sc;
    private String[] parsed;
    private File file, path;
    private Integer tmp=0, lastId=0;
    private List<BalanceFlow> listBalance = new ArrayList<BalanceFlow>();
    private ArrayList<BalanceFlow> arrayListBalance = new ArrayList<BalanceFlow>();

    public ReadBalance(Context context) throws IOException{
        path = new File(context.getFilesDir().getPath()+"/balance");
        file = new File(context.getFilesDir().getPath()+ "/balance/BalanceFlow.txt");
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
        sc = new Scanner(file);
        this.addToListBalance();
    }

    public List<BalanceFlow> getListBalance(){
        return listBalance;
    }
    public ArrayList<BalanceFlow> getArrayListBalance(){return arrayListBalance;}

    private void addToListBalance(){
        Integer n=0;
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
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
        for (BalanceFlow balanceFlow : listBalance) {
            if(balanceFlow.getDate().before(current_date)|| balanceFlow.getDate() == current_date) totalBalance+= balanceFlow.getBalance();
            if(balanceFlow.getDate().getMonth() == current_date.getMonth()){
                 totalFlow+=balanceFlow.getBalance();
                 if(balanceFlow.getBalance() < 0){
                     totalSpending+=balanceFlow.getBalance();
                 }
            }
        }
        Integer[] stats = {totalFlow, totalSpending, totalBalance};
        return stats;
    }

    public Integer getLastId() {
        return lastId;
    }
}


