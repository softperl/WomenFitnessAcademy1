package com.women.fitnessacademy.views.adapters;
/**
 * Created by Constant-Lab LLP on 29-07-2016.
 */

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CustomPagerAdapter extends FragmentPagerAdapter {
    private List<Fragment> fragments;
    private Map<Integer, Fragment> retainedFragments;
    private List<String> titles;

    public CustomPagerAdapter(FragmentManager fm, @NonNull List<Fragment> fragments, @NonNull List<String> titles) {
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
