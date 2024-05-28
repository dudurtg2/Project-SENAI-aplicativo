package com.bora.Funcoes.DAO.Usuario;

import android.content.Context;
import android.widget.Toast;
import com.bora.Funcoes.DTO.Usuario.UsuarioDTO;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.HashMap;

public class UsuarioDAO {
    private Context context;
    private FirebaseAuth mAuth;

    public UsuarioDAO(Context context) {
        this.context = context;
    }

    public void usuarioDTO(String tabela, String nome, String endereco, String telefone, String dataNascimento, String cpf, String rg){

        if(dataNascimento.isEmpty()) {
            dataNascimento = "Data de nascimento não informada";
        }
        if(cpf.isEmpty()) {
            cpf = "CPF não informado";
        }
        if(rg.isEmpty()) {
            rg = "RG não informado";
        }

        UsuarioDTO UsuarioDTO = new UsuarioDTO(nome, endereco, telefone, dataNascimento, cpf, rg);

        writeNewUser(tabela, UsuarioDTO);
    }


    public void writeNewUser(String tabela, UsuarioDTO usuarioDTO) {
        FirebaseFirestore FDBD = FirebaseFirestore.getInstance();
        FirebaseDatabase DBD = FirebaseDatabase.getInstance();
        DatabaseReference InsertDBD = DBD.getReference(tabela);
        mAuth = FirebaseAuth.getInstance();
        String uid = mAuth.getCurrentUser().getUid();

        HashMap<String, Object> query = new HashMap<>();

        query.put("nome", usuarioDTO.getNome());
        query.put("endereco", usuarioDTO.getEndereco());
        query.put("telefone", usuarioDTO.getTelefone());
        query.put("dataNascimento", usuarioDTO.getDataNascimento());
        query.put("cpf", usuarioDTO.getCpf());
        query.put("rg", usuarioDTO.getRg());

        InsertDBD.child(uid).setValue(query).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(context, "Gravação Realtime realizada com sucesso", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, " Erro ao gravar Realtime", Toast.LENGTH_SHORT).show();
            }
        });
        FDBD.collection("usuarios")
                .document(uid)
                .set(query)
                .addOnSuccessListener(documentReference -> Toast.makeText(context, "Gravação Banco de Dados realizada com sucesso", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e -> Toast.makeText(context, " Erro ao gravar Banco de Dados", Toast.LENGTH_SHORT).show());
    }


}
