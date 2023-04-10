package com.venta.a6minutosapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class ContraindicacionesRelativasActivity extends AppCompatActivity {

    FloatingActionButton btnSuguienteFormulas;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contraindicaciones_relativas);
        btnSuguienteFormulas=findViewById(R.id.btnSuguienteRelativas);

        btnSuguienteFormulas.setOnClickListener(view -> {
            Intent intent = new Intent(this, FormulasActivity.class);
            startActivity(intent);
        });
    }
}