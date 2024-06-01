package com.bora.Funcoes.DAO.Usuario;

import android.content.Context;
import android.widget.Toast;

import com.bora.Funcoes.DTO.Usuario.UsuarioDTO;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.time.LocalDateTime;
import java.util.HashMap;

public class UsuarioDAO {
    private Context context;
    private FirebaseAuth mAuth;

    public UsuarioDAO(Context context) {
        this.context = context;
    }

    public void usuarioDTO(String tabela, String nome, String endereco, String telefone, String dataNascimento, String cpf, String rg) {
        if (dataNascimento.isEmpty()) {
            dataNascimento = "Data de nascimento não informada";
        }
        if (cpf.isEmpty()) {
            cpf = "CPF não informado";
        }
        if (rg.isEmpty()) {
            rg = "RG não informado";
        }

        UsuarioDTO usuarioDTO = new UsuarioDTO(nome, endereco, telefone, dataNascimento, cpf, rg);
        writeNewUser(tabela, usuarioDTO);
    }

    public void writeNewUser(String tabela, UsuarioDTO usuarioDTO) {
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = database.getReference(tabela);
        mAuth = FirebaseAuth.getInstance();
        String uid = mAuth.getCurrentUser().getUid();

        LocalDateTime now = LocalDateTime.now();
        int currentMinutesOfYear = (now.getDayOfYear() - 1) * 24 * 60 + now.getHour() * 60 + now.getMinute();

        DocumentReference docRef = firestore.collection("usuarios").document(uid);
        docRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists() && document.get("data") != null) {
                    String lastDataString = String.valueOf(document.get("data"));
                    int lastData = Integer.parseInt(lastDataString);

                    if (currentMinutesOfYear < lastData + 60) {
                        Toast.makeText(context, "Ainda falta " + (60 - (currentMinutesOfYear - lastData)) + " minutos para a próxima gravação", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }

                HashMap<String, Object> query = new HashMap<>();

                query.put("nome", usuarioDTO.getNome());
                query.put("endereco", usuarioDTO.getEndereco());
                query.put("telefone", usuarioDTO.getTelefone());
                query.put("dataNascimento", usuarioDTO.getDataNascimento());
                query.put("cpf", usuarioDTO.getCpf());
                query.put("rg", usuarioDTO.getRg());
                query.put("uid", uid);
                query.put("data", currentMinutesOfYear);

                databaseReference.child(uid).setValue(query).addOnCompleteListener(task1 -> {
                    if (task1.isSuccessful()) {
                        Toast.makeText(context, "Gravação Realtime realizada com sucesso", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, "Erro ao gravar Realtime", Toast.LENGTH_SHORT).show();
                    }
                });

                firestore.collection("usuarios")
                        .document(uid)
                        .set(query)
                        .addOnSuccessListener(documentReference -> Toast.makeText(context, "Gravação Banco de Dados realizada com sucesso", Toast.LENGTH_SHORT).show())
                        .addOnFailureListener(e -> Toast.makeText(context, "Erro ao gravar Banco de Dados", Toast.LENGTH_SHORT).show());
            }
        });
    }
}
