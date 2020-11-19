package com.octavius.spendwatch;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.octavius.spendwatch.balanceflow.BalanceProfile;
import com.octavius.spendwatch.balanceflow.Profile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ProfileFragment extends Fragment {
    List<Profile> list_profile;
    BalanceProfile bp;
    RecyclerView rv_profile;
    RecycleViewAdapter adapter;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        rv_profile = view.findViewById(R.id.rv_profile);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        try {
            refreshRecycleView();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void refreshRecycleView() throws IOException {
        list_profile = new ArrayList<Profile>();
        try {
            bp = new BalanceProfile(getContext());
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(bp.fetchListFile()){
            for (String file_name : bp.getListName()) {
                list_profile.add(new Profile(file_name));
            }
        }
        rv_profile.setAdapter(null);
        adapter = new RecycleViewAdapter(this, list_profile);
        rv_profile.setAdapter(adapter);
        rv_profile.setLayoutManager(new GridLayoutManager(getContext(), 2));
        getActivity().setTitle("Profile " + bp.getConfigFile());
    }

}
