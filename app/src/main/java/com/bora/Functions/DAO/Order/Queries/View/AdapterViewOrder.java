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
    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private FirebaseFirestore db;
    Context context;
    List<DishesDTO> dishesDTO;
    StorageReference gsReference;

    public AdapterViewOrder(Context context, List<DishesDTO> dishesDTO) {
        this.context = context;
        this.dishesDTO = dishesDTO;
    }

    @NonNull
    @Override
    public ViewOrder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewOrder(LayoutInflater.from(context).inflate(R.layout.itensviews, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewOrder holder, int position) {
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        DishesDTO currentDish = dishesDTO.get(position);

        holder.DishesClientNameOrderShow.setText("Cliente: " + currentDish.getNome_cliente());
        holder.DishesNameOrderShow.setText(currentDish.getNome_prato());
        holder.DishesPriceOrderShow.setText("Preço: R$" + currentDish.getPreco());
        holder.DishesStatusOrderShow.setText(currentDish.getStatus());

        if (currentUser != null) {
            String uid = currentUser.getUid();
            db.collection("usuarios").document(uid).get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document != null && document.exists()) {
                        Boolean isAdmin = document.getBoolean("admin");
                        if (isAdmin != null && isAdmin) {
                            holder.CancelOrder.setVisibility(View.VISIBLE);
                            holder.ConfirmOrder.setVisibility(View.VISIBLE);
                            setupAdminButtons(holder, currentDish);
                        } else {
                            holder.CancelOrder.setVisibility(View.GONE);
                            holder.ConfirmOrder.setVisibility(View.GONE);
                        }
                    } else {
                        holder.CancelOrder.setVisibility(View.GONE);
                        holder.ConfirmOrder.setVisibility(View.GONE);
                    }
                } else {
                    holder.CancelOrder.setVisibility(View.GONE);
                    holder.ConfirmOrder.setVisibility(View.GONE);
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

    private void setupAdminButtons(ViewOrder holder, DishesDTO currentDish) {
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        HashMap<String, Object> query = new HashMap<>();

        holder.CancelOrder.setOnClickListener(view -> {
            query.put("status", "cancelado");
            firestore.collection("pedidos").document(currentDish.getUid_prato()).update(query)
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(context, "Pedido cancelado.", Toast.LENGTH_SHORT).show();
                        currentDish.setStatus("cancelado");
                        notifyItemChanged(holder.getAdapterPosition());
                    })
                    .addOnFailureListener(e -> Toast.makeText(context, "Erro ao cancelar pedido.", Toast.LENGTH_SHORT).show());
        });

        holder.ConfirmOrder.setOnClickListener(view -> {
            query.put("status", "finalizado");
            firestore.collection("pedidos").document(currentDish.getUid_prato()).update(query)
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(context, "Pedido finalizado.", Toast.LENGTH_SHORT).show();
                        currentDish.setStatus("finalizado");
                        notifyItemChanged(holder.getAdapterPosition());
                    })
                    .addOnFailureListener(e -> Toast.makeText(context, "Erro ao finalizar pedido.", Toast.LENGTH_SHORT).show());
        });
    }

    private void loadDishImage(DishesDTO currentDish, ViewOrder holder) {
        gsReference = storage.getReferenceFromUrl("gs://dbdavalonstudios.appspot.com/disher/" + currentDish.getUid_prato() + "/dishesDown.png");
        gsReference.getDownloadUrl().addOnSuccessListener(uri -> {
            Picasso.get().load(uri).into(holder.DishesImageOrderShow);
        }).addOnFailureListener(e -> {
            gsReference = storage.getReferenceFromUrl("gs://dbdavalonstudios.appspot.com/disher/" + currentDish.getUid_prato() + "/dishesTop.png");
            gsReference.getDownloadUrl().addOnSuccessListener(uri -> {
                Picasso.get().load(uri).into(holder.DishesImageOrderShow);
            }).addOnFailureListener(e1 -> {
                holder.DishesImageOrderShow.setImageResource(R.drawable.baseimageforuser);
            });
        });
    }

    @Override
    public int getItemCount() {
        return dishesDTO.size();
    }
}
