package com.bora.Activitys.Authentication.Register;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import com.bora.Activitys.Authentication.Login.LoginActivity;
import com.bora.Activitys.Authentication.RecoverPasswordActivity;
import com.bora.Functions.DAO.User.Updates.UserDAO;
import com.bora.R;
import com.bora.databinding.ActivityCadastroBinding;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends AppCompatActivity {
    private FirebaseAuth auth;
    private ActivityCadastroBinding binding;
    private UserDAO userDAO;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_cadastro);
        binding = ActivityCadastroBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.ClickLogin.setOnClickListener(v -> {
            startActivity(new Intent(this, LoginActivity.class));
            overridePendingTransition(R.anim.slide_out_right, R.anim.slide_in_left);
        });
        auth = FirebaseAuth.getInstance();
        binding.btnCadastrar.setOnClickListener(v -> {validateData();});
        binding.ClickRecuperacao.setOnClickListener(v -> {
            startActivity(new Intent(this, RecoverPasswordActivity.class));
            overridePendingTransition(R.anim.slide_out_right, R.anim.slide_in_left);
        });
        userDAO = new UserDAO(this);
    }

    private void validateData(){
        String email = binding.EditEmail.getText().toString().trim();
        String senha = binding.EditSenha.getText().toString().trim();

        if(!email.isEmpty()){
            if(!senha.isEmpty()){
                FireBaseRegistrationAccount(email, senha);
                binding.progressBar.setVisibility(View.VISIBLE);
            } else {
                Toast.makeText(this, "Preencha a senha", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Preencha todos os campos", Toast.LENGTH_SHORT).show();
        }
    }
    private void FireBaseRegistrationAccount(String email, String senha){
        auth.createUserWithEmailAndPassword(
                email, senha
        ).addOnCompleteListener(this, task -> {
            if(task.isSuccessful()){
                finish();
                Toast.makeText(this, "Cadastro realizado com sucesso", Toast.LENGTH_SHORT).show();
                userDAO.userDTO("usuarios", binding.EditUsuario.getText().toString().isEmpty() ? "" : binding.EditUsuario.getText().toString(), "Não informado", "Não informado", "Não informado", "Não informado");
                startActivity(new Intent(this, LoginActivity.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            } else {
                binding.progressBar.setVisibility(View.GONE);
                Toast.makeText(this, "Erro ao cadastrar", Toast.LENGTH_SHORT).show();
            }
        });
    }

}