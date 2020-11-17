package com.octavius.spendwatch;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.octavius.spendwatch.balanceflow.BalanceFlow;
import com.octavius.spendwatch.balanceflow.ReadBalance;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class HistoryFragment extends Fragment {
    private ArrayList<BalanceFlow> daftar;
    private ListView listView;
    private BalanceFlowAdapter adapter;
    private ReadBalance rb;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history, container, false);
        listView = (ListView) view.findViewById(R.id.list);

        try {
            rb = new ReadBalance(getContext());
        } catch (IOException e) {
            e.printStackTrace();
        }
        daftar = rb.getArrayListBalance();

        adapter = new BalanceFlowAdapter(daftar, getContext());
        listView.setAdapter(adapter);

        return view;
    }

}
