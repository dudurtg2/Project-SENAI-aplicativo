package com.bora.Principal.Inicial;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.bora.Autenticacao.Login.LoginActivity;
import com.bora.R;

public class SplashActivily extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_splash_activily);
        new Handler(getMainLooper()).postDelayed(()-> {
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }, 2000);
    }
}