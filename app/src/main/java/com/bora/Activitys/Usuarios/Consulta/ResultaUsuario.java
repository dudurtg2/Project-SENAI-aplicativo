package com.bora.Activitys.Usuarios.Consulta;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bora.Funcoes.DAO.Usuario.Consulta.ConsultaDAO;
import com.bora.Funcoes.DAO.Usuario.Consulta.View.AdapterViewUsuario;
import com.bora.Activitys.Principal.MainActivity;
import com.bora.databinding.ActivityResultaUsuarioBinding;

public class ResultaUsuario extends AppCompatActivity {
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
        ConsultaDAO resultaUsuarioDAO = new ConsultaDAO(this);
        resultaUsuarioDAO.readData(usuarioDTO -> {
            binding.TabelaDeResultados.setLayoutManager(new LinearLayoutManager(this));//, LinearLayoutManager.VERTICAL, false//https://www.youtube.com/watch?v=Zj9ZE6_HtEo
            binding.TabelaDeResultados.setAdapter(new AdapterViewUsuario(getApplicationContext(), usuarioDTO));
        });
    }

}