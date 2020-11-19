package com.octavius.spendwatch;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.octavius.spendwatch.balanceflow.Profile;

import java.util.List;

public class RecycleViewAdapter extends RecyclerView.Adapter<RecycleViewAdapter.CustomViewHolder> {
    private Context mContext;
    private List<Profile> mData;

    public RecycleViewAdapter(Context mContext, List<Profile> mData) {
        this.mContext = mContext;
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
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {
        holder.tv_profile.setText(mData.get(position).getName());
        holder.cv_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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
        public CustomViewHolder(View itemView){
            super (itemView);
            tv_profile = itemView.findViewById(R.id.tv_profile);
            cv_profile = itemView.findViewById(R.id.cv_profile);
        }
    }
}
