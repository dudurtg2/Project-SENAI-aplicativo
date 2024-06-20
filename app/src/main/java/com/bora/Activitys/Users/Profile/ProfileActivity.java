package com.bora.Activitys.Users.Profile;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.bora.Activitys.Users.Authentication.LoginActivity;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Calendar;

public class ProfileActivity extends AppCompatActivity {
    public static final int PICK_IMAGE_REQUEST = 1;
    public ActivityUserProfileBinding binding;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private FirebaseFirestore firestore;
    private ImageUploaderDAO imageUploader;
    private DocumentReference docRef;
    private String uid;
    private String profileName, profileCPF, profileAdress, profileNumber, profileBirthDate, profileCep;

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
        EditText[] fields = new EditText[] {binding.ProfileNameShow, binding.ProfileAdressShow, binding.ProfileNumberShow, binding.ProfileBirthDateShow, binding.ProfileCPFShow, binding.ProfileCepShow};

        binding.ProfileBirthDateShow.setOnClickListener(v -> showDatePickerDialog());

        binding.buttonLogout.setOnClickListener(v -> {
            mAuth.signOut();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        });
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
                    if (field == binding.ProfileCepShow && s.length() == 8) {
                        consultationCEP(s.toString(), () -> {
                        });
                    }
                }
                @Override
                public void afterTextChanged(Editable s) {}
            });
        }

        binding.imageButtonPerfil.setOnClickListener(v -> imageUploader.openFileChooser(this));

        imageUploader.loadImagem();
    }
    private void showDatePickerDialog() {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                ProfileActivity.this,
                (view, year1, monthOfYear, dayOfMonth) -> {
                    String selectedDate = String.format("%02d/%02d/%04d", dayOfMonth, (monthOfYear + 1), year1);
                    binding.ProfileBirthDateShow.setText(selectedDate);
                },
                year, month, day);
        datePickerDialog.show();
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
                        binding.ProfileAdressShow.setHint("Endereco não informado");
                    }
                }
                if (binding.ProfileNumberShow.getText().toString().isEmpty()) {
                    if (document.exists() && document.get("telefone") != null) {
                        binding.ProfileNumberShow.setHint(document.getString("telefone"));
                    } else {
                        binding.ProfileNumberShow.setHint("Sem Telefone");
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
                if (binding.ProfileCepShow.getText().toString().isEmpty()) {
                    if (document.exists() && document.get("cep") != null) {
                        binding.ProfileCepShow.setHint(document.getString("cep"));
                    } else {
                        binding.ProfileCepShow.setHint("00000-000");
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

                if (binding.ProfileCepShow.getText().toString().isEmpty()) {
                    if (document.exists() && document.get("cep") != null) {
                        binding.ProfileCepShow.setHint(document.getString("cep"));
                        profileCep = document.getString("cep");
                    } else {
                        binding.ProfileCepShow.setHint("Não informado");
                        profileCep = "Não informado";
                    }
                } else {
                    profileCep = binding.ProfileCepShow.getText().toString();
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
                        binding.ProfileProgressBar.setVisibility(View.GONE);
                        return;
                    } else {
                        profileCPF = binding.ProfileCPFShow.getText().toString();
                    }
                }
                new UserDAO(this).userDTO("usuarios", profileName, profileAdress, profileNumber, profileBirthDate, profileCPF, profileCep).addOnSuccessListener(aVoid -> {

                    binding.ProfileNameShow.setText("");
                    binding.ProfileAdressShow.setText("");
                    binding.ProfileNumberShow.setText("");
                    binding.ProfileBirthDateShow.setText("");
                    binding.ProfileCPFShow.setText("");
                    binding.ProfileCepShow.setText("");

                    reloadShowInfomations();

                    binding.ProfileProgressBar.setVisibility(View.GONE);
                }).addOnFailureListener(e -> {
                    Toast.makeText(this, "Erro ao atualizar perfil: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    binding.ProfileProgressBar.setVisibility(View.GONE);
                });
            }
        });
    }

    private void consultationCEP(String cep, Runnable callback) {
        String url = "https://viacep.com.br/ws/" + cep + "/json/";
        new AsyncTask<String, Void, String>() {
            @Override
            protected String doInBackground(String... strings) {
                HttpURLConnection urlConnection = null;
                BufferedReader reader = null;
                String jsonResponse = null;
                try {
                    URL requestUrl = new URL(strings[0]);
                    urlConnection = (HttpURLConnection) requestUrl.openConnection();
                    urlConnection.setRequestMethod("GET");
                    urlConnection.connect();

                    InputStream inputStream = urlConnection.getInputStream();
                    StringBuilder builder = new StringBuilder();
                    if (inputStream == null) {
                        return null;
                    }
                    reader = new BufferedReader(new InputStreamReader(inputStream));
                    String line;
                    while ((line = reader.readLine()) != null) {
                        builder.append(line).append("\n");
                    }
                    if (builder.length() == 0) {
                        return null;
                    }
                    jsonResponse = builder.toString();

                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (urlConnection != null) {
                        urlConnection.disconnect();
                    }
                    if (reader != null) {
                        try {
                            reader.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
                return jsonResponse;
            }
            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                if (s != null) {
                    try {
                        JSONObject jsonObject = new JSONObject(s);
                        String logradouro = jsonObject.getString("logradouro");
                        String bairro = jsonObject.getString("bairro");
                        String enderecoCompleto = logradouro + ", " + bairro;
                        binding.ProfileAdressShow.setText(enderecoCompleto);
                        callback.run();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(ProfileActivity.this, "CEP inválido ou não encontrado", Toast.LENGTH_SHORT).show();
                    binding.ProfileProgressBar.setVisibility(View.GONE);
                }
            }
        }.execute(url);
    }
}