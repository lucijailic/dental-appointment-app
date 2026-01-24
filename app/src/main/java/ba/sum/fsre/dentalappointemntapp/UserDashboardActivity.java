package ba.sum.fsre.dentalappointemntapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import ba.sum.fsre.dentalappointemntapp.data.local.TokenStorage;
import ba.sum.fsre.dentalappointemntapp.data.model.Profile;
import ba.sum.fsre.dentalappointemntapp.data.repository.ProfilesRepository;
import ba.sum.fsre.dentalappointemntapp.data.repository.RepositoryCallback;
import ba.sum.fsre.dentalappointemntapp.data.ui.ServicesActivity;
import ba.sum.fsre.dentalappointemntapp.data.util.DeleteAccountHelper;


public class UserDashboardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_dashboard);

        TokenStorage storage = new TokenStorage(this);
        TextView tvWelcome = findViewById(R.id.tv_welcome_user);

        ProfilesRepository profilesRepo = new ProfilesRepository(this);
        profilesRepo.getProfileByUserId(storage.getUserId(), new RepositoryCallback<Profile>() {
            @Override
            public void onSuccess(Profile profile) {
                if (profile != null && profile.firstName != null) {
                    tvWelcome.setText("Zdravo, " + profile.firstName + "!");
                }
            }
            @Override
            public void onError(String error) {}
        });

        findViewById(R.id.btn_services_user).setOnClickListener(v ->
                startActivity(new Intent(this, ServicesActivity.class)));

        findViewById(R.id.btn_my_appointments).setOnClickListener(v ->
                startActivity(new Intent(this, MyAppointmentsActivity.class)));


        findViewById(R.id.btn_logout_user).setOnClickListener(v -> {
            storage.clear();
            Intent i = new Intent(this, PublicDashboardActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
            finish();
            Toast.makeText(this, "Odjavljeni ste", Toast.LENGTH_SHORT).show();
        });

        findViewById(R.id.btn_delete_account_user)
                .setOnClickListener(v ->
                        DeleteAccountHelper.confirmAndDelete(this)
                );

    }
}
