package com.octavius.spendwatch;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.octavius.spendwatch.balanceflow.ModifyBalance;
import com.octavius.spendwatch.balanceflow.WriteBalance;

import org.w3c.dom.Text;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class EditActivity extends AppCompatActivity {
    private WriteBalance wb;
    private ModifyBalance mb;
    private String desc;
    private Integer id, balance;
    private Date date;

    private EditText et_desc, et_balance;
    private TextView tv_id;
    private DatePicker dp_date;
    private String blockCharacterSet = ";";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        try {
            wb = new WriteBalance(getApplicationContext());
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            mb = new ModifyBalance(getApplicationContext());
        } catch (IOException e) {
            e.printStackTrace();
        }

        et_desc = (EditText)findViewById(R.id.et_desc);
        et_desc.setFilters(new InputFilter[]{filter});
        et_balance = (EditText) findViewById(R.id.et_balance);
        dp_date = (DatePicker)findViewById(R.id.dp_date);
        dp_date.setMaxDate(Calendar.getInstance().getTimeInMillis());


        desc = getIntent().getExtras().getString("desc");
        id = getIntent().getExtras().getInt("id");
        balance = getIntent().getExtras().getInt("balance");
        try {
            date = sdf.parse(getIntent().getExtras().getString("date"));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        et_desc.setText(desc);
        et_balance.setText(balance.toString());
        Log.i("Date", date.getDay() + "-" + date.getMonth() + "-" + (1900 + date.getYear()));
        dp_date.init((1900 + date.getYear()), date.getMonth(), date.getDay(), null);

        try {
            date = sdf.parse(getIntent().getExtras().getString("date"));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

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
                try {
                    mb.editLine(id, et_desc.getText().toString(), Integer.parseInt(et_balance.getText().toString()), dp_date.getDayOfMonth() + "-" + (Integer)(dp_date.getMonth()+1) + "-" + dp_date.getYear());
                } catch (IOException e) {
                    Log.e("Error", e.toString() );
                    e.printStackTrace();
                }
                finish();
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }
}