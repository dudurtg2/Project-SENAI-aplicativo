package com.bora.Funcoes.DAO.Usuario.Consulta;

import android.content.Context;
import android.widget.Toast;

import com.bora.Funcoes.DTO.Usuario.UsuarioDTO;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class ConsultaDAO {
    private Context Contexto;
    private FirebaseFirestore db;
    private List<UsuarioDTO> usuarioList;

    public ConsultaDAO(Context Contexto) {
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
