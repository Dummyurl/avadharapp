package com.avadharwebworld.avadhar.Adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.android.volley.toolbox.ImageLoader;
import com.avadharwebworld.avadhar.Application.AppController;
import com.avadharwebworld.avadhar.R;
import com.avadharwebworld.avadhar.Support.FeedImageView;

/**
 * Created by user-03 on 23-Nov-16.
 */

public class ImageSliderAdapter extends PagerAdapter {
    Context mContext;
    LayoutInflater inflater;
    String[]urls;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();
    public ImageSliderAdapter(Context context,String[] urlarray) {
        mContext = context;
        urls=urlarray;
        inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        return urls.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((LinearLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View itemView = inflater.inflate(R.layout.imageslideritem, container, false);

        FeedImageView imageView = (FeedImageView) itemView.findViewById(R.id.iv_slider_item);
        imageView.setImageUrl(urls[position],imageLoader);

        container.addView(itemView);

        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((LinearLayout) object);
    }
}
