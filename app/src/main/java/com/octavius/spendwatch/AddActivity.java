package com.octavius.spendwatch;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.DatePicker;
import android.widget.EditText;

import com.octavius.spendwatch.balanceflow.WriteBalance;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class AddActivity extends AppCompatActivity{
    private WriteBalance wb;
    private EditText desc, balance;
    private DatePicker date;
    private SimpleDateFormat sdf;

    private String blockCharacterSet = ";";

    private InputFilter filter = new InputFilter() {

        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            if (source != null && blockCharacterSet.contains(("" + source))) {
                return "";
            }
            return null;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        assert getSupportActionBar() != null;   //null check
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        desc = findViewById(R.id.et_desc);
        desc.setFilters(new InputFilter[]{filter});
        balance = findViewById(R.id.et_balance);
        date = findViewById(R.id.dp_date);
        date.setMaxDate(Calendar.getInstance().getTimeInMillis());
        sdf = new SimpleDateFormat("dd-MM-yyyy");
        try {
            wb = new WriteBalance(getApplicationContext());//show back button
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
                // User chose the "Settings" item, show the app settings UI...
                wb.addBalance(1, desc.getText().toString(), Integer.parseInt(balance.getText().toString()), date.getDayOfMonth() + "-" + (Integer)(date.getMonth()+1) + "-" + date.getYear());
                finish();
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }
}