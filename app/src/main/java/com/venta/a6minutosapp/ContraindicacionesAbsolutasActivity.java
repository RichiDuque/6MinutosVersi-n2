package com.venta.a6minutosapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class ContraindicacionesAbsolutasActivity extends AppCompatActivity {

    FloatingActionButton btnSuguienteAbsolutas;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_contraindicaciones_absolutas);
        btnSuguienteAbsolutas=findViewById(R.id.btnSuguienteAbsolutas);

        btnSuguienteAbsolutas.setOnClickListener(view -> {
            Intent intent = new Intent(this, ContraindicacionesRelativasActivity.class);
            startActivity(intent);
        });
    }
}