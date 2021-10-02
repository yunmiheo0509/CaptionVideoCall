package com.cationvideocall.example.captionvideocall;

import android.content.Context;
import android.content.SharedPreferences;

public class MySharedPreferences {
    private static String MY_ACCOUNT = "account";

    public static void setUserId(Context context, String input) {
        SharedPreferences prefs = context.getSharedPreferences(MY_ACCOUNT, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("MY_ID", input);
        editor.commit();
    }

    public static String getUserId(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(MY_ACCOUNT, Context.MODE_PRIVATE);
        return prefs.getString("MY_ID", "");
    }

    public static void setUserPass(Context context, String input) {
        SharedPreferences prefs = context.getSharedPreferences(MY_ACCOUNT, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("MY_PASS", input);
        editor.commit();
    }

    public static String getUserPass(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(MY_ACCOUNT, Context.MODE_PRIVATE);
        return prefs.getString("MY_PASS", "");
    }

    public static void clearUser(Context context) {
        SharedPreferences prefs  = context.getSharedPreferences(MY_ACCOUNT, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.clear();
        editor.commit();
    }
}
