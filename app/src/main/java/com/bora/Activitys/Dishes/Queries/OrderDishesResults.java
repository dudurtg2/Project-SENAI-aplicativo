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

public class OrderDishesResults extends AppCompatActivity {
    private ActivityDishesOrderBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityDishesOrderBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.OrderDishesButton.setOnClickListener(v -> { startActivity(new Intent(this, MainActivity.class)); });
        QueryItems();
    }

    public void QueryItems() {
        QueryDAO OrderDishesDAO = new QueryDAO(this);
        OrderDishesDAO.readData(orderDishesDAO -> {
            binding.OrderTableResultDishesShow.setLayoutManager(new LinearLayoutManager(this));
            binding.OrderTableResultDishesShow.setAdapter(new AdapterViewOrder(getApplicationContext(), orderDishesDAO));
        });
    }
}