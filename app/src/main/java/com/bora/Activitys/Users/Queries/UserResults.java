package com.bora.Activitys.Users.Queries;

import android.content.Intent;
import android.os.Bundle;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.bora.Functions.DAO.User.Queries.QueryDAO;
import com.bora.Functions.DAO.User.Queries.View.AdapterViewUsuario;
import com.bora.Activitys.Main.MainActivity;
import com.bora.databinding.ActivityResultaUsuarioBinding;

public class UserResults extends AppCompatActivity {
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
        QueryDAO resultaUsuarioDAO = new QueryDAO(this);
        resultaUsuarioDAO.readData(usuarioDTO -> {
            binding.tableDeResultados.setLayoutManager(new LinearLayoutManager(this));//, LinearLayoutManager.VERTICAL, false//https://www.youtube.com/watch?v=Zj9ZE6_HtEo
            binding.tableDeResultados.setAdapter(new AdapterViewUsuario(getApplicationContext(), usuarioDTO));
        });
    }

}