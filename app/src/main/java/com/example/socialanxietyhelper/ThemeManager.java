package com.example.socialanxietyhelper;

import android.content.Context;
import android.content.SharedPreferences;
import androidx.appcompat.app.AppCompatDelegate;

public class ThemeManager {

    private static final String PREFS_NAME    = "CesaretPrefs";
    private static final String KEY_DARK      = "isDarkTheme";

    public static void init(Context ctx) {
        boolean dark = isDark(ctx);
        AppCompatDelegate.setDefaultNightMode(
                dark ? AppCompatDelegate.MODE_NIGHT_YES
                        : AppCompatDelegate.MODE_NIGHT_NO);
    }

    public static boolean isDark(Context ctx) {
        return ctx.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
                .getBoolean(KEY_DARK, false); // default: açık tema
    }

    public static void setDark(Context ctx, boolean dark) {
        ctx.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
                .edit().putBoolean(KEY_DARK, dark).apply();
        AppCompatDelegate.setDefaultNightMode(
                dark ? AppCompatDelegate.MODE_NIGHT_YES
                        : AppCompatDelegate.MODE_NIGHT_NO);
    }

    public static void toggle(Context ctx) {
        setDark(ctx, !isDark(ctx));
    }

    public static void apply(android.app.Activity activity) {}
}