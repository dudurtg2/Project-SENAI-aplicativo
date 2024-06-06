package com.bora.Functions.DAO.User.Queries.View;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bora.Functions.DTO.Users.UserDTO;
import com.bora.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AdapterViewUsuario extends RecyclerView.Adapter<ViewUsuario> {
    private FirebaseAuth mAuth;
    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private FirebaseFirestore db;
    Context context;
    List<UserDTO> usuarioDTO;

    public AdapterViewUsuario(Context context, List<UserDTO> usuarioDTO) {
        this.context = context;
        this.usuarioDTO = usuarioDTO;
    }

    @NonNull
    @Override
    public ViewUsuario onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewUsuario(LayoutInflater.from(context).inflate(R.layout.itensviews, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewUsuario holder, int position) {
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        holder.TextNome.setText(usuarioDTO.get(position).getName());
        holder.TextEndereco.setText(usuarioDTO.get(position).getAddress());
        holder.TextTelefone.setText(usuarioDTO.get(position).getNumber());
        holder.imageViewEditar.setImageResource(com.google.android.gms.base.R.drawable.common_google_signin_btn_text_light_normal_background);

        if (currentUser != null) {
            StorageReference gsReference = storage.getReferenceFromUrl("gs://dbdavalonstudios.appspot.com/" + usuarioDTO.get(position).getId() + "/profile.png");
            gsReference.getDownloadUrl().addOnSuccessListener(uri -> {
                Picasso.get().load(uri).into(holder.imageViewUsuario);
            }).addOnFailureListener(exception -> {
                holder.imageViewUsuario.setImageResource(R.drawable.a);
            });
        }


        holder.itemView.setOnClickListener(view -> {
            Toast.makeText(context, "Clicou em " + usuarioDTO.get(position).getName(), Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public int getItemCount() {
        return usuarioDTO.size();
    }
}
