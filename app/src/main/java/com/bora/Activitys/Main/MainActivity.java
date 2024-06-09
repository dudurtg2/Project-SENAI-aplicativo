package com.bora.Activitys.Main;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bora.Activitys.Main.Dishes.DishesActivity;
import com.bora.Activitys.Main.Profile.ProfileActivity;
import com.bora.Activitys.Users.Queries.UserResults;
import com.bora.Functions.DAO.Dishes.Queries.View.AdapterViewDishes;
import com.bora.Functions.DAO.Dishes.Queries.QueryDAO;
import com.bora.Functions.DTO.Dishes.DishesDTO;
import com.bora.R;
import com.bora.databinding.ActivityMainBinding;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize view binding
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Set click listeners
        binding.imageButtonBusca.setOnClickListener(v -> {
            startActivity(new Intent(this, ProfileActivity.class));
        });
        binding.imageButtonUsuario.setOnClickListener(v -> {
            startActivity(new Intent(this, UserResults.class));
        });
        binding.button2.setOnClickListener(v -> {
            startActivity(new Intent(this, DishesActivity.class));
        });
        // Query and display items
        QueryItems();
    }

    private void QueryItems() {
        QueryDAO queryDAO = new QueryDAO(this);
        queryDAO.readData(dishesDTO -> {
            binding.MainPrincipalViewPratos.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
            binding.MainPrincipalViewPratos.setAdapter(new AdapterViewDishes(getApplicationContext(), dishesDTO));
        });
    }
}
