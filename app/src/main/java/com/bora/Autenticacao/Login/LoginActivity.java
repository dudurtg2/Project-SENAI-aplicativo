package com.bora.Autenticacao.Login;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.bora.Autenticacao.Cadastro.CadastroActivity;
import com.bora.R;
import com.bora.databinding.ActivityLoginBinding;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding; //ELIMINA A NECESSIDADE DO USO DO FINDBYID E LISTA TUDO QUE TEM NA ATIVIDADE/TELA

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.ClickCadastro.setOnClickListener(v -> {
            startActivity(new Intent(this, CadastroActivity.class));
        });

    }
}