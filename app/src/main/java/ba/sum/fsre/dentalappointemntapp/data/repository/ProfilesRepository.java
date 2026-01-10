package ba.sum.fsre.dentalappointemntapp.data.repository;

import android.content.Context;

import java.util.List;

import ba.sum.fsre.dentalappointemntapp.BuildConfig;
import ba.sum.fsre.dentalappointemntapp.data.model.Profile;
import ba.sum.fsre.dentalappointemntapp.data.network.ApiClient;
import ba.sum.fsre.dentalappointemntapp.data.network.api.ProfilesApi;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfilesRepository {

    private final ProfilesApi api;

    public ProfilesRepository(Context context) {
        api = ApiClient.get(context).create(ProfilesApi.class);
    }

    public void getProfileByUserId(String userId, RepositoryCallback<Profile> callback) {
        api.getProfileById(BuildConfig.SUPABASE_ANON_KEY, "eq." + userId, "*")
                .enqueue(new Callback<List<Profile>>() {
                    @Override
                    public void onResponse(Call<List<Profile>> call, Response<List<Profile>> response) {
                        if (response.isSuccessful() && response.body() != null && !response.body().isEmpty()) {
                            callback.onSuccess(response.body().get(0));
                        } else {
                            callback.onError("Profil nije pronađen.");
                        }
                    }

                    @Override
                    public void onFailure(Call<List<Profile>> call, Throwable t) {
                        callback.onError(t.getMessage());
                    }
                });
    }
}
