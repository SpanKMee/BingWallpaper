package com.example.krasimirunarev.bingwallpaper.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.krasimirunarev.bingwallpaper.R;
import com.example.krasimirunarev.bingwallpaper.db.mypictures.MyPicture;
import com.example.krasimirunarev.bingwallpaper.listeners.OnClick;
import com.example.krasimirunarev.bingwallpaper.listeners.OnLongClick;

import java.util.ArrayList;
import java.util.List;

/**
 * @author MentorMate
 */
public class MyPicturesAdapter extends RecyclerView.Adapter<MyPicturesAdapter.ViewHolder> {

    private List<MyPicture> mImages = new ArrayList<>();
    private Context mContext;
    private OnClick mOnClick;
    private OnLongClick mOnLongClick;

    public MyPicturesAdapter(Context context) {
        mContext = context;
    }

    public void setImages(List<MyPicture> images) {
        if(images != null) {
            mImages.clear();
            mImages.addAll(images);
            notifyDataSetChanged();
        }
    }

    public void setOnClick(OnClick onClick) {
        mOnClick = onClick;
    }

    public void setOnLongClick(OnLongClick onLongClick) {
        mOnLongClick = onLongClick;
    }

    public MyPicture getItem(int position) {
        return mImages.get(position);
    }

    public void removePicture(MyPicture pic) {
        notifyItemRemoved(mImages.indexOf(pic));
        mImages.remove(pic);
    }

    @Override
    public MyPicturesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater
            .from(parent.getContext()).inflate(R.layout.list_image, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyPicturesAdapter.ViewHolder h, int position) {
        MyPicture image = mImages.get(position);

        Bitmap thumbImage = MediaStore.Images.Thumbnails.getThumbnail(
            mContext.getContentResolver(), image.getImageId(),
            MediaStore.Images.Thumbnails.MINI_KIND,
            null);

        h.mImage.setImageBitmap(thumbImage);

        h.mTitle.setText(image.getFilename());
    }

    @Override
    public int getItemCount() {
        return mImages.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView mImage;
        public TextView mTitle;
        public FrameLayout mRow;

        public ViewHolder(View v) {
            super(v);
            mImage = (ImageView) v.findViewById(R.id.iv_image);
            mTitle = (TextView) v.findViewById(R.id.tv_title);
            mRow = (FrameLayout) v.findViewById(R.id.fl_row);

            mRow.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if(mOnLongClick != null) {
                        mOnLongClick.onLongClick(getAdapterPosition());
                    }
                    return false;
                }
            });
            mRow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mOnClick != null) {
                        mOnClick.onClick(getAdapterPosition());
                    }
                }
            });

        }
    }
}
