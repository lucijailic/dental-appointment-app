package ba.sum.fsre.dentalappointemntapp.data.local;

import android.content.Context;
import android.content.SharedPreferences;

public class TokenStorage {
    private static final String PREFS = "auth_prefs";
    private static final String KEY_ACCESS_TOKEN = "access_token";

    private static final String KEY_USER_ROLE = "user_role";

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

    public void saveRole(String role) {
        sp.edit().putString(KEY_USER_ROLE, role).apply();
    }

    public String getRole() {
        return sp.getString(KEY_USER_ROLE, null);
    }

    public void saveUserId(String userId) {
        sp.edit().putString("user_id", userId).apply();
    }

    public String getUserId() {
        return sp.getString("user_id", null);
    }

    public void clear() {
        sp.edit().clear().apply();
    }
}
