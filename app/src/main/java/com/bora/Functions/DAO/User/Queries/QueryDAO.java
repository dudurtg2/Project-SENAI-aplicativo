package com.bora.Functions.DAO.User.Queries;

import android.content.Context;
import android.widget.Toast;

import com.bora.Functions.DTO.Users.UserDTO;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class QueryDAO {
    private Context Contexto;
    private FirebaseFirestore db;
    private List<UserDTO> usuarioList;

    public QueryDAO(Context Contexto) {
        this.Contexto = Contexto;
        this.db = FirebaseFirestore.getInstance();
        this.usuarioList = new ArrayList<>();
    }

    public interface FirestoreCallback {
        void onCallback(List<UserDTO> usuarioList);
    }
    public void readData(final FirestoreCallback firestoreCallback) {
        db.collection("usuarios")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        usuarioList.clear();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            UserDTO usuario = new UserDTO(
                                    document.getString("nome"),
                                    document.getString("endereco"),
                                    document.getString("telefone"),
                                    document.getString("uid")
                            );
                            usuarioList.add(usuario);
                        }
                        firestoreCallback.onCallback(usuarioList);
                    } else {
                        Toast.makeText(Contexto, "Erro ao obter documentos: " + task.getException(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
