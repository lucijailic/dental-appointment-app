package ba.sum.fsre.dentalappointemntapp.data.local;

import android.content.Context;
import android.content.SharedPreferences;

public class TokenStorage {
    private static final String PREFS = "auth_prefs";
    private static final String KEY_ACCESS_TOKEN = "access_token";
    private static final String KEY_USER_ID = "user_id";
    private static final String KEY_ROLE = "role";

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

    public void saveUserId(String userId) {
        sp.edit().putString(KEY_USER_ID, userId).apply();
    }

    public String getUserId() {
        return sp.getString(KEY_USER_ID, null);
    }

    public void saveRole(String role) {
        sp.edit().putString(KEY_ROLE, role).apply();
    }

    public String getRole() {
        return sp.getString(KEY_ROLE, null);
    }

    public void clear() {
        sp.edit()
                .remove(KEY_ACCESS_TOKEN)
                .remove(KEY_USER_ID)
                .remove(KEY_ROLE)
                .apply();
    }
}
