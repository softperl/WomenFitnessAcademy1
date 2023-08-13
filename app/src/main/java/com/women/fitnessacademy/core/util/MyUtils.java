package com.women.fitnessacademy.core.util;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;

import com.women.fitnessacademy.core.config.Config;

import java.security.MessageDigest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import timber.log.Timber;

/**
 * Created by Constant-Lab LLP on 03-08-2017.
 */

public class MyUtils {

    public static int ADS_VIEW_COUNT = 1;

    public static String formatDate(String data) {
        SimpleDateFormat parser = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US);
        Date date;
        String strData = "";
        try {
            date = parser.parse(data);
            strData = android.text.format.DateUtils.getRelativeTimeSpanString(date.getTime(), System.currentTimeMillis(), android.text.format.DateUtils.SECOND_IN_MILLIS).toString();
//            strData = DateUtils.getRelativeDateTimeString(context, date.getTime(), DateUtils.SECOND_IN_MILLIS, DateUtils.WEEK_IN_MILLIS, DateUtils.FORMAT_ABBREV_ALL).toString();
        } catch (ParseException e) {
            Timber.v("Exception %s", e.getMessage());
        }

        return strData;
    }


    public static String md5(final String toEncrypt) {
        try {
            final MessageDigest digest = MessageDigest.getInstance("md5");
            digest.update(toEncrypt.getBytes());
            final byte[] bytes = digest.digest();
            final StringBuilder sb = new StringBuilder();
            for (byte aByte : bytes) {
                sb.append(String.format("%02X", aByte));
            }
            return sb.toString().toLowerCase();
        } catch (Exception exc) {
            return "";
        }
    }

    //method to get the right URL to use in the intent //
    public static Intent openFacebook(Context context) {
        try {
            context.getPackageManager()
                    .getPackageInfo("com.facebook.katana", 0);
            return new Intent(Intent.ACTION_VIEW,
                    Uri.parse("fb://page/106644889166924"));
        } catch (Exception e) {
            return new Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://www.facebook.com/ClassyCuticlez/"));
        }


    }
}
