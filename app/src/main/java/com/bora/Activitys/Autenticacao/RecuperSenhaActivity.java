package com.bora.Activitys.Autenticacao;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import com.bora.Activitys.Autenticacao.Login.LoginActivity;
import com.bora.R;
import com.bora.databinding.ActivityRecuperSenhaBinding;
import com.google.firebase.auth.FirebaseAuth;

public class RecuperSenhaActivity extends AppCompatActivity {
    private FirebaseAuth auth;
    private ActivityRecuperSenhaBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_recuper_senha);
        binding = ActivityRecuperSenhaBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        auth = FirebaseAuth.getInstance();
        binding.btnRecuperar.setOnClickListener(v -> {validarDados();});
        binding.ClickLogin.setOnClickListener(v -> { startActivity(new Intent(this, LoginActivity.class));});
    }

    private void validarDados(){
        String email = binding.EditEmail.getText().toString().trim();
        if(!email.isEmpty()){
            FireBaseRecuperaConta(email);
            binding.progressBar.setVisibility(View.VISIBLE);
        } else {
            Toast.makeText(this, "Preencha todos os campos", Toast.LENGTH_SHORT).show();
        }
    }

    private void FireBaseRecuperaConta(String email){
        auth.sendPasswordResetEmail(
                email
        ).addOnCompleteListener(this, task -> {
            if(task.isSuccessful()){
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