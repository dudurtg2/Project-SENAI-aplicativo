package com.bora.Activitys.Principal;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import com.bora.Funcoes.DAO.Usuario.UsuarioDAO;
import com.bora.Funcoes.Verificadores;
import com.bora.Activitys.Usuarios.Consulta.ResultaUsuario;
import com.bora.R;
import com.bora.Activitys.Usuarios.Perfis.UsuarioPerfil;
import com.bora.databinding.ActivityMainBinding;

import java.time.LocalDateTime;
import java.time.temporal.ChronoField;

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
            salvar();
        });

        binding.imageButtonBusca.setOnClickListener(v -> {
            startActivity(new Intent(this, ResultaUsuario.class));
        });

        binding.imageButtonUsuario.setOnClickListener(v -> { startActivity( new Intent(this, UsuarioPerfil.class));
        });

    }

    private void salvar() {
        Verificadores verificadores = new Verificadores();

        UsuarioDAO usuario = new UsuarioDAO(this);

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

        String tabela;
        String cpf;
        String rg;

        if (!binding.EditCPF.getText().toString().isEmpty()){
            if (!Verificadores.verificarCPF(binding.EditCPF.getText().toString())){
                Toast.makeText(this, "CPF inválido", Toast.LENGTH_SHORT).show();
                return;
            }else{
                cpf = binding.EditCPF.getText().toString();
            }
        } else {
            cpf = binding.EditCPF.getText().toString();
        }

        if (!binding.EditRG.getText().toString().isEmpty()){
            if (!Verificadores.verificarRG(binding.EditCPF.getText().toString())){
                Toast.makeText(this, "RG inválido", Toast.LENGTH_SHORT).show();
                return;
            }else{
                rg = binding.EditRG.getText().toString();
            }
        } else {
            rg = binding.EditRG.getText().toString();
        }

        if (binding.EditTabela.getText().toString().isEmpty()){
            tabela = binding.EditTabela.getText().toString();
        }else{
            tabela = "usuarios";
        }

        String nome = binding.Editnome.getText().toString();
        String endereco = binding.Editendereco.getText().toString();
        String telefone = binding.Edittelefone.getText().toString();
        String dataNascimento = binding.EditdataNascimento.getText().toString();

        usuario.usuarioDTO(tabela, nome, endereco, telefone, dataNascimento, cpf, rg);

        Toast.makeText(this, "Dados salvos com sucesso", Toast.LENGTH_SHORT).show();
    }
}