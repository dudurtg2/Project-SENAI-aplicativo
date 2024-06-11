package com.bora.Activitys.Users.Profile;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import com.bora.Functions.DAO.User.Updates.ImageUploaderDAO;
import com.bora.Functions.DAO.User.Updates.UserDAO;
import com.bora.Functions.Verifiers;
import com.bora.R;
import com.bora.databinding.ActivityUserProfileBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class ProfileActivity extends AppCompatActivity {
    public static final int PICK_IMAGE_REQUEST = 1;
    public ActivityUserProfileBinding binding;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private FirebaseFirestore firestore;
    private ImageUploaderDAO imageUploader;
    private DocumentReference docRef;
    private String uid;
    private String profileName, profileCPF, profileAdress, profileNumber, profileBirthDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_user_profile);

        binding = ActivityUserProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        imageUploader = new ImageUploaderDAO(this);
        uid = mAuth.getCurrentUser().getUid();
        firestore = FirebaseFirestore.getInstance();
        docRef = firestore.collection("usuarios").document(uid);

        reloadShowInfomations();

        binding.ProfileButtonUpdateProfile.setVisibility(View.GONE);

        binding.ProfileButtonUpdateProfile.setOnClickListener(v -> save());
        EditText[] fields = new EditText[] {binding.ProfileNameShow, binding.ProfileAdressShow, binding.ProfileNumberShow, binding.ProfileBirthDateShow, binding.ProfileCPFShow};

        for (EditText field : fields) {
            field.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (s.length() > 0) {
                        binding.ProfileButtonUpdateProfile.setVisibility(View.VISIBLE);
                    } else {
                        binding.ProfileButtonUpdateProfile.setVisibility(View.GONE);
                    }
                }
                @Override
                public void afterTextChanged(Editable s) {}
            });
        }

        binding.imageButtonPerfil.setOnClickListener(v -> imageUploader.openFileChooser(this));

        imageUploader.loadImagem();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        imageUploader.handleImageResult(requestCode, resultCode, data, this);
    }
    private void reloadShowInfomations() {
        docRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (binding.ProfileNameShow.getText().toString().isEmpty()) {
                    if (document.exists() && document.get("nome") != null) {
                        binding.ProfileNameShow.setHint(document.getString("nome"));
                    } else {
                        binding.ProfileNameShow.setHint(mAuth.getCurrentUser().getDisplayName());
                    }
                }
                if (binding.ProfileAdressShow.getText().toString().isEmpty()) {
                    if (document.exists() && document.get("endereco") != null) {
                        binding.ProfileAdressShow.setHint(document.getString("endereco"));
                    } else {
                        binding.ProfileAdressShow.setHint("Não informado");
                    }
                }
                if (binding.ProfileNumberShow.getText().toString().isEmpty()) {
                    if (document.exists() && document.get("telefone") != null) {
                        binding.ProfileNumberShow.setHint(document.getString("telefone"));
                    } else {
                        binding.ProfileNumberShow.setHint("Não informado");
                    }
                }
                if (binding.ProfileBirthDateShow.getText().toString().isEmpty()) {
                    if (document.exists() && document.get("dataNascimento") != null) {
                        binding.ProfileBirthDateShow.setHint(document.getString("dataNascimento"));
                    } else {
                        binding.ProfileBirthDateShow.setHint("01/01/2000");
                    }
                }
                if (binding.ProfileCPFShow.getText().toString().isEmpty()) {
                    if (document.exists() && document.get("cpf") != null) {
                        binding.ProfileCPFShow.setHint(document.getString("cpf"));
                    } else {
                        binding.ProfileCPFShow.setHint("000.000.000-00");
                    }
                }
            }
        });
    }
    private void save() {
        binding.ProfileProgressBar.setVisibility(View.VISIBLE);
        docRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();

                if (binding.ProfileNameShow.getText().toString().isEmpty()) {
                    if (document.exists() && document.get("nome") != null) {
                        binding.ProfileNameShow.setHint(document.getString("nome"));
                        profileName = document.getString("nome");
                    } else {
                        binding.ProfileNameShow.setHint(mAuth.getCurrentUser().getDisplayName());
                        profileName = mAuth.getCurrentUser().getDisplayName();
                    }
                } else {
                    profileName = binding.ProfileNameShow.getText().toString();
                }

                if (binding.ProfileAdressShow.getText().toString().isEmpty()) {
                    if (document.exists() && document.get("endereco") != null) {
                        binding.ProfileAdressShow.setHint(document.getString("endereco"));
                        profileAdress = document.getString("endereco");
                    } else {
                        binding.ProfileAdressShow.setHint("Não informado");
                        profileAdress = "Não informado";
                    }
                } else {
                    profileAdress = binding.ProfileAdressShow.getText().toString();
                }

                if (binding.ProfileNumberShow.getText().toString().isEmpty()) {
                    if (document.exists() && document.get("telefone") != null) {
                        binding.ProfileNumberShow.setHint(document.getString("telefone"));
                        profileNumber = document.getString("telefone");
                    } else {
                        binding.ProfileNumberShow.setHint("Não informado");
                        profileNumber = "Não informado";
                    }
                } else {
                    profileNumber = binding.ProfileNumberShow.getText().toString();
                }
                if (binding.ProfileBirthDateShow.getText().toString().isEmpty()) {
                    if (document.exists() && document.get("dataNascimento") != null) {
                        binding.ProfileBirthDateShow.setHint(document.getString("dataNascimento"));
                        profileBirthDate = document.getString("dataNascimento");
                    } else {
                        binding.ProfileBirthDateShow.setHint("01/01/2000");
                        profileBirthDate = "01/01/2000";
                    }
                } else {
                    profileBirthDate = binding.ProfileBirthDateShow.getText().toString();
                }

                if (binding.ProfileCPFShow.getText().toString().isEmpty()) {
                    if (document.exists() && document.get("cpf") != null) {
                        binding.ProfileCPFShow.setHint(document.getString("cpf"));
                        profileCPF = document.getString("cpf");
                    } else {
                        binding.ProfileCPFShow.setHint("000.000.000-00");
                        profileCPF = "000.000.000-00";
                    }
                } else {
                    if (!Verifiers.verifierCPF(binding.ProfileCPFShow.getText().toString())) {
                        Toast.makeText(this, "CPF inválido", Toast.LENGTH_SHORT).show();
                        return;
                    } else {
                        profileCPF = binding.ProfileCPFShow.getText().toString();
                    }
                }

                new UserDAO(this).userDTO("usuarios", profileName, profileAdress, profileNumber, profileBirthDate, profileCPF).addOnSuccessListener(aVoid -> {
                    binding.ProfileNameShow.setText("");
                    binding.ProfileAdressShow.setText("");
                    binding.ProfileNumberShow.setText("");
                    binding.ProfileBirthDateShow.setText("");
                    binding.ProfileCPFShow.setText("");
                    reloadShowInfomations();
                });
            }
        });
        binding.ProfileProgressBar.setVisibility(View.GONE);
    }
}