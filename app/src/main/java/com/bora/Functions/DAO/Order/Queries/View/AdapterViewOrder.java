package com.bora.Functions.DAO.Order.Queries.View;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bora.Functions.DTO.Dishes.DishesDTO;
import com.bora.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;

public class AdapterViewOrder extends RecyclerView.Adapter<ViewOrder> {
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private Context context;
    private List<DishesDTO> dishesDTO;
    private StorageReference gsReference;

    public AdapterViewOrder(Context context, List<DishesDTO> dishesDTO) {
        this.context = context;
        this.dishesDTO = dishesDTO;
    }

    @NonNull
    @Override
    public ViewOrder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) { return new ViewOrder(LayoutInflater.from(context).inflate(R.layout.itensviews, parent, false)); }

    @Override
    public void onBindViewHolder(@NonNull ViewOrder holder, int position) {
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        DishesDTO currentDish = dishesDTO.get(position);

        holder.DishesClientNameOrderShow.setText("Cliente: " + currentDish.getNome_cliente());
        holder.DishesNameOrderShow.setText(currentDish.getNome_prato());
        holder.DishesPriceOrderShow.setText("Preço: R$" + currentDish.getPrice());
        holder.DishesStatusOrderShow.setText(currentDish.getStatus());

        if (currentUser != null) {
            String uid = currentUser.getUid();
            db.collection("usuarios").document(uid).get().addOnCompleteListener(task -> {
                if (task.isSuccessful() && task.getResult() != null && task.getResult().exists()) {
                    handleUserDocument(task.getResult(), holder, currentDish);
                } else {
                    fetchOrderStatusAndSetVisibility(holder, currentDish);
                }
            });
        } else {
            holder.CancelOrder.setVisibility(View.GONE);
            holder.ConfirmOrder.setVisibility(View.GONE);
        }

        loadDishImage(currentDish, holder);

        holder.itemView.setOnClickListener(view -> {
            Toast.makeText(context, "Pedido realizado às " + currentDish.getData(), Toast.LENGTH_SHORT).show();
        });
    }

    private void handleUserDocument(DocumentSnapshot document, ViewOrder holder, DishesDTO currentDish) {
        Boolean isAdmin = document.getBoolean("admin");
        if (isAdmin != null && isAdmin) {
            holder.CancelOrder.setVisibility(View.GONE);
            holder.ConfirmOrder.setVisibility(View.VISIBLE);
        } else {
            fetchOrderStatusAndSetVisibility(holder, currentDish);
            holder.ConfirmOrder.setVisibility(View.GONE);
        }
        setupAdminButtons(holder, currentDish);
    }

    private void fetchOrderStatusAndSetVisibility(ViewOrder holder, DishesDTO currentDish) {
        db.collection("pedidos").document(currentDish.getUid()).get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult() != null && task.getResult().exists()) {
                String status = task.getResult().getString("status");
                if ("pendente".equals(status)) {
                    holder.CancelOrder.setVisibility(View.VISIBLE);
                } else {
                    holder.CancelOrder.setVisibility(View.GONE);
                }
            } else {
                holder.CancelOrder.setVisibility(View.GONE);
            }
        });
    }

    private void setupAdminButtons(ViewOrder holder, DishesDTO currentDish) {
        holder.CancelOrder.setOnClickListener(view -> { updateOrderStatus(currentDish.getUid(), "cancelado", holder.getAdapterPosition());});
        holder.ConfirmOrder.setOnClickListener(view -> { updateOrderStatus(currentDish.getUid(), "finalizado", holder.getAdapterPosition());});
    }

    private void updateOrderStatus(String orderId, String status, int position) {
        db.collection("pedidos").document(orderId).update("status", status).addOnSuccessListener(aVoid -> {
                    Toast.makeText(context, "Pedido " + status + " com sucesso", Toast.LENGTH_SHORT).show();
                    dishesDTO.get(position).setStatus(status);
                    notifyItemChanged(position);
            }).addOnFailureListener(e -> {
                Toast.makeText(context, "Erro ao atualizar pedido: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            });
    }

    private void loadDishImage(DishesDTO currentDish, ViewOrder holder) {
        String uid = currentDish.getUid_prato();

        gsReference = storage.getReference().child("disher/" + uid + "/dishesDown.png");
        gsReference.getDownloadUrl().addOnSuccessListener(uri -> {
            Picasso.get().load(uri).into(holder.DishesImageOrderShow);
        }).addOnFailureListener(e -> {
            gsReference = storage.getReference().child("disher/" + uid + "/dishesTop.png");
            gsReference.getDownloadUrl().addOnSuccessListener(uri -> {
                Picasso.get().load(uri).into(holder.DishesImageOrderShow);
            }).addOnFailureListener(e2 -> {
                gsReference = storage.getReference().child("disher/" + uid + "/dishesPrincipal.png");
                gsReference.getDownloadUrl().addOnSuccessListener(uri -> {
                    Picasso.get().load(uri).into(holder.DishesImageOrderShow);
                }).addOnFailureListener(e1 -> {
                holder.DishesImageOrderShow.setImageResource(R.drawable.imagemupload);
                });
            });
        });
    }

    @Override
    public int getItemCount() { return dishesDTO.size(); }
}
