package com.bora.Activitys.Main.Profile;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.bora.Functions.DAO.User.Updates.ImageUploaderDAO;
import com.bora.Functions.DAO.User.Updates.UserDAO;
import com.bora.Functions.Verifiers;
import com.bora.R;
import com.bora.databinding.ActivityPerfilUsuarioBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ProfileActivity extends AppCompatActivity {

    public static final int PICK_IMAGE_REQUEST = 1;
    public ActivityPerfilUsuarioBinding binding;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private ImageUploaderDAO imageUploader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_perfil_usuario);
        binding = ActivityPerfilUsuarioBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        imageUploader = new ImageUploaderDAO(this);

        binding.BTMINSERTE.setOnClickListener(v -> save());

        binding.imageButtonPerfil.setOnClickListener(v -> imageUploader.openFileChooser(this));

        imageUploader.loadImagem();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        imageUploader.handleImageResult(requestCode, resultCode, data, this);
    }

    private void save() {
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

        if (!binding.EditCPF.getText().toString().isEmpty()) {
            if (!Verifiers.verifierCPF(binding.EditCPF.getText().toString())) {
                Toast.makeText(this, "CPF inválido", Toast.LENGTH_SHORT).show();
                return;
            } else {
                cpf = binding.EditCPF.getText().toString();
            }
        } else {
            cpf = binding.EditCPF.getText().toString();
        }

        if (!binding.EditRG.getText().toString().isEmpty()) {
            if (!Verifiers.verifierRG(binding.EditCPF.getText().toString())) {
                Toast.makeText(this, "RG inválido", Toast.LENGTH_SHORT).show();
                return;
            } else {
                rg = binding.EditRG.getText().toString();
            }
        } else {
            rg = binding.EditRG.getText().toString();
        }

        if (binding.Edittable.getText().toString().isEmpty()) {
            table = binding.Edittable.getText().toString();
        } else {
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