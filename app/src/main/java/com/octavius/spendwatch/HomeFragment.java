package com.octavius.spendwatch;
import android.Manifest;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import com.octavius.spendwatch.balanceflow.*;

import java.io.IOException;

public class HomeFragment extends Fragment {
    TextView tv_bulanan, tv_pengeluaran, tv_total;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        return view;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tv_bulanan = view.findViewById(R.id.tv_kas_bulan);
        tv_pengeluaran = view.findViewById(R.id.tv_pengeluaran_bulan);
        tv_total = view.findViewById(R.id.tv_total_kas);
    }

    @Override
    public void onResume() {
        super.onResume();
        try {

//            WriteBalance wb = new WriteBalance(getContext());
////            wb.addBalance(1, "aaa", 1000, "10-11-2020");
            ReadBalance rb = new ReadBalance(getContext());
            Integer[] total = rb.getThisMonthTotal();

            tv_bulanan.setText(total[0].toString());
            tv_pengeluaran.setText(total[1].toString());
            tv_total.setText(total[2].toString());
//            Log.i("KAS_BULAN", total[0].toString());
        } catch (IOException e) {
            Log.e("Error", e.toString());
        }
    }
}
