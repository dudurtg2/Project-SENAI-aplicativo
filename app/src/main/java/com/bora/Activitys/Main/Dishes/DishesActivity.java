package com.bora.Activitys.Main.Dishes;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bora.Functions.DAO.Dishes.Updates.DishesDAO;
import com.bora.Functions.DAO.Dishes.Updates.ImageUploaderDAO;
import com.bora.Functions.DTO.Dishes.DishesDTO;
import com.bora.R;
import com.bora.databinding.ActivityCreateDishesBinding;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DishesActivity extends AppCompatActivity {

    public static final int PICK_IMAGE_REQUEST = 1;
    private ActivityCreateDishesBinding binding;
    private DatabaseReference mDatabase;
    private String uniqueId;
    private ImageUploaderDAO imageUploader;
    public ImageView image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_dishes);
        binding = ActivityCreateDishesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.DishesImageUpdate.setImageResource(R.drawable.a);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        uniqueId = mDatabase.push().getKey();
        imageUploader = new ImageUploaderDAO(this, uniqueId);
        image = binding.DishesImageUpdate;

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
        String name = binding.DishesNameUpdate.getText().toString();
        String description = binding.DishesDescriptionUpdate.getText().toString();
        String uid = uniqueId;

        DishesDAO dishesDAO = new DishesDAO(this);
        dishesDAO.addDishToFirestore(name, description, uid);

        imageUploader.uploadImage();
    }

}
