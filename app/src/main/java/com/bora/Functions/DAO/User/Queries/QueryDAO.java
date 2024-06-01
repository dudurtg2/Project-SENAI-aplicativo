package com.bora.Functions.DAO.User.Queries;

import android.content.Context;
import android.widget.Toast;

import com.bora.Functions.DTO.Users.UserDTO;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class QueryDAO {
    private Context context;
    private FirebaseFirestore db;
    private List<UserDTO> userList;

    public QueryDAO(Context context) {
        this.context = context;
        this.db = FirebaseFirestore.getInstance();
        this.userList = new ArrayList<>();
    }

    public interface FirestoreCallback {
        void onCallback(List<UserDTO> userList);
    }
    public void readData(final FirestoreCallback firestoreCallback) {
        db.collection("usuarios")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        userList.clear();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            UserDTO usuario = new UserDTO(
                                    document.getString("nome"),
                                    document.getString("endereco"),
                                    document.getString("telefone"),
                                    document.getString("uid")
                            );
                            userList.add(usuario);
                        }
                        firestoreCallback.onCallback(userList);
                    } else {
                        Toast.makeText(context, "Erro ao obter documentos: " + task.getException(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
