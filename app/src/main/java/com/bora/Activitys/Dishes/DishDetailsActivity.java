package com.bora.Activitys.Dishes;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import com.bora.databinding.ActivityDishesDetailsBinding;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class DishDetailsActivity extends AppCompatActivity {
    private ActivityDishesDetailsBinding binding;
    private FirebaseFirestore firestore;
    private DocumentReference docRef;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private FirebaseStorage storage;
    private DatabaseReference mDatabase;
    private String uniqueId;
    private String prise;
    private StorageReference gsReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDishesDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        mDatabase = FirebaseDatabase.getInstance().getReference();
        uniqueId = mDatabase.push().getKey();

        Intent intent = getIntent();
        if (intent != null) {
            String uid = intent.getStringExtra("uid");
            String table = intent.getStringExtra("table");

            if (uid != null && table != null) {
                mAuth = FirebaseAuth.getInstance();
                currentUser = mAuth.getCurrentUser();
                firestore = FirebaseFirestore.getInstance();
                storage = FirebaseStorage.getInstance();

                selectDish(uid, table);
            } else {
                finish();
            }
            binding.detailsButtonResgister.setOnClickListener(v -> {
                requestMakeDish(uid, table);
            });
        }
    }

    private void requestMakeDish(String uid, String table) {
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        HashMap<String, Object> query = new HashMap<>();
        String currentUserUid = mAuth.getCurrentUser().getUid();

        Task<DocumentSnapshot> dishTask = firestore.collection(table).document(uid).get();
        Task<DocumentSnapshot> userTask = firestore.collection("usuarios").document(currentUserUid).get();
        Tasks.whenAllSuccess(dishTask, userTask).addOnSuccessListener(results -> {
            DocumentSnapshot dishDoc = (DocumentSnapshot) results.get(0);
            DocumentSnapshot userDoc = (DocumentSnapshot) results.get(1);
            if (dishDoc.exists() && dishDoc.get("nome") != null) {
                query.put("nome_prato", dishDoc.getString("nome"));
            }
            if (userDoc.exists() && userDoc.get("nome") != null) {
                query.put("nome_cliente", userDoc.getString("nome"));
            }
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd - HH:mm", Locale.getDefault());
            String currentDateAndTime = sdf.format(new Date());
            query.put("data_pedido", currentDateAndTime);
            query.put("uid_cliente", currentUserUid);
            query.put("uid_prato", uid);

            captureDishPrice(uid, query);
        }).addOnFailureListener(e -> Toast.makeText(this, "Erro ao buscar documentos: " + e.getMessage(), Toast.LENGTH_SHORT).show());
        finish();
    }
    private void captureDishPrice(String uid, HashMap<String, Object> query) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("dishesDown").document(uid).get().addOnSuccessListener(downSnap -> {
            if (downSnap.exists()) {
                query.put("preco", downSnap.getString("preco"));
                query.put("status", "pendente");
                firestore.collection("pedidos").document(uniqueId).set(query)
                        .addOnSuccessListener(aVoid -> Toast.makeText(this, "Pedido realizado com sucesso", Toast.LENGTH_SHORT).show())
                        .addOnFailureListener(e -> Toast.makeText(this, "Falha ao realizar pedido: " + e.getMessage(), Toast.LENGTH_SHORT).show());
            } else {
                db.collection("dishesTop").document(uid).get().addOnSuccessListener(topSnap -> {
                    if (topSnap.exists()) {
                        query.put("preco", topSnap.getString("preco"));
                        query.put("status", "pendente");
                        firestore.collection("pedidos").document(uniqueId).set(query)
                                .addOnSuccessListener(aVoid -> Toast.makeText(this, "Pedido realizado com sucesso", Toast.LENGTH_SHORT).show())
                                .addOnFailureListener(e -> Toast.makeText(this, "Falha ao realizar pedido: " + e.getMessage(), Toast.LENGTH_SHORT).show());
                    } else {
                        db.collection("dishesPrincipal").document(uid).get().addOnSuccessListener(PrincipalSnap -> {
                            if (PrincipalSnap.exists()) {
                                query.put("preco", PrincipalSnap.getString("preco"));
                                query.put("status", "pendente");
                                firestore.collection("pedidos").document(uniqueId).set(query)
                                        .addOnSuccessListener(aVoid -> Toast.makeText(this, "Pedido realizado com sucesso", Toast.LENGTH_SHORT).show())
                                        .addOnFailureListener(e -> Toast.makeText(this, "Falha ao realizar pedido: " + e.getMessage(), Toast.LENGTH_SHORT).show());
                            } else {
                                Toast.makeText(this, "Preço do prato não encontrado", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
            }
        }).addOnFailureListener(e -> Toast.makeText(this, "Erro ao buscar preço do prato: " + e.getMessage(), Toast.LENGTH_SHORT).show())
                .addOnCompleteListener(task ->  finish());
    }

    private void selectDish(String uid, String table) {
        docRef = firestore.collection(table).document(uid);
        docRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    String nome = document.getString("nome");
                    String descricao = document.getString("descrisao");
                    String prise = document.getString("preco");

                    binding.detailsNameShow.setText(nome != null ? nome : "");
                    binding.detailsDescriptionShow.setText(descricao != null ? descricao : "");
                    binding.detailsButtonResgister.setText(prise != null ? "Adicionar  R$ " + prise : "");

                    gsReference = storage.getReferenceFromUrl("gs://dbdavalonstudios.appspot.com/disher/" + uid + "/dishesDown.png");
                    gsReference.getDownloadUrl().addOnSuccessListener(uri -> {
                        Picasso.get().load(uri).into(binding.detailsImageShow);
                    }).addOnFailureListener(e -> {
                        gsReference = storage.getReferenceFromUrl("gs://dbdavalonstudios.appspot.com/disher/" + uid + "/dishesTop.png");
                        gsReference.getDownloadUrl().addOnSuccessListener(uri -> {
                            Picasso.get().load(uri).into(binding.detailsImageShow);
                        }).addOnFailureListener(e2 -> {
                            gsReference = storage.getReferenceFromUrl("gs://dbdavalonstudios.appspot.com/disher/" + uid + "/dishesPrincipal.png");
                            gsReference.getDownloadUrl().addOnSuccessListener(uri -> {
                                Picasso.get().load(uri).into(binding.detailsImageShow);
                            }).addOnFailureListener(e3 -> {
                                Toast.makeText(this, "Imagem do prato não encontrada", Toast.LENGTH_SHORT).show();
                            });
                        });
                    });
                } else {
                    Toast.makeText(this, "Documento do prato não encontrado", Toast.LENGTH_SHORT).show();
                    finish();
                }
            } else {
                Toast.makeText(this, "Erro ao buscar dados do prato: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

}