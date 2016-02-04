package com.example.krasimirunarev.bingwallpaper.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.krasimirunarev.bingwallpaper.ui.fragment.BingFragment;
import com.example.krasimirunarev.bingwallpaper.R;
import com.example.krasimirunarev.bingwallpaper.ui.fragment.MyPicturesFragment;

/**
 * @author MentorMate.
 */
public class HomeAdapter extends FragmentPagerAdapter {

    private Context mContext;

    public HomeAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment;
        switch (position) {
            case 0:
                fragment = BingFragment.newInstance();
                break;
            case 1:
                fragment = MyPicturesFragment.newInstance();
                break;
            default:
                fragment = BingFragment.newInstance();
                break;
        }

        return fragment;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        String title;

        switch (position) {
            case 0:
                title = mContext.getString(R.string.bing_tab);
                break;
            case 1:
                title = mContext.getString(R.string.my_tab);
                break;
            default:
                title = mContext.getString(R.string.bing_tab);
                break;
        }

        return title;
    }
}
