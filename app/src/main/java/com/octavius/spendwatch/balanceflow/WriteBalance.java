package com.octavius.spendwatch.balanceflow;

import android.content.Context;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

public class WriteBalance{
    //private BalanceFlow bf;
    private FileWriter writer; 
    private File file;

    public WriteBalance(Context context) throws FileNotFoundException, IOException{
        file = new File(context.getFilesDir().getPath() + "/balance/BalanceFlow.txt");
        if(!file.exists()){
            file.createNewFile();
        }
    }
    
    private void writerNew() throws IOException {
        writer = new FileWriter(file, true);
    }

    private void writerClose() throws IOException {
        writer.close();
    }

    public void addBalance(Integer id, String desc, Integer balance, String date){
        try {
            this.writerNew();
            writer.write(String.format("%d;%s;%d;%s\n", id, desc, balance, date));
            this.writerClose();
        } catch (IOException e) {
            Log.i("AddError", e.toString());
        }
    }


    

}
