package ba.sum.fsre.dentalappointemntapp;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ManageServicesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_services);

        TextView placeholder = findViewById(R.id.tv_placeholder);
        if (placeholder != null) {
            placeholder.setText("Placeholder tekst");
        }
    }
}
