package com.bora.Functions.DAO.Order.Queries;

import android.content.Context;
import android.widget.Toast;
import com.bora.Functions.DTO.Dishes.DishesDTO;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.util.ArrayList;
import java.util.List;

public
class QueryDAO {
    private
    Context context;
    private
    FirebaseFirestore db;
    private
    List<DishesDTO> dishesList;
    private
    FirebaseAuth mAuth;
    private
    boolean admin;

    public
    QueryDAO(Context context, FirebaseAuth mAuth, boolean admin) {
        this.context = context;
        this.db = FirebaseFirestore.getInstance();
        this.dishesList = new ArrayList<>();
        this.mAuth = mAuth;
        this.admin = admin;
    }

    public
    interface FirestoreCallback {
        void onCallback(List<DishesDTO> dishesList);
    }
    public
    void readData(final FirestoreCallback firestoreCallback) {
        db.collection("pedidos").get().addOnCompleteListener(task->{
            if (task.isSuccessful()) {
                dishesList.clear();
                for (QueryDocumentSnapshot document : task.getResult()) {
                    String uidCliente = document.getString("uid_cliente");
                    if (admin || uidCliente.equals(mAuth.getUid())) {
                        DishesDTO usuario = new DishesDTO(document.getString("nome_cliente"), document.getString("nome_prato"), uidCliente, document.getString("uid_prato"), document.getString("data_pedido"), document.getString("preco"), document.getString("status"));
                        dishesList.add(usuario);
                    }
                }
                firestoreCallback.onCallback(dishesList);
            } else {
                Toast.makeText(context, "Erro ao obter documentos: " + task.getException(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
