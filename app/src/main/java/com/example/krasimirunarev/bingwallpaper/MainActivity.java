package com.example.krasimirunarev.bingwallpaper;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.krasimirunarev.bingwallpaper.listeners.OnDownloadComplete;
import com.example.krasimirunarev.bingwallpaper.ws.DownloadWallpaper;
import com.google.android.gms.gcm.GcmNetworkManager;
import com.google.android.gms.gcm.PeriodicTask;
import com.google.android.gms.gcm.Task;

public class MainActivity extends AppCompatActivity {

    private WallpaperAdapter mAdapter;
    private MenuItem mOnButton;
    private MenuItem mOffButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        RecyclerView rvList = (RecyclerView) findViewById(R.id.rv_list);
        final ProgressBar pbLoading = (ProgressBar) findViewById(R.id.pbLoading);

        rvList.setHasFixedSize(true);
        rvList.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new WallpaperAdapter(this);
        rvList.setAdapter(mAdapter);

        new DownloadWallpaper(this, 10, new OnDownloadComplete() {
            @Override
            public void onDownloadComplete(BingResponse images) {
                mAdapter.setImages(images.getImages());
                pbLoading.setVisibility(View.GONE);
            }
        }).execute();
    }

    private void startService() {
        GcmNetworkManager gcmNetworkManager = GcmNetworkManager.getInstance(this);
        PeriodicTask task = new PeriodicTask.Builder()
            .setService(DownloadService.class)
            .setTag(C.SERVICE_TAG)
            .setPeriod(10800L) // every 3 hours

                //            .setPeriod(30L) // every 30 seconds
            .setRequiredNetwork(Task.NETWORK_STATE_CONNECTED)
            .build();

        gcmNetworkManager.schedule(task);

        Prefs.setApplicationStatus(true);
        toggleOnOffMenuItems();
    }

    private void stopService() {
        GcmNetworkManager.getInstance(this).cancelTask(C.SERVICE_TAG, DownloadService.class);

        Prefs.setApplicationStatus(false);
        toggleOnOffMenuItems();
    }

    private void toggleOnOffMenuItems() {
        boolean isAppOn = Prefs.isApplicationOn();
        mOffButton.setVisible(isAppOn);
        mOnButton.setVisible(!isAppOn);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        mOnButton = menu.findItem(R.id.action_on);
        mOffButton = menu.findItem(R.id.action_off);

        if (Prefs.isApplicationOn()) {
            startService();
        }

        toggleOnOffMenuItems();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.action_on:
                startService();
                Toast.makeText(this, R.string.app_enabled, Toast.LENGTH_SHORT).show();
                break;
            case R.id.action_off:
                stopService();
                Toast.makeText(this, R.string.app_disabled, Toast.LENGTH_SHORT).show();
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
