package ba.sum.fsre.dentalappointemntapp.data.network;

import java.io.IOException;

import ba.sum.fsre.dentalappointemntapp.BuildConfig;
import ba.sum.fsre.dentalappointemntapp.data.local.TokenStorage;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class AuthInterceptor implements Interceptor {

    private final TokenStorage tokenStorage;

    public AuthInterceptor(TokenStorage tokenStorage) {
        this.tokenStorage = tokenStorage;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request original = chain.request();

        Request.Builder builder = original.newBuilder()
                .header("apikey", BuildConfig.SUPABASE_ANON_KEY);

        String token = tokenStorage.getAccessToken();
        if (token != null && !token.isEmpty()) {
            builder.header("Authorization", "Bearer " + token);
        }

        return chain.proceed(builder.build());
    }
}
