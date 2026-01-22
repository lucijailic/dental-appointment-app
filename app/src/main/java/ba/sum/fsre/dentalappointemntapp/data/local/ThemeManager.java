package ba.sum.fsre.dentalappointemntapp.data.local;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.appcompat.app.AppCompatDelegate;

public class ThemeManager {
    private static final String PREFS = "settings_prefs";
    private static final String KEY_DARK_MODE = "dark_mode";

    public static boolean isDarkModeEnabled(Context context) {
        SharedPreferences sp = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
        return sp.getBoolean(KEY_DARK_MODE, false);
    }

    public static void setDarkMode(Context context, boolean enabled) {
        SharedPreferences sp = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
        sp.edit().putBoolean(KEY_DARK_MODE, enabled).apply();
        applyTheme(enabled);
    }

    public static void applyTheme(boolean darkMode) {
        if (darkMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
    }

    public static void applySavedTheme(Context context) {
        applyTheme(isDarkModeEnabled(context));
    }
}
