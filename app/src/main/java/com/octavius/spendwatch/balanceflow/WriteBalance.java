package com.octavius.spendwatch.balanceflow;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

public class WriteBalance{
    //private BalanceFlow bf;
    private FileWriter writer; 
    private File file, path;

    public WriteBalance(Context context, String file_name) throws FileNotFoundException, IOException{
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
