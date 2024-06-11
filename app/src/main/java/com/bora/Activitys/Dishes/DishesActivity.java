package com.bora.Activitys.Dishes;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import com.bora.Activitys.Main.MainActivity;
import com.bora.Functions.DAO.Dishes.Updates.DishesDAO;
import com.bora.Functions.DAO.Dishes.Updates.ImageUploaderDAO;
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

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.dishes_options, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        binding.DishesOptionsSpinner.setAdapter(adapter);

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
        String table = binding.DishesOptionsSpinner.getSelectedItem().toString();

        DishesDAO dishesDAO = new DishesDAO(this);
        dishesDAO.addDishToFirestore(name, description, uid, table);

        imageUploader.uploadImage(table);
        finish();
        startActivity(new Intent(this, MainActivity.class));
    }
}
