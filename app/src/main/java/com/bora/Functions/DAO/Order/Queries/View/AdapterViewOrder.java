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
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;
import java.util.List;

public
class AdapterViewOrder extends RecyclerView.Adapter<ViewOrder> {
    private
    FirebaseAuth mAuth;
    private
    FirebaseStorage storage = FirebaseStorage.getInstance();
    private
    FirebaseFirestore db;
    Context context;
    List<DishesDTO> dishesDTO;
    StorageReference gsReference;

    public
    AdapterViewOrder(Context context, List<DishesDTO> dishesDTO) {
        this.context = context;
        this.dishesDTO = dishesDTO;
    }

    @NonNull @Override public ViewOrder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewOrder(LayoutInflater.from(context).inflate(R.layout.itensviews, parent, false));
    }

    @Override public void onBindViewHolder(@NonNull ViewOrder holder, int position) {
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        holder.DishesClientNameOrderShow.setText("Cliente: " + dishesDTO.get(position).getNome_cliente());
        holder.DishesNameOrderShow.setText(dishesDTO.get(position).getNome_prato());
        holder.DishesPriceOrderShow.setText("Preço: R$" + dishesDTO.get(position).getPreco());
        holder.DishesStatusOrderShow.setText(dishesDTO.get(position).getStatus());
        if (currentUser != null) {
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("usuarios").document(mAuth.getCurrentUser().getUid()).addSnapshotListener((value, error) -> {
                holder.ConfirmOrder.setVisibility(View.VISIBLE);
                holder.CancelOrder.setVisibility(View.VISIBLE);
            });
        }

        if (currentUser != null) {
            gsReference = storage.getReferenceFromUrl("gs://dbdavalonstudios.appspot.com/disher/" + dishesDTO.get(position).getUid_prato() + "/dishesDown.png");
            gsReference.getDownloadUrl().addOnSuccessListener(uri -> {
                Picasso.get().load(uri).into(holder.DishesImageOrderShow);
            }).addOnFailureListener(e -> {
                gsReference = storage.getReferenceFromUrl("gs://dbdavalonstudios.appspot.com/disher/" + dishesDTO.get(position).getUid_prato() + "/dishesTop.png");
                gsReference.getDownloadUrl().addOnSuccessListener(uri -> {
                    Picasso.get().load(uri).into(holder.DishesImageOrderShow);
                }).addOnFailureListener(e1 -> holder.DishesImageOrderShow.setImageResource(R.drawable.baseimageforuser));
            });
        }
        holder.itemView.setOnClickListener(view -> {
            Toast.makeText(context, "Pedido realizado as " + dishesDTO.get(position).getData() , Toast.LENGTH_SHORT).show();
        });

    }

    @Override public int getItemCount() {
        return dishesDTO.size();
    }
}
