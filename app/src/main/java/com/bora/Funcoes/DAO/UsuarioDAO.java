package com.bora.Funcoes.DAO;

import android.content.Context;
import android.widget.Toast;

import com.bora.Funcoes.DTO.UsuarioDTO;
import com.bora.Principal.MainActivity;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class UsuarioDAO {
    private Context context;

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
        FirebaseDatabase DBD = FirebaseDatabase.getInstance();
        DatabaseReference InsertDBD = DBD.getReference(tabela);

        String key = InsertDBD.push().getKey();

        HashMap<String, Object> query = new HashMap<>();

        query.put("nome", usuarioDTO.getNome());
        query.put("endereco", usuarioDTO.getEndereco());
        query.put("telefone", usuarioDTO.getTelefone());
        query.put("dataNascimento", usuarioDTO.getDataNascimento());
        query.put("cpf", usuarioDTO.getCpf());
        query.put("rg", usuarioDTO.getRg());
        query.put("key", key);

        InsertDBD.child(key).setValue(query).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(context, "Gravação realizada com sucesso", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, " Erro ao gravar", Toast.LENGTH_SHORT).show();
            }
        });;
    }


}
