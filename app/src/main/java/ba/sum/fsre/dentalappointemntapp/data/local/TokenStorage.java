package ba.sum.fsre.dentalappointemntapp.data.local;

import android.content.Context;
import android.content.SharedPreferences;

public class TokenStorage {
    private static final String PREFS = "auth_prefs";
    private static final String KEY_ACCESS_TOKEN = "access_token";

    private final SharedPreferences sp;

    public TokenStorage(Context context) {
        this.sp = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
    }

    public void saveAccessToken(String token) {
        sp.edit().putString(KEY_ACCESS_TOKEN, token).apply();
    }

    public String getAccessToken() {
        return sp.getString(KEY_ACCESS_TOKEN, null);
    }

    public void clear() {
        sp.edit().remove(KEY_ACCESS_TOKEN).apply();
    }
}
