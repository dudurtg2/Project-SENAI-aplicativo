package com.bora.Functions.DAO.Dishes.Updates;

import android.content.Context;
import android.widget.Toast;
import com.bora.Functions.DTO.Dishes.DishesDTO;
import com.google.android.gms.tasks.Task;
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

    public Task<Void> addDishToFirestore(String name, String description, String uid, String preco, String category, String table) {
        DishesDTO dishesDTO = new DishesDTO(name, description, uid, table);

        FirebaseFirestore firestore = FirebaseFirestore.getInstance();

        HashMap<String, Object> query = new HashMap<>();

        query.put("categoria", category);
        query.put("uid", dishesDTO.getUid());
        query.put("nome", dishesDTO.getName());
        query.put("descrisao", dishesDTO.getDescription());
        query.put("preco", preco);

        return firestore.collection(dishesDTO.getTable()).document(uid).set(query).addOnSuccessListener(aVoid -> Toast.makeText(context, "Prato adicionado com sucesso!", Toast.LENGTH_SHORT).show()).addOnFailureListener(e -> Toast.makeText(context, "Falha ao adicionar prato: " + e.getMessage(), Toast.LENGTH_SHORT).show());

    }
}
