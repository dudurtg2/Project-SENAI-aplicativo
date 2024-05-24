package com.bora.Autenticacao.Cadastro;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import com.bora.Autenticacao.Login.LoginActivity;
import com.bora.Autenticacao.RecuperSenhaActivity;
import com.bora.R;
import com.bora.databinding.ActivityCadastroBinding;
import com.google.firebase.auth.FirebaseAuth;

public class CadastroActivity extends AppCompatActivity {
    private FirebaseAuth auth;
    private ActivityCadastroBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_cadastro);
        binding = ActivityCadastroBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.ClickLogin.setOnClickListener(v -> {
            startActivity(new Intent(this, LoginActivity.class));
        });
        auth = FirebaseAuth.getInstance();
        binding.btnCadastrar.setOnClickListener(v -> {validarDados();});
        binding.ClickRecuperacao.setOnClickListener(v -> { startActivity(new Intent(this, RecuperSenhaActivity.class)); });
    }

    private void validarDados(){
        String email = binding.EditEmail.getText().toString().trim();
        String senha = binding.EditSenha.getText().toString().trim();

        if(!email.isEmpty()){
            if(!senha.isEmpty()){
                FireBaseCadastroConta(email, senha);
                binding.progressBar.setVisibility(View.VISIBLE);
            } else {
                Toast.makeText(this, "Preencha a senha", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Preencha todos os campos", Toast.LENGTH_SHORT).show();
        }
    }
    private void FireBaseCadastroConta(String email, String senha){
        auth.createUserWithEmailAndPassword(
                email, senha
        ).addOnCompleteListener(this, task -> {
            if(task.isSuccessful()){
                finish();
                Toast.makeText(this, "Cadastro realizado com sucesso", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this, LoginActivity.class));
            } else {
                binding.progressBar.setVisibility(View.GONE);
                Toast.makeText(this, "Erro ao cadastrar", Toast.LENGTH_SHORT).show();
            }
        });
    }

}