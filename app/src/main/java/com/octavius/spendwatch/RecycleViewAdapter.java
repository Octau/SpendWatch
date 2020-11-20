package com.octavius.spendwatch;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.octavius.spendwatch.balanceflow.BalanceProfile;
import com.octavius.spendwatch.balanceflow.Profile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class RecycleViewAdapter extends RecyclerView.Adapter<RecycleViewAdapter.CustomViewHolder> {
    private final String TAG = "RecycleViewAdapter";
    private ProfileFragment fragment;
    private Context mContext;
    private List<Profile> mData;
    private BalanceProfile bp;

    public RecycleViewAdapter(ProfileFragment fragment, List<Profile> mData) {
        this.fragment = fragment;
        this.mContext = fragment.getContext();
        this.mData = mData;
    }

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        LayoutInflater mInflater = LayoutInflater.from(mContext);
        view = mInflater.inflate(R.layout.card, parent, false);

        return new CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final CustomViewHolder holder, final int position) {
        final String profile_name = mData.get(position).getName();
        holder.tv_profile.setText(profile_name);
        holder.cv_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "onClick: ");
                try {
                    bp = new BalanceProfile(v.getContext());
                    bp.setDefaultFile(profile_name);
                    fragment.refreshRecycleView();
                        Toast.makeText(mContext, "Switched profile to " + profile_name,
                            Toast.LENGTH_SHORT).show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        holder.cv_profile.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(final View v) {
                Log.i(TAG, "onLongClick: ");

                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);

                builder.setTitle(mData.get(position).getName()).setItems(R.array.profile_choice, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.i(TAG, "onClick: " + which);
                        switch (which){
                            case 0:
                                AlertDialog.Builder builder_rename = new AlertDialog.Builder(mContext);

                                builder_rename.setMessage("Renaming " + profile_name + " Profile").setView(R.layout.dialog_rename_profile).setPositiveButton("Rename", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Dialog f = (Dialog) dialog;
                                        EditText et_new_file = f.findViewById(R.id.et_filename);
                                        TextView tv_error = f.findViewById(R.id.tv_file_error);
                                        String new_file_name = et_new_file.getText().toString();
                                        String value = "";
                                        if(!(new_file_name.trim().isEmpty()) || !new_file_name.trim().equals("")){
                                            try {
                                                bp = new BalanceProfile(mContext);
                                                String error = bp.renameProfile(profile_name, new_file_name);
                                                if(error.equals("")) {
                                                    if(profile_name.trim().equals(bp.getConfigFile())){
                                                        Log.i(TAG, "onClick: set default file");
                                                        bp.setDefaultFile(new_file_name);
                                                    }
                                                }
                                                else{
                                                    Toast.makeText(mContext, error,
                                                            Toast.LENGTH_SHORT).show();
                                                }
                                                fragment.refreshRecycleView();
                                            } catch (IOException e) {
                                                e.printStackTrace();
                                            }
                                            Log.i(TAG, "onClick: new name " + new_file_name);
                                        }
                                        else{
                                            Toast.makeText(mContext, "Profile name can't be empty",
                                                    Toast.LENGTH_SHORT).show();
                                            Log.i(TAG, "onClick: Filename is empty");
                                        }
                                    }
                                }).setNegativeButton("Cancel", null).show();

                                break;
                            case 1:
                                AlertDialog.Builder builder= new AlertDialog.Builder(mContext);
                                builder.setTitle("Deleting " + profile_name).setMessage("Are you sure you want to delete this profile?").setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        try {
                                            bp = new BalanceProfile(mContext);
                                            String error = bp.deleteProfile(profile_name);
                                            if(error.equals("")){
                                                if(profile_name.equals(bp.getConfigFile())) bp.createDefaultFile();
                                                fragment.refreshRecycleView();
                                                Toast.makeText(mContext, "Deleted " + profile_name,
                                                        Toast.LENGTH_SHORT).show();
                                            }
                                            else Toast.makeText(mContext, error,
                                                    Toast.LENGTH_SHORT).show();
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }).setNegativeButton("Cancel", null).show();

                                break;
                            default:
                                break;
                        }
                    }
                }).show();
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class CustomViewHolder extends RecyclerView.ViewHolder{
        TextView tv_profile;
        CardView cv_profile;
        ImageView iv_profile;
        public CustomViewHolder(View itemView){
            super (itemView);
            tv_profile = itemView.findViewById(R.id.tv_profile);
            cv_profile = itemView.findViewById(R.id.cv_profile);
            iv_profile = itemView.findViewById(R.id.iv_profile);        }
    }
}
