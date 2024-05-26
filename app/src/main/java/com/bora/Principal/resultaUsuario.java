package com.bora.Principal;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bora.Autenticacao.Cadastro.CadastroActivity;
import com.bora.Funcoes.DAO.Usuario.ResultaUsuarioDAO;
import com.bora.Funcoes.DAO.Usuario.View.AdapterViewUsuario;
import com.bora.Funcoes.DTO.Usuario.UsuarioDTO;
import com.bora.R;
import com.bora.databinding.ActivityLoginBinding;
import com.bora.databinding.ActivityResultaUsuarioBinding;

import java.util.ArrayList;
import java.util.List;

public class resultaUsuario extends AppCompatActivity {
    private ActivityResultaUsuarioBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityResultaUsuarioBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.imageButtonVoltar.setOnClickListener(v -> {
            startActivity(new Intent(this, MainActivity.class));
        });
        ConsultaItens();

    }

    public void ConsultaItens() {
        ResultaUsuarioDAO resultaUsuarioDAO = new ResultaUsuarioDAO(this);
        resultaUsuarioDAO.readData(usuarioDTO -> {
            binding.TabelaDeResultados.setLayoutManager(new LinearLayoutManager(this));
            binding.TabelaDeResultados.setAdapter(new AdapterViewUsuario(getApplicationContext(), usuarioDTO));
        });
    }

}