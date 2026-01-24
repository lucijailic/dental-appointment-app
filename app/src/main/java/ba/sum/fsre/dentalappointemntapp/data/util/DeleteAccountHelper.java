package ba.sum.fsre.dentalappointemntapp.data.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.widget.Toast;

import java.io.IOException;

import ba.sum.fsre.dentalappointemntapp.BuildConfig;
import ba.sum.fsre.dentalappointemntapp.PublicDashboardActivity;
import ba.sum.fsre.dentalappointemntapp.data.local.TokenStorage;
import ba.sum.fsre.dentalappointemntapp.data.model.AuthResponse;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class DeleteAccountHelper {

    public static void confirmAndDelete(Activity activity) {

        new AlertDialog.Builder(activity)
                .setTitle("Brisanje računa")
                .setMessage("Ovom akcijom trajno brišete račun i sve povezane podatke. Želite li nastaviti?")
                .setPositiveButton("DA", (d, w) -> deleteAccount(activity))
                .setNegativeButton("NE", null)
                .show();
    }

    private static void deleteAccount(Activity activity) {
        TokenStorage storage = new TokenStorage(activity);
        String token = storage.getAccessToken();

        if (token == null) {
            Toast.makeText(activity, "Niste prijavljeni", Toast.LENGTH_SHORT).show();
            return;
        }

        token = token.trim(); // ✅ makni whitespace

        if (token.startsWith("Bearer ")) {
            token = token.substring(7).trim();
        }

        if (token.isEmpty()) {
            Toast.makeText(activity, "Niste prijavljeni", Toast.LENGTH_SHORT).show();
            return;
        }

        String url = BuildConfig.SUPABASE_URL + "/functions/v1/delete-account";

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(url)
                .post(okhttp3.RequestBody.create(new byte[0]))
                .addHeader("Authorization", "Bearer " + token)
                .addHeader("apikey", BuildConfig.SUPABASE_ANON_KEY)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                activity.runOnUiThread(() ->
                        Toast.makeText(activity, "Greška pri brisanju računa: " + e.getMessage(), Toast.LENGTH_LONG).show()
                );
            }

            @Override
            public void onResponse(Call call, Response response) {
                activity.runOnUiThread(() -> {
                    String body = "";
                    try { body = response.body() != null ? response.body().string() : ""; }
                    catch (Exception ignored) {}

                    if (response.isSuccessful()) {
                        storage.clear();
                        Intent i = new Intent(activity, PublicDashboardActivity.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        activity.startActivity(i);
                        activity.finish();
                        Toast.makeText(activity, "Račun je obrisan", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(activity,
                                "Brisanje nije uspjelo (" + response.code() + ") " + body,
                                Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }


    private static void callDeleteFunction(Activity activity, TokenStorage storage, String token) {

        if (token != null && token.startsWith("Bearer ")) token = token.substring(7);

        String url = BuildConfig.SUPABASE_URL + "/functions/v1/delete-account";

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(url)
                .post(okhttp3.RequestBody.create(new byte[0]))
                .addHeader("Authorization", "Bearer " + token)
                .addHeader("apikey", BuildConfig.SUPABASE_ANON_KEY)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                activity.runOnUiThread(() ->
                        Toast.makeText(activity, "Greška pri brisanju računa", Toast.LENGTH_LONG).show()
                );
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String body = response.body() != null ? response.body().string() : "";

                activity.runOnUiThread(() -> {
                    if (response.isSuccessful()) {
                        storage.clear();
                        Intent i = new Intent(activity, PublicDashboardActivity.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        activity.startActivity(i);
                        activity.finish();
                        Toast.makeText(activity, "Račun je obrisan", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(activity,
                                "Brisanje nije uspjelo (" + response.code() + ") " + body,
                                Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }

}
