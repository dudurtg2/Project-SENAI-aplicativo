package com.bora.Functions.DAO.Dishes.Queries.Querys;

import android.content.Context;
import android.widget.Toast;

import com.bora.Functions.DTO.Dishes.DishesDTO;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class QueryCategoryDAO {
    private Context context;
    private FirebaseFirestore db;
    private List<DishesDTO> dishesList;

    public QueryCategoryDAO(Context context) {
        this.context = context;
        this.db = FirebaseFirestore.getInstance();
        this.dishesList = new ArrayList<>();
    }

    public interface FirestoreCallback {
        void onCallback(List<DishesDTO> dishesList);
    }

    public void readData(final FirestoreCallback firestoreCallback, String category) {
        dishesList.clear();
        readFromCollection("dishesDown", category, dishesList1 -> {
            dishesList.addAll(dishesList1);
            readFromCollection("dishesTop", category, dishesList2 -> {
                dishesList.addAll(dishesList2);
                readFromCollection("dishesPrincipal", category, dishesList3 -> {
                    dishesList.addAll(dishesList3);
                    firestoreCallback.onCallback(dishesList);
                });
            });
        });
    }

    private void readFromCollection(String collectionName, String category, final FirestoreCallback firestoreCallback) {
        db.collection(collectionName).whereEqualTo("categoria", category).get().addOnCompleteListener(task -> {
            List<DishesDTO> tempList = new ArrayList<>();
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    DishesDTO dishesDTO = new DishesDTO(
                            document.getString("nome"),
                            document.getString("uid"),
                            document.getString("preco")
                    );
                    tempList.add(dishesDTO);
                }
            } else {
                Toast.makeText(context, "Erro ao obter documentos de " + collectionName + ": " + task.getException(), Toast.LENGTH_SHORT).show();
            }
            firestoreCallback.onCallback(tempList);
        }).addOnFailureListener(e -> {
            Toast.makeText(context, "Erro ao obter documentos de " + collectionName + ": " + e, Toast.LENGTH_SHORT).show();
            firestoreCallback.onCallback(new ArrayList<>());
        });
    }
}
