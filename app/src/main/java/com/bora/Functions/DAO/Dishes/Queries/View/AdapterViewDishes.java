package com.bora.Functions.DAO.Dishes.Queries.View;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bora.Functions.DAO.User.Queries.View.ViewUsuario;
import com.bora.Functions.DTO.Dishes.DishesDTO;
import com.bora.Functions.DTO.Users.UserDTO;
import com.bora.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AdapterViewDishes extends RecyclerView.Adapter<ViewDishes> {
    private FirebaseAuth mAuth;
    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private FirebaseFirestore db;
    Context context;
    List<DishesDTO> dishesDTO;

    public AdapterViewDishes(Context context, List<DishesDTO> dishesDTO) {
        this.context = context;
        this.dishesDTO = dishesDTO;
    }

    @NonNull
    @Override
    public ViewDishes onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewDishes(LayoutInflater.from(context).inflate(R.layout.itensmainpratos, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewDishes holder, int position) {
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        holder.imageViewDishes.setImageResource(R.drawable.a);
        holder.imageDishesBack.setImageResource(com.google.android.gms.base.R.drawable.common_google_signin_btn_icon_dark_normal_background);
        holder.ButtonEditar.setText(dishesDTO.get(position).getName());
        holder.itemView.setOnClickListener(view -> {
            Toast.makeText(context, "Clicou em " + dishesDTO.get(position).getName(), Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public int getItemCount() {
        return dishesDTO.size();
    }
}
