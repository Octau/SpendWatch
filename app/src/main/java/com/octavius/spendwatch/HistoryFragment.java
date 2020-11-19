package com.octavius.spendwatch;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.octavius.spendwatch.balanceflow.BalanceFlow;
import com.octavius.spendwatch.balanceflow.BalanceProfile;
import com.octavius.spendwatch.balanceflow.ReadBalance;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

public class HistoryFragment extends Fragment {
    private ArrayList<BalanceFlow> list_balance_flow;
    private ListView listView;
    private BalanceFlowAdapter adapter;
    private ReadBalance rb;
    private BalanceProfile bp;
    private String file_name;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history, container, false);
        listView = (ListView) view.findViewById(R.id.list);
        try {
            bp = new BalanceProfile(getContext());
            file_name = bp.getConfigFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            rb = new ReadBalance(getContext(), file_name);
        } catch (IOException e) {
            e.printStackTrace();
        }
        list_balance_flow = rb.getArrayListBalance("desc");

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        try {
            rb.refreshList();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        list_balance_flow = null;
        list_balance_flow = rb.getArrayListBalance("desc");
        if(list_balance_flow == null) Log.i("list", "null");
        for (BalanceFlow item: list_balance_flow) {
            Log.i("item", "onResume: " + item.getDesc());
        }
        listView.setAdapter(null);
        adapter = new BalanceFlowAdapter(list_balance_flow, getContext());
        listView.setAdapter(adapter);

    }
}
