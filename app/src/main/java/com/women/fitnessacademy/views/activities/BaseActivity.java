package com.women.fitnessacademy.views.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.MobileAds;
import com.women.fitnessacademy.R;
import com.women.fitnessacademy.application.ApplicationMain;
import com.women.fitnessacademy.core.config.Config;
import com.women.fitnessacademy.core.data.Constants;

import timber.log.Timber;

/**
 * Created by Constant-Lab LLP on 21-07-2016.
 */
public abstract class BaseActivity extends AppCompatActivity {

    protected ApplicationMain applicationMain;
    AlertDialog.Builder builder;
    AlertDialog alertDialog;
    private BroadcastReceiver mNetworkReceiver;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        applicationMain = (ApplicationMain) getApplicationContext();
        mNetworkReceiver = new NetworkChangeReceiver();
        setupBuilder();
        registerNetworkBroadcastForNougat();
        if (Config.ABMOB_APP_ID != null && !Config.ABMOB_APP_ID.isEmpty()) {
            try {
                MobileAds.initialize(this);
            } catch (Exception e) {
                Timber.e("Unable to Initialize");
            }
        }
    }

    private void setupBuilder() {
        builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.no_internet_connection);
        builder.setCancelable(false);
        builder.setMessage(R.string.mobile_data_wifi);
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertDialog = builder.create();
    }

//    @Override
//    protected void attachBaseContext(Context newBase) {
//        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
//    }

    protected boolean isUserLoggedIn() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        return sharedPreferences.getString(Constants.KEY_FOR_TOKEN, null) != null;
    }

    private void registerNetworkBroadcastForNougat() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            registerReceiver(mNetworkReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        }
    }

    protected void unregisterNetworkChanges() {
        try {
            unregisterReceiver(mNetworkReceiver);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        unregisterNetworkChanges();
        super.onDestroy();
    }

    public void dialog(boolean value) {
        if (value) {
            Handler handler = new Handler();
            Runnable delayrunnable = new Runnable() {
                @Override
                public void run() {
                    if (alertDialog.isShowing())
                        alertDialog.dismiss();
                }
            };
            handler.postDelayed(delayrunnable, 3000);
        } else {
            alertDialog.show();
        }
    }

    public class NetworkChangeReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                if (isOnline(context)) {
                    dialog(true);
                    Timber.e("Online Connect Intenet");
                } else {
                    dialog(false);
                    Timber.e("Conectivity Failure!!");
                }
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        }

        private boolean isOnline(Context context) {
            try {
                ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo netInfo = cm.getActiveNetworkInfo();
                //should check null because in airplane mode it will be null
                return (netInfo != null && netInfo.isConnected());
            } catch (NullPointerException e) {
                e.printStackTrace();
                return false;
            }
        }
    }

}
