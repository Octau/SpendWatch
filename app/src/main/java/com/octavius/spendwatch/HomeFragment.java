package com.octavius.spendwatch;
import android.annotation.SuppressLint;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import com.octavius.spendwatch.balanceflow.*;

import java.io.IOException;

public class HomeFragment extends Fragment {
    private TextView tv_balance_this_month, tv_spending, tv_total;
    private FrameLayout frame_total, frame_total_this_month;
    private Drawable colorGray, colorRed, colorGreen;
    private String file_name;
    private BalanceProfile bp;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        return view;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        try {
            bp = new BalanceProfile(getContext());
            file_name = bp.getConfigFile();
        } catch (IOException e) {
            e.printStackTrace();
        }


        super.onViewCreated(view, savedInstanceState);
        tv_balance_this_month = view.findViewById(R.id.tv_total_balance_this_month_info);
        tv_spending = view.findViewById(R.id.tv_spending_this_month_info);
        tv_total = view.findViewById(R.id.tv_total_balance_info);
        frame_total = view.findViewById(R.id.frame_total);
        frame_total_this_month = view.findViewById(R.id.frame_total_this_month);
        colorRed = new ColorDrawable(getContext().getColor(R.color.colorBalanceRed));
        colorGreen = new ColorDrawable(getContext().getColor(R.color.colorBalanceGreen));
        colorGray = new ColorDrawable(getContext().getColor(R.color.colorLightGray));
    }

    @SuppressLint("DefaultLocale")
    @Override
    public void onResume() {
        super.onResume();
        try {
            try {
                bp = new BalanceProfile(getContext());
                file_name = bp.getConfigFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
//            WriteBalance wb = new WriteBalance(getContext());
////            wb.addBalance(1, "aaa", 1000, "10-11-2020");
            ReadBalance rb = new ReadBalance(getContext(), file_name);
            Integer[] total = rb.getThisMonthTotal();

            if(total[0] < 0){
                tv_balance_this_month.setText(String.format("-Rp. %d", (Math.abs(total[0]))));
                frame_total_this_month.setBackground(colorRed);
            }
            else if(total[0] > 0){
                tv_balance_this_month.setText(String.format("Rp. %d", (Math.abs(total[0]))));
                frame_total_this_month.setBackground(colorGreen);
            }
            else{
                tv_balance_this_month.setText(String.format("Rp. %d", (Math.abs(total[0]))));
                frame_total_this_month.setBackground(colorGray);
            }

            if(total[2] < 0){
                tv_total.setText(String.format("-Rp. %d", (Math.abs(total[2]))));
                frame_total.setBackground(colorRed);
            }
            else if(total[2] > 0){
                tv_total.setText(String.format("Rp. %d", (Math.abs(total[2]))));
                frame_total.setBackground(colorGreen);
            }
            else{

                tv_total.setText(String.format("Rp. %d", (Math.abs(total[2]))));

                frame_total.setBackground(colorGray);
            }
            tv_spending.setText(String.format("Rp. %d", (Math.abs(total[1]))));
        } catch (IOException e) {
            Log.e("Error", e.toString());
        }
    }
}
