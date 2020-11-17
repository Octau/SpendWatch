package com.octavius.spendwatch;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import com.octavius.spendwatch.balanceflow.BalanceFlow;
import com.octavius.spendwatch.balanceflow.ModifyBalance;

import java.util.ArrayList;

public class BalanceFlowAdapter extends ArrayAdapter<BalanceFlow> implements View.OnClickListener
{
    private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    private ModifyBalance mb;
    private int lastPosition = -1;
    private ArrayList<BalanceFlow> dataSet;
    private Context mContext;

    public BalanceFlowAdapter(ArrayList<BalanceFlow> data, Context context) {
        super(context, R.layout.row_item, data);
        this.dataSet = data;
        this.mContext=context;
    }

    @Override
    public void onClick(View v) {
        int position=(Integer) v.getTag();
        Object object= getItem(position);
        Toast.makeText(mContext, "Info at " + (position+1), Toast.LENGTH_SHORT).show();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        BalanceFlow bf = getItem(position);
        final ViewHolder viewHolder;
        final View result;
        if (convertView == null)
        {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.row_item, parent, false);
            viewHolder.id = (TextView) convertView.findViewById(R.id.tv_id);
            viewHolder.date = (TextView) convertView.findViewById(R.id.tv_date);
            viewHolder.desc = (TextView) convertView.findViewById(R.id.tv_desc_info);
            viewHolder.prefix = (TextView) convertView.findViewById(R.id.tv_prefix);
            viewHolder.balance = (TextView) convertView.findViewById(R.id.tv_balance_info);
            viewHolder.delete = (ImageView) convertView.findViewById(R.id.btn_delete);
            viewHolder.edit = (ImageView) convertView.findViewById(R.id.btn_edit);
            convertView.setTag(viewHolder);
        }
        else{
                viewHolder = (ViewHolder) convertView.getTag();
        }
        lastPosition = position;

        viewHolder.id.setText(bf.getId().toString());

        viewHolder.desc.setText(bf.getDesc());
        viewHolder.balance.setText(Integer.toString(Math.abs(bf.getBalance())));
        viewHolder.date.setText(sdf.format(bf.getDate()));

        //viewHolder.date.setText(bf.getDate().toString());
        //viewHolder.balance.setText(Math.abs(bf.getBalance()));
        if(bf.getBalance() > 0) {
            viewHolder.prefix.setText("+ Rp");
            viewHolder.prefix.setTextColor(getContext().getColor(R.color.colorBalanceGreen));
            viewHolder.balance.setTextColor(getContext().getColor(R.color.colorBalanceGreen));
        }
        else if(bf.getBalance() < 0){
            viewHolder.prefix.setText("-Rp");
            viewHolder.prefix.setTextColor(getContext().getColor(R.color.colorBalanceRed));
            viewHolder.balance.setTextColor(getContext().getColor(R.color.colorBalanceRed));
        }
        viewHolder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    mb = new ModifyBalance(getContext());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    mb.deleteLine(Integer.parseInt(viewHolder.id.getText().toString()));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                remove(getItem(position));
            }
        });
        Intent newActivity1 = new Intent(this.getContext() , AddActivity.class);
        viewHolder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("ListViewEdit", "onClick: edit");
                openActivity(Integer.parseInt(viewHolder.id.getText().toString()), viewHolder.desc.getText().toString(), Integer.parseInt(viewHolder.balance.getText().toString()), viewHolder.date.getText().toString());
            }
        });
        return convertView;
    }

    public void openActivity(Integer id, String desc, Integer balance, String date){
        Intent intent_edit = new Intent(this.getContext(), EditActivity.class);
        intent_edit.putExtra("id", id);
        intent_edit.putExtra("desc", desc);
        intent_edit.putExtra("balance", balance);
        intent_edit.putExtra("date", date);
        getContext().startActivity(intent_edit);
    }
    private static class ViewHolder {
        private TextView desc, prefix, balance, id, date;
        private ImageView delete, edit;
    }

}
