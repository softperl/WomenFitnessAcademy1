package com.women.fitnessacademy.views.fragments;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.women.fitnessacademy.application.ApplicationMain;
import com.women.fitnessacademy.core.data.Constants;

/**
 * Created by Constant-Lab LLP on 28-06-2016.
 */
public class BaseFragment extends Fragment {

    ApplicationMain applicationMain;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        applicationMain = (ApplicationMain) getContext().getApplicationContext();
    }

    protected boolean isUserLoggedIn() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext().getApplicationContext());
        return sharedPreferences.getString(Constants.KEY_FOR_TOKEN, null) != null;
    }
}
