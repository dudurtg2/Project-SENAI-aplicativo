package com.bora.Activitys.Main;
import com.bora.Activitys.Authentication.RecoverPasswordActivity;
import com.bora.Activitys.Main.Profile.ProfileActivity;
import com.bora.Functions.DAO.User.Updates.ImageUploaderDAO;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.EditText;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import com.bora.Functions.DAO.User.Updates.UserDAO;
import com.bora.Functions.Verifiers;
import com.bora.Activitys.Users.Queries.UserResults;
import com.bora.R;
import com.bora.Activitys.Users.Profiles.UserProfile;
import com.bora.databinding.ActivityMainBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    public ActivityMainBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.imageButtonBusca.setOnClickListener(v -> { startActivity(new Intent(this, ProfileActivity.class));} );
        binding.imageButtonUsuario.setOnClickListener(v -> { startActivity(new Intent(this, UserProfile.class));} );

    }
}
