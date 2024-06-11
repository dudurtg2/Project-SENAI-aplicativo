package com.bora.Activitys.Main;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.bora.Activitys.Dishes.DishesActivity;
import com.bora.Activitys.Users.Profile.ProfileActivity;
import com.bora.Activitys.Users.Queries.UserResults;
import com.bora.Functions.DAO.Dishes.Queries.Querys.Query2DAO;
import com.bora.Functions.DAO.Dishes.Queries.Querys.QueryDAO;
import com.bora.Functions.DAO.Dishes.Queries.View.AdapterViewDishes;
import com.bora.R;
import com.bora.databinding.ActivityMainBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.imageButtonBusca.setOnClickListener(v -> { startActivity(new Intent(this, ProfileActivity.class)); });
        binding.imageButtonUsuario.setOnClickListener(v -> { startActivity(new Intent(this, UserResults.class)); });
        binding.imageButton.setOnClickListener(v -> { startActivity(new Intent(this, DishesActivity.class)); });
        checkAdminStatus();
        QueryItems();
    }
    private void checkAdminStatus() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String uid = currentUser.getUid();
            db.collection("usuarios").document(uid).get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document != null && document.exists()) {
                        Boolean isAdmin = document.getBoolean("admin");
                        if (isAdmin != null && !isAdmin) {
                            binding.imageButton.setVisibility(View.GONE);
                        } else {
                            binding.imageButton.setVisibility(View.VISIBLE);
                        }
                    } else {
                        binding.imageButton.setVisibility(View.GONE);
                    }
                } else {
                    binding.imageButton.setVisibility(View.GONE);
                }
            });
        } else {
            binding.imageButton.setVisibility(View.GONE);
        }
    }

    private void QueryItems() {
        QueryDAO queryDAO = new QueryDAO(this);
        queryDAO.readData(dishesDTO -> {
            binding.MainPrincipalViewPratos.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
            binding.MainPrincipalViewPratos.setAdapter(new AdapterViewDishes(getApplicationContext(), dishesDTO));
        });
        Query2DAO queryDAO2 = new Query2DAO(this);
        queryDAO2.readData(dishesDTO -> {
            binding.MainPrincipalViewPratos2.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
            binding.MainPrincipalViewPratos2.setAdapter(new AdapterViewDishes(getApplicationContext(), dishesDTO));
        });
    }
}
