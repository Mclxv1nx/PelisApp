package com.example.pelisapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    Button btnIrPeliculas, btnIrActores;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnIrPeliculas = findViewById(R.id.btnIrPeliculas);
        btnIrActores = findViewById(R.id.btnIrActores);

        btnIrPeliculas.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, PeliculasActivity.class);
            startActivity(intent);
        });

        btnIrActores.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ActoresActivity.class);
            startActivity(intent);
        });
    }
}