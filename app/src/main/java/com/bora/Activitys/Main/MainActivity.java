package com.bora.Activitys.Main;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import com.bora.Functions.DAO.User.UserDAO;
import com.bora.Functions.Verifiers;
import com.bora.Activitys.Users.Queries.UserResults;
import com.bora.R;
import com.bora.Activitys.Users.Profiles.UserProfile;
import com.bora.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());



        binding.BTMINSERTE.setOnClickListener(v -> {
            save();
        });

        binding.imageButtonBusca.setOnClickListener(v -> {
            startActivity(new Intent(this, UserResults.class));
        });

        binding.imageButtonUsuario.setOnClickListener(v -> { startActivity( new Intent(this, UserProfile.class));
        });

    }

    private void save() {
        Verifiers verificadores = new Verifiers();

        UserDAO userDAO = new UserDAO(this);

        EditText[] fields = new EditText[]{
                binding.Editnome,
                binding.Editendereco,
                binding.Edittelefone,
        };

        for (EditText field : fields) {
            if (field.getText().toString().isEmpty()) {
                Toast.makeText(this, "Nome, Telefone e Endereço são obrigatórios", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        String table;
        String cpf;
        String rg;

        if (!binding.EditCPF.getText().toString().isEmpty()){
            if (!Verifiers.verifierCPF(binding.EditCPF.getText().toString())){
                Toast.makeText(this, "CPF inválido", Toast.LENGTH_SHORT).show();
                return;
            }else{
                cpf = binding.EditCPF.getText().toString();
            }
        } else {
            cpf = binding.EditCPF.getText().toString();
        }

        if (!binding.EditRG.getText().toString().isEmpty()){
            if (!Verifiers.verifierRG(binding.EditCPF.getText().toString())){
                Toast.makeText(this, "RG inválido", Toast.LENGTH_SHORT).show();
                return;
            }else{
                rg = binding.EditRG.getText().toString();
            }
        } else {
            rg = binding.EditRG.getText().toString();
        }

        if (binding.Edittable.getText().toString().isEmpty()){
            table = binding.Edittable.getText().toString();
        }else{
            table = "usuarios";
        }

        String nome = binding.Editnome.getText().toString();
        String endereco = binding.Editendereco.getText().toString();
        String telefone = binding.Edittelefone.getText().toString();
        String dataNascimento = binding.EditdataNascimento.getText().toString();

        userDAO.userDTO(table, nome, endereco, telefone, dataNascimento, cpf, rg);

        Toast.makeText(this, "Dados salvos com sucesso", Toast.LENGTH_SHORT).show();
    }
}