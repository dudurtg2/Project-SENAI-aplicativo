package com.bora.Functions.DAO.Dishes.Updates;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.bora.Activitys.Main.Dishes.DishesActivity;
import com.bora.databinding.ActivityCreateDishesBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class ImageUploaderDAO {
    private Bitmap bitmap;
    private Bitmap resizedBitmap;
    private final Context context;
    private final StorageReference storageReference;
    private final FirebaseUser currentUser;

    public ImageUploaderDAO(Context context, String Uid) {
        this.context = context;
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        this.currentUser = mAuth.getCurrentUser();
        this.storageReference = FirebaseStorage.getInstance().getReference(Uid);
    }

    public void openFileChooser(DishesActivity dishesActivity) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        dishesActivity.startActivityForResult(intent, DishesActivity.PICK_IMAGE_REQUEST);
    }

    public void handleImageResult(int requestCode, int resultCode, @Nullable Intent data, DishesActivity dishesActivity) {
        if (requestCode == DishesActivity.PICK_IMAGE_REQUEST && resultCode == DishesActivity.RESULT_OK && data != null && data.getData() != null) {
            Uri imageUri = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(dishesActivity.getContentResolver(), imageUri);
                resizedBitmap = resizeBitmap(bitmap, 256, 256);
                dishesActivity.image.setImageBitmap(resizedBitmap);
            } catch (IOException e) {
                Toast.makeText(context, "Falha ao carregar a imagem", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void uploadImage() {
        uploadFile(resizedBitmap);
    }

    private void uploadFile(Bitmap bitmap) {
        if (bitmap != null) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] data = baos.toByteArray();

            StorageReference fileReference = storageReference.child("DishesMainDown.png");

            fileReference.putBytes(data)
                    .addOnSuccessListener(taskSnapshot -> {
                        Toast.makeText(context, "Upload bem-sucedido", Toast.LENGTH_SHORT).show();
                    })
                    .addOnFailureListener(e -> Toast.makeText(context, "Falha no upload: " + e.getMessage(), Toast.LENGTH_SHORT).show());

        } else {
            Toast.makeText(context, "Nenhum arquivo selecionado", Toast.LENGTH_SHORT).show();
        }
    }

    private Bitmap resizeBitmap(Bitmap bitmap, int width, int height) {
        return Bitmap.createScaledBitmap(bitmap, width, height, true);
    }
}
