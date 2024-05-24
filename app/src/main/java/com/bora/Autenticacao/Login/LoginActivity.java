package com.bora.Autenticacao.Login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.bora.Autenticacao.Cadastro.CadastroActivity;
import com.bora.Autenticacao.RecuperSenhaActivity;
import com.bora.Principal.MainActivity;
import com.bora.R;
import com.bora.databinding.ActivityLoginBinding;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth auth;
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
        auth = FirebaseAuth.getInstance();
        binding.btnLogin.setOnClickListener(v -> {validarDados();});
        binding.ClickRecuperacao.setOnClickListener(v -> { startActivity(new Intent(this, RecuperSenhaActivity.class)); });
    }
    private void validarDados(){
        String email = binding.EditEmail.getText().toString().trim();
        String senha = binding.EditSenha.getText().toString().trim();

        if(!email.isEmpty()){
            if(!senha.isEmpty()){
                FireBaseLoginConta(email, senha);
                binding.progressBar.setVisibility(View.VISIBLE);
            } else {
                Toast.makeText(this, "Preencha a senha", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Preencha todos os campos", Toast.LENGTH_SHORT).show();
        }
    }
    private void FireBaseLoginConta(String email, String senha){
        auth.signInWithEmailAndPassword(
                email, senha
        ).addOnCompleteListener(this, task -> {
            if(task.isSuccessful()){
                finish();
                Toast.makeText(this, "Login realizado com sucesso", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this, MainActivity.class));
            } else {
                binding.progressBar.setVisibility(View.GONE);
                Toast.makeText(this, "Email não cadastrado", Toast.LENGTH_SHORT).show();
            }
        });
    }
}