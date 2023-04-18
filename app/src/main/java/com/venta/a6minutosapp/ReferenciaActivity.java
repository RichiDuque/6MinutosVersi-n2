package com.venta.a6minutosapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class ReferenciaActivity extends AppCompatActivity {

    Button btnRegresar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_referencia);

        btnRegresar = findViewById(R.id.btnRegresar);

        btnRegresar.setOnClickListener(view -> {
            Intent intent = new Intent(this, Resultados.class);
            intent.putExtra("noDetencion",getIntent().getExtras().getString("noDetencion"));
            intent.putExtra("duracionDetencion",getIntent().getExtras().getString("duracionDetencion"));
            intent.putExtra("listaObjetos", getIntent().getSerializableExtra("listaObjetos"));
            intent.putExtra("noVueltas",getIntent().getExtras().getString("noVueltas"));
            startActivity(intent);
        });

    }
}