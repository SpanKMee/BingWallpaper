package com.example.krasimirunarev.bingwallpaper.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.krasimirunarev.bingwallpaper.R;
import com.example.krasimirunarev.bingwallpaper.adapter.WallpaperAdapter;
import com.example.krasimirunarev.bingwallpaper.helpers.WallpaperHelper;
import com.example.krasimirunarev.bingwallpaper.listeners.OnClick;
import com.example.krasimirunarev.bingwallpaper.listeners.OnDownloadComplete;
import com.example.krasimirunarev.bingwallpaper.ws.BingResponse;
import com.example.krasimirunarev.bingwallpaper.ws.DownloadWallpaper;

public class BingFragment extends Fragment {

    private WallpaperAdapter mAdapter;

    public BingFragment() {
    }

    public static BingFragment newInstance() {
        return new BingFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_bing, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView rvList = (RecyclerView) view.findViewById(R.id.rv_list);
        final ProgressBar pbLoading = (ProgressBar) view.findViewById(R.id.pbLoading);

        rvList.setHasFixedSize(true);
        rvList.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter = new WallpaperAdapter(getContext());
        rvList.setAdapter(mAdapter);

        new DownloadWallpaper(getContext(), 10, new OnDownloadComplete() {
            @Override
            public void onDownloadComplete(BingResponse images) {
                mAdapter.setImages(images.getImages());
                pbLoading.setVisibility(View.GONE);
            }
        }).execute();

        mAdapter.setListener(new OnClick() {
            @Override
            public void onClick(int position) {
                BingResponse.Image image = mAdapter.getItem(position);
                WallpaperHelper.updateWallpaper(getContext(), image.getInputStream());

                Toast.makeText(getContext(), R.string.wallpaper_updated, Toast.LENGTH_SHORT)
                     .show();
            }
        });
    }

}
