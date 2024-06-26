package com.bora.Activitys.Dishes;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import com.bora.Activitys.Main.MainActivity;
import com.bora.Functions.DAO.Dishes.Updates.DishesDAO;
import com.bora.Functions.DAO.Dishes.Updates.ImageUploaderDAO;
import com.bora.R;
import com.bora.databinding.ActivityDishesCreateBinding;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DishesActivity extends AppCompatActivity {
    public static final int PICK_IMAGE_REQUEST = 1;
    private ActivityDishesCreateBinding binding;
    private DatabaseReference mDatabase;
    private String uniqueId;
    private ImageUploaderDAO imageUploader;
    public ImageView image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dishes_create);
        binding = ActivityDishesCreateBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.DishesImageUpdate.setImageResource(R.drawable.baseimageforuser);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        uniqueId = mDatabase.push().getKey();
        imageUploader = new ImageUploaderDAO(this, uniqueId);
        image = binding.DishesImageUpdate;

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.dishes_options, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        ArrayAdapter<CharSequence> category = ArrayAdapter.createFromResource(this, R.array.dishes_category, android.R.layout.simple_spinner_item);
        category.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        binding.DishesOptionsSpinner.setAdapter(adapter);
        binding.DishesCategorySpinner.setAdapter(category);

        binding.DishesImageUpdate.setOnClickListener(view -> imageUploader.openFileChooser(this));
        binding.DishesButtonUpdate.setOnClickListener(view -> updateInsets());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (imageUploader != null) {
            imageUploader.handleImageResult(requestCode, resultCode, data, this);

        }
    }

    private void updateInsets() {
        if (binding.DishesNameUpdate.getText().toString().isEmpty()
                && binding.DishesDescriptionUpdate.getText().toString().isEmpty()
                && binding.DishesPriseUpdate.getText().toString().isEmpty() ){
            Toast.makeText(this, "Preencha todos os campos", Toast.LENGTH_SHORT).show();
            return;
        }
        String name = binding.DishesNameUpdate.getText().toString();
        String description = binding.DishesDescriptionUpdate.getText().toString();
        String uid = uniqueId;
        String table = binding.DishesOptionsSpinner.getSelectedItem().toString();
        String preco = binding.DishesPriseUpdate.getText().toString();
        String category = binding.DishesCategorySpinner.getSelectedItem().toString();

        DishesDAO dishesDAO = new DishesDAO(this);
        dishesDAO.addDishToFirestore(name, description, uid, preco, category, table).addOnCompleteListener(task -> finish());

        imageUploader.uploadImage(table);
        startActivity(new Intent(this, MainActivity.class));
    }
}
