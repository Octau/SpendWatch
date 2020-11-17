package com.octavius.spendwatch.balanceflow;

import android.content.Context;
import android.os.Build;
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
    private File file;
    private List<BalanceFlow> listBalance = new ArrayList<BalanceFlow>();
    private ArrayList<BalanceFlow> arrayListBalance = new ArrayList<BalanceFlow>();

    public ReadBalance(Context context) throws FileNotFoundException, IOException{
        file = new File(context.getFilesDir().getPath().toString() + "/BalanceFlow.txt");
        if(!file.exists()){
            file.createNewFile();
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
            try{
                //id, desc, balance ,date
                listBalance.add(new BalanceFlow(Integer.parseInt(parsed[0]), parsed[1], Integer.parseInt(parsed[2]), sdf.parse(parsed[3])));
                arrayListBalance.add(new BalanceFlow(Integer.parseInt(parsed[0]), parsed[1], Integer.parseInt(parsed[2]), sdf.parse(parsed[3])));
            }
            catch(Exception e){
                System.out.println(e);
            }
            n++;
        }
        Log.i("FETCH", "fetched " + n + " line");
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
}


