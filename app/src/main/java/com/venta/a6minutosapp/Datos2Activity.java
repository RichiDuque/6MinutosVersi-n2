package com.venta.a6minutosapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;

import java.text.DecimalFormat;

public class Datos2Activity extends AppCompatActivity {

    FloatingActionButton btnsiguientemedicamentos;
    Spinner spSexo,spPeso;

    TextView datos1;
    EditText editFC, spMts,spCm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_datos2);
        btnsiguientemedicamentos=findViewById(R.id.btnSiguiente2);
        spSexo=findViewById(R.id.spinnerSexo);
        spPeso=findViewById(R.id.spinnerPeso);
        spMts=findViewById(R.id.spinnerTallaMts);
        spCm=findViewById(R.id.spinnerTalla);
        editFC=findViewById(R.id.editFC);
        traerDatos();

        InputFilter inputFilterMetros = new InputFilter() {

            int min = 0;
            int max = 3;

            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                try {
                    int input = Integer.parseInt(dest.toString() + source.toString());
                    if (isInRange(input))
                        return null;
                } catch (NumberFormatException nfe) {
                    // do nothing
                }
                return "";
            }

            private boolean isInRange(int value) {
                return value >= min && value <= max;
            }
        };

        InputFilter inputFilterCentimetros = new InputFilter() {

            int min = 0;
            int max = 100;

            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                try {
                    int input = Integer.parseInt(dest.toString() + source.toString());
                    if (isInRange(input))
                        return null;
                } catch (NumberFormatException nfe) {
                    // do nothing
                }
                return "";
            }

            private boolean isInRange(int value) {
                return value >= min && value <= max;
            }
        };
        spMts.setFilters(new InputFilter[]{inputFilterMetros});
        spCm.setFilters(new InputFilter[]{inputFilterCentimetros});

        SharedPreferences preferences=getSharedPreferences("usur",MODE_PRIVATE);
        try {
           double edadint=Integer.parseInt(preferences.getString("edad","1"));
           edadint =208.75-(0.73*edadint);

            DecimalFormat df = new DecimalFormat("#.##");
            String rounded = df.format(edadint);


            editFC.setText(rounded);
        }catch (Exception e ){
            editFC.setText("208");
        }

        btnsiguientemedicamentos.setOnClickListener(view -> {

            if(editFC.getText().toString().equals("")||spSexo.getSelectedItem().toString().equals("Sexo")||spPeso.getSelectedItem().toString().equals("Peso")||spMts.getText().toString().equals("")||spCm.getText().toString().equals("")){
                Toast.makeText(this, "Llene primero todos los campos antes de continuar", Toast.LENGTH_SHORT).show();
            }else {
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("sexo", spSexo.getSelectedItem().toString());
                editor.putString("peso", spPeso.getSelectedItem().toString());
                editor.putString("Talla-mts", spMts.getText().toString());
                editor.putString("FC-teorica", editFC.getText().toString());
                editor.putString("Talla-cm", spCm.getText().toString());
                editor.commit();
                showSheet();
            }
        });

    }
    private void showSheet() {
        final Dialog dialog=new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.sheetlayout1);

        TextInputEditText medicamentos=dialog.findViewById(R.id.EdittextMedicamentos);
        Button btnMedicamentos=dialog.findViewById(R.id.ButtonMedicamentos);


        btnMedicamentos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String inputText = medicamentos.getText().toString();
                SharedPreferences preferences=getSharedPreferences("usur",MODE_PRIVATE);
                SharedPreferences.Editor editor=preferences.edit();
                editor.putString("Medicamentos",inputText);
                editor.commit();
                Toast.makeText(Datos2Activity.this, "Guardo los medicamentos", Toast.LENGTH_SHORT).show();
                showSheet2();
            }
        });
        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations =R.style.DialogAnimations;
        dialog.getWindow().setGravity(Gravity.BOTTOM);
    }

    private void showSheet2() {
        final Dialog dialog=new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.sheetlayout2);


        Button buttonSiContraindicaciones=dialog.findViewById(R.id.Buttoncontraindicaciones);
        Button buttonNoContraindicaciones=dialog.findViewById(R.id.Buttoncancelarcontraind);
        buttonSiContraindicaciones.setOnClickListener(view -> {
            Intent intent = new Intent(this,ContraindicacionesAbsolutasActivity.class);
            startActivity(intent);
        });
        buttonNoContraindicaciones.setOnClickListener(view -> {
            Intent intent = new Intent(this,FormulasActivity.class);
            intent.putExtra("sexo",spSexo.getSelectedItem().toString());
            startActivity(intent);
        });
        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations =R.style.DialogAnimations;
        dialog.getWindow().setGravity(Gravity.BOTTOM);
    }

    private void traerDatos(){
        SharedPreferences preferences = getSharedPreferences("usur", MODE_PRIVATE);

        editFC.setText(preferences.getString("FC-teorica",""));
        if(preferences.getString("sexo","").equals("Hombre")){
            spSexo.setSelection(1);
        }else {
            spSexo.setSelection(2);
        }
        try {
            spPeso.setSelection(Integer.parseInt(preferences.getString("peso","")));
        }catch (Exception E){
            spPeso.setSelection(Integer.parseInt(preferences.getString("peso","0")));
        }

        spMts.setText(preferences.getString("Talla-mts",""));
        spCm.setText(preferences.getString("Talla-cm",""));
    }
}