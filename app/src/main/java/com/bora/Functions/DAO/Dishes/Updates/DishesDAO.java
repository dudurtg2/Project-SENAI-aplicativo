package com.bora.Functions.DAO.Dishes.Updates;

import android.content.Context;
import android.widget.Toast;
import com.bora.Functions.DTO.Dishes.DishesDTO;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.HashMap;

public class DishesDAO {
    private Context context;
    private FirebaseAuth mAuth;

    public DishesDAO(Context context) {
        this.context = context;
        this.mAuth = FirebaseAuth.getInstance();
    }

    public void addDishToFirestore(String name, String description, String uid, String table) {
        DishesDTO dishesDTO = new DishesDTO(name, description, uid);

        FirebaseFirestore firestore = FirebaseFirestore.getInstance();

        HashMap<String, Object> query = new HashMap<>();
        query.put("uid", dishesDTO.getUid());
        query.put("nome", dishesDTO.getName());
        query.put("descrisao", dishesDTO.getDescription());

        firestore.collection(table).document(uid).set(query).addOnSuccessListener(aVoid -> Toast.makeText(context, "Prato adicionado com sucesso!", Toast.LENGTH_SHORT).show()).addOnFailureListener(e -> Toast.makeText(context, "Falha ao adicionar prato: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }
}
