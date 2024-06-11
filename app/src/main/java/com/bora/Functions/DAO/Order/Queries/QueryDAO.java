package com.bora.Functions.DAO.Order.Queries;

import android.content.Context;
import android.widget.Toast;

import com.bora.Functions.DTO.Dishes.DishesDTO;
import com.bora.Functions.DTO.Users.UserDTO;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.util.ArrayList;
import java.util.List;

public class QueryDAO {
    private Context context;
    private FirebaseFirestore db;
    private List<DishesDTO> dishesList;

    public QueryDAO(Context context) {
        this.context = context;
        this.db = FirebaseFirestore.getInstance();
        this.dishesList = new ArrayList<>();
    }

    public interface FirestoreCallback { void onCallback(List<DishesDTO> dishesList); }
    public void readData(final FirestoreCallback firestoreCallback) {
        db.collection("pedidos").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                dishesList.clear();
                for (QueryDocumentSnapshot document : task.getResult()) {
                    DishesDTO usuario = new DishesDTO(document.getString("nome_cliente"), document.getString("nome_prato"), document.getString("uid_cliente"), document.getString("uid_prato"), document.getString("data_pedido"));
                    dishesList.add(usuario);
                }
                firestoreCallback.onCallback(dishesList);
            } else {
                Toast.makeText(context, "Erro ao obter documentos: " + task.getException(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
