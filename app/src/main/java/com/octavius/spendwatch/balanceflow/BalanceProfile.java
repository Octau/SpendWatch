package com.octavius.spendwatch.balanceflow;

import android.content.Context;
import android.util.Log;

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class BalanceProfile {
    private final String TAG = "BalanceProfile";
    private Scanner sc;
    private File dir, file, config_file;
    private File[] list;
    private String[]  parsed;
    private List<String> list_name;
    private Context context;

    public BalanceProfile(Context context) throws IOException{
        this.context = context;
        config_file = new File(context.getFilesDir().getPath() + "config.txt");
        dir = new File( context.getFilesDir().getPath() + "/balance/");
        if(!dir.exists()){
            dir.mkdirs();
            if(dir.isDirectory()){
                //System.out.println("created dir");
                Log.i(TAG, "created dir");
            }
        }
    }

    public void printListFile(){
        System.out.println("Printing list file");
        list = dir.listFiles();
        for (int i = 0; i < list.length; i++) {
//            System.out.println(list[i].getName());
            Log.i(TAG, list[i].getName());
        }
    }

    public Boolean fetchListFile(){
        list_name = new ArrayList<String>();
        list = dir.listFiles();
        for (int i = 0; i < list.length; i++) {
            System.out.println(list[i].getName());
            parsed = list[i].getName().split("[.]");
            list_name.add(parsed[0]);
        }
        return true;
    }

    public List<String> getListName(){
        return list_name;
    }

    public void createNewFile(String file_name){
        file = new File(context.getFilesDir().getPath() + "/" + file_name + ".txt");
        if(!file.exists()){
            try {
                file.createNewFile();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    public Boolean deleteFile(String file_name){
        file = new File(dir.getAbsolutePath() + "/" + file_name + ".txt");
        if(file.exists()){
            file.delete();
        }
        return true;
    }

    public Boolean setDefaultFile(String file_name) throws IOException {
        FileWriter writer = new FileWriter(config_file,false);
        writer.write(file_name);
        return true;
    }

    public void createDefaultFile() throws IOException{
        config_file.createNewFile();
        FileWriter writer = new FileWriter(config_file,false);
        writer.write("default");
    }

    public String getConfigFile() throws IOException {
        if(!config_file.exists()){
            this.createDefaultFile();
            return "default";
        }
        else {
            String file_name = "default";

            sc = new Scanner(config_file);
            while(sc.hasNextLine()){
                file_name = sc.nextLine();
            }
            return file_name;
        }
    }
}
