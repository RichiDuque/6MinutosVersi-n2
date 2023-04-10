package com.venta.a6minutosapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {


    Button btnEmpezar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        setTheme(R.style.Theme_6MinutosApp);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnEmpezar = findViewById(R.id.btnEmpezar);

        SharedPreferences preferences=getSharedPreferences("usur",MODE_PRIVATE);
        if(preferences.getString("CorreoP","").isEmpty()){
            correo();
        }

        btnEmpezar.setOnClickListener(view -> {
            Intent intent = new Intent(this, Datos1Activity.class);
            //Intent intent = new Intent(this, FormulasActivity.class);
            startActivity(intent);
        });
    }

    private void correo(){
        final Dialog dialog=new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.correoprofesional);

        TextInputEditText Correo=dialog.findViewById(R.id.EdittextCorreo);
        Button btncorreo=dialog.findViewById(R.id.ButtonCorreo);


        btncorreo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validarCorreo(Correo)){
                    String inputText = Correo.getText().toString();
                    SharedPreferences preferences = getSharedPreferences("usur", MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("CorreoP", inputText);
                    editor.commit();
                    Toast.makeText(MainActivity.this, "Correo guardado", Toast.LENGTH_SHORT).show();
                    dialog.cancel();
                }
            }
        });
        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations =R.style.DialogAnimations;
        dialog.getWindow().setGravity(Gravity.BOTTOM);
    }

    private boolean validarCorreo(TextInputEditText correo){
        Pattern pattern = Pattern
                .compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                        + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");
        Matcher mather = pattern.matcher(correo.getText().toString());
        if(mather.find() == true) {
            return true;
        }else{
            Toast.makeText(this, "Correo electronico invalido",Toast.LENGTH_SHORT).show();
            return false;
        }
    }
}