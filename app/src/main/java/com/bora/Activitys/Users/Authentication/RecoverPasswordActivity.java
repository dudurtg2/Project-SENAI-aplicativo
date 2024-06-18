package com.bora.Activitys.Users.Authentication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.bora.R;
import com.bora.databinding.ActivityAuthenticatorRecoverPasswordBinding;
import com.google.firebase.auth.FirebaseAuth;

public class RecoverPasswordActivity extends AppCompatActivity {
    private FirebaseAuth auth;
    private ActivityAuthenticatorRecoverPasswordBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_authenticator_recover_password);
        binding = ActivityAuthenticatorRecoverPasswordBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        auth = FirebaseAuth.getInstance();
        binding.btnRecuperar.setOnClickListener(v -> { validateData(); });
        binding.ClickLogin.setOnClickListener(v -> {
            startActivity(new Intent(this, LoginActivity.class));
            overridePendingTransition(R.anim.slide_out_right, R.anim.slide_in_left);
        });
    }

    private void validateData() {
        String email = binding.EditEmail.getText().toString().trim();
        if (!email.isEmpty()) {
            FireBaseRecoverAccount(email);
            binding.progressBar.setVisibility(View.VISIBLE);
        } else {
            Toast.makeText(this, "Preencha todos os campos", Toast.LENGTH_SHORT).show();
        }
    }

    private void FireBaseRecoverAccount(String email) {
        auth.sendPasswordResetEmail(email).addOnCompleteListener(this, task -> {
            if (task.isSuccessful()) {
                finish();
                Toast.makeText(this, "Email enviado", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this, LoginActivity.class));
            } else {
                binding.progressBar.setVisibility(View.GONE);
                Toast.makeText(this, "Erro ao enviar email", Toast.LENGTH_SHORT).show();
            }
        });
    }
}