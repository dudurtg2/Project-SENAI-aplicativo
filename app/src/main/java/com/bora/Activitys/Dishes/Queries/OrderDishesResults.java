package com.bora.Activitys.Dishes.Queries;

import android.content.Intent;
import android.os.Bundle;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.bora.Activitys.Main.MainActivity;
import com.bora.Functions.DAO.Order.Queries.QueryDAO;
import com.bora.Functions.DAO.Order.Queries.View.AdapterViewOrder;
import com.bora.databinding.ActivityDishesOrderBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class OrderDishesResults extends AppCompatActivity {
    private ActivityDishesOrderBinding binding;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        binding = ActivityDishesOrderBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.OrderDishesButton.setOnClickListener(v -> {
            startActivity(new Intent(this, MainActivity.class));
        });
        QueryItems();
    }

    public void QueryItems() {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String uid = currentUser.getUid();
            db.collection("usuarios").document(uid).get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    boolean admin = false;
                    if (document != null && document.exists()) {
                        admin = document.getBoolean("admin");
                    }
                    QueryDAO orderDishesDAO = new QueryDAO(this, mAuth, admin);
                    orderDishesDAO.readData(orderDishesList -> {
                        binding.OrderTableResultDishesShow.setLayoutManager(new LinearLayoutManager(this));
                        binding.OrderTableResultDishesShow.setAdapter(new AdapterViewOrder(getApplicationContext(), orderDishesList));
                    });
                }
            });
        }
    }
}

