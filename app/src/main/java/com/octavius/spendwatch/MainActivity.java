package com.octavius.spendwatch;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.JobIntentService;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.google.android.material.bottomnavigation.BottomNavigationView;


public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
    private FrameLayout frame_layout;
    private BottomNavigationView bottom_navigation_view;
    private FragmentTransaction ft;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActivityCompat.requestPermissions(this,  new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        frame_layout = findViewById(R.id.fl_container);
        bottom_navigation_view = findViewById(R.id.bn_main);
        bottom_navigation_view.setOnNavigationItemSelectedListener(this);
        loadFragment(new HomeFragment());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.top_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add:
                // User chose the "Settings" item, show the app settings UI...
                Intent intent = new Intent(this, AddActivity.class);
                this.startActivity(intent);
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item){
        Fragment fragment = null;
        switch (item.getItemId()){
            case R.id.home_menu:
                fragment = new HomeFragment();
                break;
            case R.id.history_menu:
                fragment = new HistoryFragment();
                break;
            case R.id.stats_menu:
                fragment = new StatisticFragment();
                break;
            case R.id.profile_menu:
                fragment = new ProfileFragment();
                break;
            default:
                return false;

        }

        return loadFragment(fragment);
    }

    private boolean loadFragment(Fragment fragment) {
        if (fragment != null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fl_container, fragment)
                    .commit();
        }
        return false;
    }
}