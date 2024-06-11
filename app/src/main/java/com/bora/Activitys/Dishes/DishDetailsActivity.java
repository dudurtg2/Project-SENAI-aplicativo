package com.bora.Activitys.Dishes;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.bora.databinding.ActivityDetailsDishesBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class DishDetailsActivity extends AppCompatActivity {
    private ActivityDetailsDishesBinding binding;
    private FirebaseFirestore firestore;
    private DocumentReference docRef;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private FirebaseStorage storage;
    private StorageReference gsReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetailsDishesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

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
        }
    }

    public void selectDish(String uid, String table) {
        docRef = firestore.collection(table).document(uid);
        docRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    if (document.get("nome") != null) {
                        binding.detailsNameShow.setText(document.getString("nome"));
                    }
                    if (document.get("descrisao") != null) {
                        binding.detailsDescriptionShow.setText(document.getString("descrisao"));
                    }
                    gsReference = storage.getReferenceFromUrl("gs://dbdavalonstudios.appspot.com/disher/" + uid + "/dishesDown.png");
                    gsReference.getDownloadUrl().addOnSuccessListener(uri -> { Picasso.get().load(uri).into(binding.detailsImageShow); }).addOnFailureListener(e -> {
                        gsReference = storage.getReferenceFromUrl("gs://dbdavalonstudios.appspot.com/disher/" + uid + "/dishesTop.png");
                        gsReference.getDownloadUrl().addOnSuccessListener(uri -> { Picasso.get().load(uri).into(binding.detailsImageShow); });
                    });
                } else {
                }
            } else {
            }
        });
    }
}