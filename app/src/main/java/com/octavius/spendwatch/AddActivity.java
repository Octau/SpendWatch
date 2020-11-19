package com.octavius.spendwatch;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.octavius.spendwatch.balanceflow.BalanceProfile;
import com.octavius.spendwatch.balanceflow.ReadBalance;
import com.octavius.spendwatch.balanceflow.WriteBalance;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class AddActivity extends AppCompatActivity{
    private ReadBalance rb;
    private WriteBalance wb;
    private EditText et_desc, et_balance;
    private DatePicker dp_date;
    private SimpleDateFormat sdf;
    private TextView tv_error;
    private String block_character_set = ";", file_name;
    private BalanceProfile bp;


    private InputFilter filter = new InputFilter() {

        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            if (source != null && block_character_set.contains(("" + source))) {
                return "";
            }
            return null;
        }
    };

    @SuppressLint("SimpleDateFormat")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);
        assert getSupportActionBar() != null;   //null check
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        try {
            bp = new BalanceProfile(getApplicationContext());
            file_name = bp.getConfigFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            rb = new ReadBalance(getApplicationContext(), file_name);
        } catch (IOException e) {
            e.printStackTrace();
        }

        rb.getArrayListBalance("");

        tv_error = findViewById(R.id.error_container);
        et_desc = findViewById(R.id.et_desc);
        et_desc.setFilters(new InputFilter[]{filter ,new InputFilter.LengthFilter(20)});
        et_balance = findViewById(R.id.et_balance);
        dp_date = findViewById(R.id.dp_date);
        dp_date.setMaxDate(Calendar.getInstance().getTimeInMillis());
        sdf = new SimpleDateFormat("dd-MM-yyyy");
        try {
            bp = new BalanceProfile(getApplicationContext());
            file_name = bp.getConfigFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            wb = new WriteBalance(getApplicationContext(), file_name);//show back button
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.add_top_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add:
                if(et_desc.getText().toString().isEmpty() || et_balance.getText().toString().isEmpty())
                    tv_error.setTextSize(15);
                // User chose the "Settings" item, show the app settings UI...
                else{
                    wb.addBalance(rb.getLastId()+1, et_desc.getText().toString(), Integer.parseInt(et_balance.getText().toString()), dp_date.getDayOfMonth() + "-" + (Integer)(dp_date.getMonth()+1) + "-" + dp_date.getYear());
                    finish();
                    return true;
                }

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }
}