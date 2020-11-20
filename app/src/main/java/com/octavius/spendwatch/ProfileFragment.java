package com.octavius.spendwatch;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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
    private final String TAG = "ProfileFragment";
    private List<Profile> list_profile;
    private BalanceProfile bp;
    private RecyclerView rv_profile;
    private RecycleViewAdapter adapter;
    private Button btn_add_profile;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        rv_profile = view.findViewById(R.id.rv_profile);
        btn_add_profile = view.findViewById(R.id.btn_add_profile);

        btn_add_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setView(R.layout.dialog_rename_profile).setTitle("Creating new file").setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Dialog d = (Dialog) dialog;
                        EditText et_new_file = d.findViewById(R.id.et_filename);
                        String new_file_name = et_new_file.getText().toString();
                        try {
                            bp = new BalanceProfile(getContext());
                            if(!(new_file_name.trim().isEmpty()) || !new_file_name.trim().equals("")){
                                String error = bp.createProfile(et_new_file.getText().toString());
                                if(error == ""){
                                    refreshRecycleView();
                                }
                                else{
                                    Toast.makeText(getContext(), error,
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                            else{
                                Toast.makeText(getContext(), "Profile name can't be empty",
                                        Toast.LENGTH_SHORT).show();
                                Log.i(TAG, "onClick: Filename is empty");
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }).setNegativeButton("Cancel", null).show();

            }
        });
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
