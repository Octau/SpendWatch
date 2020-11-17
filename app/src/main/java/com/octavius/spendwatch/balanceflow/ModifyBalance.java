package com.octavius.spendwatch.balanceflow;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class ModifyBalance {
    private FileWriter writer;
    private File file, path;
    private Scanner sc;
    private String[] parsed;
    private List<BalanceFlow> listBalance = new ArrayList<BalanceFlow>();
    private ArrayList<BalanceFlow> arrayListBalance = new ArrayList<BalanceFlow>();

    public ModifyBalance(Context context) throws FileNotFoundException, IOException {
        path = new File(Environment.getExternalStorageDirectory()+"/balance");
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

    private void writerNew() throws IOException {
        writer = new FileWriter(file, false);
    }

    private void writerClose() throws IOException {
        writer.close();
    }

    public void deleteLine(Integer id) throws IOException{
        writerNew();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        for (BalanceFlow bf : getListBalance()) {
            if(bf.id != id) writer.write(String.format("%d;%s;%d;%s\n", bf.id, bf.desc, bf.balance, sdf.format(bf.date)));
        }
        writerClose();
    }

    public void editLine(Integer id, String desc, Integer balance, Date date)  throws IOException{
        writerNew();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        for (BalanceFlow bf : getListBalance()) {
            if(bf.id != id) writer.write(String.format("%d;%s;%d;%s\n", bf.id, bf.desc, bf.balance, sdf.format(bf.date)));
            else writer.write(String.format("%d;%s;%d;%s\n", bf.id, desc, balance, sdf.format(date)));
        }
        writerClose();
    }

    public List<BalanceFlow> getListBalance(){
        return listBalance;
    }

    private void addToListBalance(){
        Integer n=0;
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        while(sc.hasNextLine()){
            String line = sc.nextLine();
            parsed = line.trim().split(";");
            try{
                //id, desc, balance ,date
                listBalance.add(new BalanceFlow(Integer.parseInt(parsed[0]), parsed[1], Integer.parseInt(parsed[2]), sdf.parse(parsed[3])));
            }
            catch(Exception e){
                System.out.println(e);
            }
            n++;
        }
    }
}
