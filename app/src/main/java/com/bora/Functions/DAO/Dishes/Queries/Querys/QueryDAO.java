package com.bora.Functions.DAO.Dishes.Queries.Querys;

import android.content.Context;
import android.widget.Toast;

import com.bora.Functions.DTO.Dishes.DishesDTO;
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

    public interface FirestoreCallback {
        void onCallback(List<DishesDTO> dishesList);
    }

    public void readData(final FirestoreCallback firestoreCallback) {
        db.collection("dishesDown")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        dishesList.clear();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            DishesDTO dishesDTO = new DishesDTO(
                                    document.getString("nome"),
                                    document.getString("uid")
                            );
                            dishesList.add(dishesDTO);
                        }
                        firestoreCallback.onCallback(dishesList);
                    } else {
                        Toast.makeText(context, "Erro ao obter documentos: " + task.getException(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
