package com.example.krasimirunarev.bingwallpaper.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.krasimirunarev.bingwallpaper.R;
import com.example.krasimirunarev.bingwallpaper.Resolutions;
import com.example.krasimirunarev.bingwallpaper.listeners.OnClick;
import com.example.krasimirunarev.bingwallpaper.ws.BingResponse;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * @author MentorMate
 */
public class WallpaperAdapter extends RecyclerView.Adapter<WallpaperAdapter.ViewHolder> {

    private List<BingResponse.Image> mImages = new ArrayList<>();
    private Context mContext;
    private OnClick mOnClick;


    public WallpaperAdapter(Context context) {
        mContext = context;
    }

    public void setImages(List<BingResponse.Image> images) {
        mImages.clear();
        mImages.addAll(images);
        notifyDataSetChanged();
    }

    public void setListener(OnClick onClick) {
        mOnClick = onClick;
    }

    public BingResponse.Image getItem(int position) {
        return mImages.get(position);
    }

    @Override
    public WallpaperAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater
            .from(parent.getContext()).inflate(R.layout.list_image, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(WallpaperAdapter.ViewHolder h, int position) {
        BingResponse.Image image = mImages.get(position);
        Picasso.with(mContext).load(image.getFullUrl(Resolutions.MED)).into(h.mImage);
        h.mTitle.setText(image.getCopyright());
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
