package com.women.fitnessacademy.views.activities;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.appcompat.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.women.fitnessacademy.R;
import com.women.fitnessacademy.core.data.Constants;
import com.women.fitnessacademy.core.data.model.Gallery;
import com.women.fitnessacademy.views.custom.HackyViewPager;
import com.women.fitnessacademy.views.fragments.FullImageFragment;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

public class FullImageSliderActivity extends BaseActivity {


    private static final int UI_ANIMATION_DELAY = 300;
    private final Handler mHideHandler = new Handler();
    View mContentView;
    private final Runnable mHidePart2Runnable = new Runnable() {
        @SuppressLint("InlinedApi")
        @Override
        public void run() {
            // Delayed removal of status and navigation bar

            // Note that some of these constants are new as of API 16 (Jelly Bean)
            // and API 19 (KitKat). It is safe to use them, as they are inlined
            // at compile-time and do nothing on earlier devices.
            mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        }
    };
    private final Runnable mHideRunnable = new Runnable() {
        @Override
        public void run() {
            hide();
        }
    };
    HackyViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_image_slider);
        initView();
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            List<Gallery> photos = getIntent().getParcelableArrayListExtra(Constants.IMAGES_LIST_KEY);
            int position = getIntent().getExtras().getInt(Constants.IMAGE_POSITION_KEY);
            List<Fragment> fragments = new Vector<>();
            for (Gallery gallery : photos) {
                fragments.add(FullImageFragment.getInstance(gallery));
            }
            PhotosPagerAdapter photosPagerAdapter = new PhotosPagerAdapter(getSupportFragmentManager(), fragments, null);
            viewPager.setAdapter(photosPagerAdapter);
            viewPager.setCurrentItem(position);
        }
    }


    private void initView() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        viewPager = findViewById(R.id.view_pager);
        mContentView = findViewById(R.id.content_view);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        delayedHide(200);
    }

    private void hide() {


        // Schedule a runnable to remove the status and navigation bar after a delay

        mHideHandler.postDelayed(mHidePart2Runnable, UI_ANIMATION_DELAY);
    }

    private void delayedHide(int delayMillis) {
        mHideHandler.removeCallbacks(mHideRunnable);
        mHideHandler.postDelayed(mHideRunnable, delayMillis);
    }

    private class PhotosPagerAdapter extends FragmentStatePagerAdapter {
        private List<Fragment> fragments;
        private Map<Integer, Fragment> retainedFragments;
        private List<String> titles;

        PhotosPagerAdapter(FragmentManager fm, @NonNull List<Fragment> fragments, @NonNull List<String> titles) {
            super(fm);
            this.fragments = fragments;
            this.retainedFragments = new HashMap<>();
            this.titles = titles;
        }

        @Override
        public Fragment getItem(int position) {
            // Check if the fragment at this position has been retained by the PagerAdapter
            if (retainedFragments.containsKey(position)) {
                return retainedFragments.get(position);
            }
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return this.fragments.size();
        }

        @NonNull
        public List<Fragment> getFragments() {
            return fragments;
        }

        @NonNull
        public Collection<Fragment> getRetainedFragments() {
            return retainedFragments.values();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            if (titles != null)
                return titles.get(position);
            else
                return null;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            Fragment fragment = (Fragment) super.instantiateItem(container, position);
            retainedFragments.put(position, fragment);
            return fragment;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            if (retainedFragments.containsKey(position)) {
                retainedFragments.remove(position);
            }
            super.destroyItem(container, position, object);
        }
    }
}
