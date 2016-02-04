package com.example.krasimirunarev.bingwallpaper.ui.fragment;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.krasimirunarev.bingwallpaper.R;
import com.example.krasimirunarev.bingwallpaper.adapter.MyPicturesAdapter;
import com.example.krasimirunarev.bingwallpaper.db.mypictures.MyPicture;
import com.example.krasimirunarev.bingwallpaper.db.mypictures.MyPictureModel;
import com.example.krasimirunarev.bingwallpaper.helpers.WallpaperHelper;
import com.example.krasimirunarev.bingwallpaper.listeners.OnClick;
import com.example.krasimirunarev.bingwallpaper.listeners.OnLongClick;

import net.yazeed44.imagepicker.model.ImageEntry;
import net.yazeed44.imagepicker.util.Picker;

import java.util.ArrayList;
import java.util.List;

public class MyPicturesFragment extends Fragment implements View.OnClickListener {

    public static final int REQUEST_CODE_READ_EXTERNAL_STORAGE = 10;

    // =============================================================================================
    // Private fields
    // =============================================================================================
    private MyPicturesAdapter mAdapter;
    private TextView mTvEmpty;


    // =============================================================================================
    // New instance
    // =============================================================================================
    public MyPicturesFragment() {
    }

    public static MyPicturesFragment newInstance() {
        return new MyPicturesFragment();
    }

    // =============================================================================================
    // Fragment lifecycle
    // =============================================================================================

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_my_pictures, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mAdapter = new MyPicturesAdapter(getContext());

        RecyclerView rvList = (RecyclerView) view.findViewById(R.id.rv_list);
        mTvEmpty = (TextView) view.findViewById(R.id.tv_empty);

        rvList.setHasFixedSize(true);
        rvList.setLayoutManager(new LinearLayoutManager(getContext()));
        rvList.setAdapter(mAdapter);

        view.findViewById(R.id.fab).setOnClickListener(this);

        mAdapter.setImages(MyPictureModel.getMyPictures());

        mAdapter.setOnLongClick(new OnLongClick() {
            @Override
            public void onLongClick(int position) {
                MyPicture pic = mAdapter.getItem(position);
                deleteDialog(pic);
            }
        });

        mAdapter.setOnClick(new OnClick() {
            @Override
            public void onClick(int position) {
                MyPicture pic = mAdapter.getItem(position);
                WallpaperHelper.updateWallpaper(getContext(), pic);
                MyPictureModel.setPictureAsWallpaper(pic);
                Toast.makeText(getContext(), R.string.wallpaper_updated, Toast.LENGTH_LONG).show();
            }
        });

        showHideEmptyView();
    }

    // =============================================================================================
    // Overridden methods
    // =============================================================================================
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab:
                askPermission();
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_READ_EXTERNAL_STORAGE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    pickImages();
                } else {
                    Toast.makeText(getContext(), R.string.read_external_storage_msg,
                                   Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

    // =============================================================================================
    // Private methods
    // =============================================================================================
    private void showHideEmptyView() {
        List<MyPicture> myPictures = MyPictureModel.getMyPictures();
        if(myPictures.size() == 0) {
            mTvEmpty.setVisibility(View.VISIBLE);
        } else {
            mTvEmpty.setVisibility(View.GONE);
        }
    }

    private void pickImages() {
        //You can change many settings in builder like limit , Pick mode and colors
        new Picker.Builder(getContext(), new MyPickListener(), R.style.AppTheme_NoActionBar)
            .build()
            .startActivity();
    }

    private void deleteDialog(final MyPicture pic) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.delete_image)
               .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int id) {
                       deletePicture(pic);
                   }
               })
               .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int id) {
                       // do nothing
                   }
               });
        // Create the AlertDialog object and return it
        builder.show();
    }

    private void deletePicture(MyPicture pic) {
        mAdapter.removePicture(pic);
        MyPictureModel.deletePicture(pic);
        showHideEmptyView();
    }

    private void askPermission() {
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.
            permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                getActivity(),
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                REQUEST_CODE_READ_EXTERNAL_STORAGE);
        } else {
            pickImages();
        }
    }

    // =============================================================================================
    // Private listener
    // =============================================================================================
    private class MyPickListener implements Picker.PickListener {

        @Override
        public void onPickedSuccessfully(final ArrayList<ImageEntry> images) {
            for (ImageEntry image : images) {
                String filename = image.path.substring(image.path.lastIndexOf("/") + 1);

                MyPicture pic = new MyPicture();
                pic.setFilename(filename);
                pic.setImagePath(image.path);
                pic.setImageId(image.imageId);

                if (MyPictureModel.findPicture(pic) == null) {
                    pic.save();
                }
            }

            mAdapter.setImages(MyPictureModel.getMyPictures());
            showHideEmptyView();
        }

        @Override
        public void onCancel() {

        }
    }

}
