package com.app.narlocks.helper;

import android.content.Context;
import android.net.ConnectivityManager;

import java.text.NumberFormat;

public class Extras {

    public static String brFormat(double value) {
        NumberFormat moneyFormat = NumberFormat.getCurrencyInstance();
        return " " + moneyFormat.format(value);
    }

    public static boolean isNetworkAvailable(Context context) {
        final ConnectivityManager connectivityManager = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }
}
