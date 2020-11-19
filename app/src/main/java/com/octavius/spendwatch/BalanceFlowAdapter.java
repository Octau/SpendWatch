package com.octavius.spendwatch;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import com.octavius.spendwatch.balanceflow.BalanceFlow;
import com.octavius.spendwatch.balanceflow.BalanceProfile;
import com.octavius.spendwatch.balanceflow.ModifyBalance;

public class BalanceFlowAdapter extends ArrayAdapter<BalanceFlow> implements View.OnClickListener
{
    private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    private ModifyBalance mb;
    private BalanceProfile bp;
    private String file_name;
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
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        BalanceFlow bf = getItem(position);
        final ViewHolder viewHolder;
        if (convertView == null)
        {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.row_item, parent, false);
            viewHolder.et_id =  convertView.findViewById(R.id.tv_id);
            viewHolder.et_date =  convertView.findViewById(R.id.tv_date);
            viewHolder.tv_desc =  convertView.findViewById(R.id.tv_desc_info);
            viewHolder.et_prefix =  convertView.findViewById(R.id.tv_prefix);
            viewHolder.et_balance =  convertView.findViewById(R.id.tv_balance_info);
            viewHolder.iv_delete =  convertView.findViewById(R.id.btn_delete);
            viewHolder.iv_edit =  convertView.findViewById(R.id.btn_edit);
            convertView.setTag(viewHolder);
        }
        else{
                viewHolder = (ViewHolder) convertView.getTag();
        }
        lastPosition = position;

        viewHolder.et_id.setText(bf.getId().toString());

        viewHolder.tv_desc.setText(bf.getDesc());
        viewHolder.et_balance.setText(Integer.toString(Math.abs(bf.getBalance())));
        viewHolder.et_date.setText(sdf.format(bf.getDate()));

        //viewHolder.date.setText(bf.getDate().toString());
        //viewHolder.balance.setText(Math.abs(bf.getBalance()));
        if(bf.getBalance() > 0) {
            viewHolder.et_prefix.setText("+ Rp");
            viewHolder.et_prefix.setTextColor(getContext().getColor(R.color.colorBalanceGreen));
            viewHolder.et_balance.setTextColor(getContext().getColor(R.color.colorBalanceGreen));
        }
        else if(bf.getBalance() < 0){
            viewHolder.et_prefix.setText("-Rp");
            viewHolder.et_prefix.setTextColor(getContext().getColor(R.color.colorBalanceRed));
            viewHolder.et_balance.setTextColor(getContext().getColor(R.color.colorBalanceRed));
        }
        viewHolder.iv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    bp = new BalanceProfile(getContext());
                    file_name = bp.getConfigFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    mb = new ModifyBalance(getContext(), file_name);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    mb.deleteLine(Integer.parseInt(viewHolder.et_id.getText().toString()));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                remove(getItem(position));
            }
        });
        viewHolder.iv_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("ListViewEdit", "onClick: edit");
                openActivity(Integer.parseInt(viewHolder.et_id.getText().toString()), viewHolder.tv_desc.getText().toString(), Integer.parseInt(viewHolder.et_balance.getText().toString()), viewHolder.et_date.getText().toString());
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
        private TextView tv_desc, et_prefix, et_balance, et_id, et_date;
        private ImageView iv_delete, iv_edit;
    }

}
