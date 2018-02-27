package com.avadharwebworld.avadhar.Activity;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.avadharwebworld.avadhar.R;

import java.util.ArrayList;
import java.util.List;

public class JobPostResumeOne extends FragmentActivity implements ViewPager.OnPageChangeListener {
    private int[] layouts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_post_resume_one);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
        ViewPager pager=(ViewPager)findViewById(R.id.viewPager);
        pager.setAdapter(new PageViewManger(getSupportFragmentManager()));
        pager.addOnPageChangeListener(pageChangeListener);
        layouts = new int[]{R.layout.fragment_job_post_resume_one,R.layout.fragment_job_post_resumesection1,R.layout.fragment_job_post_resumesection2};

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }


    private class PageViewManger extends FragmentPagerAdapter{
        private List<Fragment> fragments;
        private LayoutInflater layoutInflater;
        public PageViewManger(FragmentManager fm) {
            super(fm);
            this.fragments = new ArrayList<Fragment>();
            fragments.add(new JobPostResumesection1());
            fragments.add(new JobPostResumesection2());

        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);

        }

        @Override
        public int getCount() {
            return fragments.size();
        }

//    @Override
//    public Object instantiateItem(ViewGroup container, int position) {
//        layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//
//        View view = layoutInflater.inflate(layouts[position], container, false);
//        container.addView(view);
//
//        return view;
//    }
    }
    private ViewPager.OnPageChangeListener pageChangeListener = new ViewPager.OnPageChangeListener() {

        int currentPosition = 0;

        @Override
        public void onPageSelected(int newPosition) {

//            FragmentLifecycle fragmentToHide = (FragmentLifecycle)pageAdapter.getItem(currentPosition);
//            fragmentToHide.onPauseFragment();
//
//            FragmentLifecycle fragmentToShow = (FragmentLifecycle)pageAdapter.getItem(newPosition);
//            fragmentToShow.onResumeFragment();

            currentPosition = newPosition;
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) { }

        public void onPageScrollStateChanged(int arg0) { }
    };
}
