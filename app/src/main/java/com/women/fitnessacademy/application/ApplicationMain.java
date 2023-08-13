package com.women.fitnessacademy.application;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import androidx.appcompat.app.AppCompatDelegate;
import android.util.Log;

import com.women.fitnessacademy.BuildConfig;
import com.women.fitnessacademy.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;
import com.women.fitnessacademy.core.data.Constants;
import com.women.fitnessacademy.core.data.model.Post;
import com.women.fitnessacademy.core.data.model.Social;
import com.women.fitnessacademy.core.data.model.User;

import java.lang.reflect.Type;
import java.util.List;

import timber.log.Timber;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * Created by Junaid Asghar on 1-07-2019.
 */
public class ApplicationMain extends Application {

    public User user;
    public List<Post> favoritePosts;
    public List<Social> mSocialFavorites;
    public String token;

    @Override
    public void onCreate() {
        super.onCreate();
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/Roboto-Regular.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );

        Picasso.Builder builder = new Picasso.Builder(this);
        Picasso built = builder.build();
        if (BuildConfig.DEBUG) {
            built.setIndicatorsEnabled(false);
            built.setLoggingEnabled(true);
        } else {
            built.setIndicatorsEnabled(false);
        }
        Picasso.setSingletonInstance(built);

        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        } else {
            Timber.plant(new CrashReportingTree());
        }
        checkForFavorites();
        checkForToken();
    }

    private void checkForToken() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        token = sharedPreferences.getString(Constants.KEY_FOR_TOKEN, null);
        Gson gson = new Gson();
        String json = sharedPreferences.getString(Constants.KEY_FOR_USER, null);
        if (json != null) {
            user = gson.fromJson(json, User.class);
        }
    }


    private void checkForFavorites() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this.getApplicationContext());
        Gson gson = new Gson();
        String json = sharedPreferences.getString(Constants.KEY_FOR_FAVORITES, "");
        String socialJson = sharedPreferences.getString(Constants.KEY_FOR_INSTAGRAM_FAVORITES, "");
        if (!json.isEmpty()) {
            Type type = new TypeToken<List<Post>>() {
            }.getType();
            favoritePosts = gson.fromJson(json, type);
        }

        if (!socialJson.isEmpty()) {
            Type type = new TypeToken<List<Social>>() {
            }.getType();
            mSocialFavorites = gson.fromJson(socialJson, type);
        }
    }


    private static class CrashReportingTree extends Timber.Tree {

        @Override
        protected void log(int priority, String tag, String message, Throwable t) {
            if (priority == Log.VERBOSE || priority == Log.DEBUG || priority == Log.INFO) {
                return;
            }

            if (t != null) {
                if (priority == Log.ERROR) {
                    Timber.e(t, message, tag);
                } else if (priority == Log.WARN) {
                    Timber.w(t, message, tag);
                }
            }
        }

    }
}
