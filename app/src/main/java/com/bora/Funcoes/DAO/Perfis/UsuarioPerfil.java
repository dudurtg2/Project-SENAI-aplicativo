package com.bora.Funcoes.DAO.Perfis;

import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.bora.databinding.ActivityUsuarioPerfilBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.DocumentSnapshot;

public class UsuarioPerfil extends AppCompatActivity {
    private ActivityUsuarioPerfilBinding binding;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityUsuarioPerfilBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {

            String email = currentUser.getEmail();
            binding.email.setText(email);
        }
        ;
        if (currentUser != null) {
            DocumentReference docRef = db.collection("usuarios").document(currentUser.getUid());
            docRef.get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        String nome = document.getString("nome");
                        binding.nome.setText(nome != null ? nome : "Nome não disponível");
                    } else {
                        Toast.makeText(UsuarioPerfil.this, "Documento não encontrado", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(UsuarioPerfil.this, "Erro ao acessar o Firestore", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            binding.nome.setText("Usuário não autenticado");
        }

    }
}