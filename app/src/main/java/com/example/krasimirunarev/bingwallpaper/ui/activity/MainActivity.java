package com.example.krasimirunarev.bingwallpaper.ui.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.example.krasimirunarev.bingwallpaper.C;
import com.example.krasimirunarev.bingwallpaper.Prefs;
import com.example.krasimirunarev.bingwallpaper.R;
import com.example.krasimirunarev.bingwallpaper.WallpaperChangeService;
import com.example.krasimirunarev.bingwallpaper.adapter.HomeAdapter;
import com.google.android.gms.gcm.GcmNetworkManager;
import com.google.android.gms.gcm.PeriodicTask;
import com.google.android.gms.gcm.Task;

public class MainActivity extends AppCompatActivity {

    private ViewPager mPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        mPager = (ViewPager) findViewById(R.id.vp_pager);
        TabLayout tabs = (TabLayout) findViewById(R.id.tl_tabs);

        mPager.setAdapter(new HomeAdapter(this, getSupportFragmentManager()));
        mPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabs));

        tabs.setupWithViewPager(mPager);
    }

    // =============================================================================================
    // Overridden methods
    // =============================================================================================
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.action_settings:
                sourcePickerDialog();
                break;

        }

        return super.onOptionsItemSelected(item);
    }

    // =============================================================================================
    // Private methods
    // =============================================================================================
    private void sourcePickerDialog() {
        View v = getLayoutInflater().inflate(R.layout.dialog_souce_picker, null);

        final CheckBox cbBing = (CheckBox) v.findViewById(R.id.cb_bing);
        final CheckBox cbCustom = (CheckBox) v.findViewById(R.id.cb_custom);

        cbBing.setChecked(Prefs.isBingSourceUpdatingOn());
        cbCustom.setChecked(Prefs.isCustomSourceUpdatingOn());

        cbBing.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!isChecked && !cbBing.isChecked()) {
                    cbBing.setChecked(false);
                } else {
                    cbBing.setChecked(isChecked);
                    cbCustom.setChecked(!isChecked);
                }
            }
        });

        cbCustom.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!isChecked && !cbCustom.isChecked()) {
                    cbCustom.setChecked(false);
                } else {
                    cbBing.setChecked(!isChecked);
                    cbCustom.setChecked(isChecked);
                }
            }
        });

        AlertDialog.Builder dialog = new AlertDialog.Builder(this)
            .setTitle(R.string.choose_source)
            .setPositiveButton(R.string.ok,
                               new DialogInterface.OnClickListener() {
                                   public void onClick(DialogInterface dialog, int whichButton) {
                                       Prefs.setBingSourceUpdatingStatus(cbBing.isChecked());
                                       Prefs.setCustomSourceUpdatingStatus(cbCustom.isChecked());

                                       if (Prefs.isApplicationUpdatingOn()) {
                                           startService();
                                       } else {
                                           stopService();
                                       }
                                   }
                               }
            )
            .setNegativeButton(R.string.cancel,
                               new DialogInterface.OnClickListener() {
                                   public void onClick(DialogInterface dialog, int whichButton) {
                                       dialog.dismiss();
                                   }
                               }
            );

        dialog.setView(v);
        dialog.show();
    }

    private void startService() {
        Log.d("asd", "Starting service");
        GcmNetworkManager gcmNetworkManager = GcmNetworkManager.getInstance(this);
        PeriodicTask task = new PeriodicTask.Builder()
            .setService(WallpaperChangeService.class)
            .setTag(C.SERVICE_TAG)
                            .setPeriod(10800L) // every 3 hours

//            .setPeriod(30L) // every 30 seconds
            .setRequiredNetwork(Task.NETWORK_STATE_CONNECTED)
            .build();

        gcmNetworkManager.schedule(task);
    }

    private void stopService() {
        Log.d("asd", "Stopping service");
        GcmNetworkManager.getInstance(this)
                         .cancelTask(C.SERVICE_TAG, WallpaperChangeService.class);
    }
}
