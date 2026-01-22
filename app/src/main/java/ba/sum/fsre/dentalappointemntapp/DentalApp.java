package ba.sum.fsre.dentalappointemntapp;

import android.app.Application;
import ba.sum.fsre.dentalappointemntapp.data.local.ThemeManager;

public class DentalApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        try {
            ThemeManager.applySavedTheme(this);
        } catch (Exception ex) {
            android.util.Log.e("DentalApp", "Failed to apply saved theme", ex);
        }
    }
}
