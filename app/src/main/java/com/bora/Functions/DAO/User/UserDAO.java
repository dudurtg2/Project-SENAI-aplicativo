package com.bora.Functions.DAO.User;

import android.content.Context;
import android.widget.Toast;

import com.bora.Functions.DTO.Users.UserDTO;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.time.LocalDateTime;
import java.util.HashMap;

public class UserDAO {
    private Context context;
    private FirebaseAuth mAuth;

    public UserDAO(Context context) {
        this.context = context;
    }

    public void userDTO(String table, String name, String endereco, String telefone, String dataNascimento, String cpf, String rg) {
        if (dataNascimento.isEmpty()) {
            dataNascimento = "Data de nascimento não informada";
        }
        if (cpf.isEmpty()) {
            cpf = "CPF não informado";
        }
        if (rg.isEmpty()) {
            rg = "RG não informado";
        }

        UserDTO usuarioDTO = new UserDTO(name, endereco, telefone, dataNascimento, cpf, rg);
        writeNewUser(table, usuarioDTO);
    }

    public void writeNewUser(String table, UserDTO usuarioDTO) {
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = database.getReference(table);
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

                query.put("name", usuarioDTO.getName());
                query.put("endereco", usuarioDTO.getAddress());
                query.put("telefone", usuarioDTO.getNumber());
                query.put("dataNascimento", usuarioDTO.getBirthDate());
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
