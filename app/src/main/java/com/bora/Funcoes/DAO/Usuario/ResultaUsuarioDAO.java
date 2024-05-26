package com.bora.Funcoes.DAO.Usuario;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.bora.Funcoes.DTO.Usuario.UsuarioDTO;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ResultaUsuarioDAO {
    private Context Contexto;
    private FirebaseFirestore db;
    private List<UsuarioDTO> usuarioList;

    public ResultaUsuarioDAO(Context Contexto) {
        this.Contexto = Contexto;
        this.db = FirebaseFirestore.getInstance();
        this.usuarioList = new ArrayList<>();
    }

    public interface FirestoreCallback {
        void onCallback(List<UsuarioDTO> usuarioList);
    }
    public void readData(final FirestoreCallback firestoreCallback) {
        db.collection("usuarios")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        usuarioList.clear();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            UsuarioDTO usuario = new UsuarioDTO(
                                    document.getString("nome"),
                                    document.getString("endereco"),
                                    document.getString("telefone")
                            );

                            usuarioList.add(usuario);
                        }
                        // Chamar o callback com a lista preenchida
                        firestoreCallback.onCallback(usuarioList);
                    } else {
                        Toast.makeText(Contexto, "Erro ao obter documentos: " + task.getException(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
