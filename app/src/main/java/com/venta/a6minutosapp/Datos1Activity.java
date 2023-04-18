package com.venta.a6minutosapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Datos1Activity extends AppCompatActivity {

    FloatingActionButton btnSiguiente;
    EditText FechaNacimiento, Nombre,Documento, Edad;

    TextView txtFecha;
    DatePicker dpFecha;

    Spinner spTDOC;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_datos1);

        btnSiguiente = findViewById(R.id.btnSuguiente1);
        FechaNacimiento=findViewById(R.id.editNacimiento);
        Nombre=findViewById(R.id.editNombre);
        Documento=findViewById(R.id.editDocumento);
        dpFecha=findViewById(R.id.dpFecha);
        Edad=findViewById(R.id.editEdad);
        spTDOC=findViewById(R.id.spinnerDocumento);
        txtFecha=findViewById(R.id.txtFecha);
        String j=getFechaDatePicker();
        txtFecha.setText(j);
        traerDatos();
        btnSiguiente.setOnClickListener(view -> {
            if(Nombre.getText().toString().equals("")||spTDOC.getSelectedItem().toString().equals("Tipo de documentación")||Documento.getText().toString().equals("")||Edad.getText().toString().equals("")){
                Toast.makeText(this, "Llene primero todos los campos antes de continuar", Toast.LENGTH_SHORT).show();
            }else {
                SharedPreferences preferences = getSharedPreferences("usur", MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("nombre", Nombre.getText().toString());
                editor.putString("tipo_documento", spTDOC.getSelectedItem().toString());
                editor.putString("num_documento", Documento.getText().toString());
                editor.putString("fecha_nacimiento", FechaNacimiento.getText().toString());
                editor.putString("edad", Edad.getText().toString());
                editor.commit();
                Intent intent = new Intent(this, Datos2Activity.class);
                startActivity(intent);
            }
        });
        FechaNacimiento.setOnClickListener(view -> {
            muestraCalendario(view);
        });

    }
    @Override
    public void onBackPressed() {
        // No hacemos nada para bloquear el botón de retroceso
    }

    private void muestraCalendario(View view) {
        DatePickerDialog d=new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMounth) {
                String MES[] = {"Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio", "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"};
                FechaNacimiento.setText(dayOfMounth+"/"+MES[month]+"/"+year);
                try {
                    Edad.setText(calcularEdad(year+"-"+(month+1)+"-"+dayOfMounth));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        },2000,0,1);
        d.show();
    }

    private String getFechaDatePicker() {
        int day= dpFecha.getDayOfMonth();
        int mnth=dpFecha.getMonth();
        int year=dpFecha.getYear();
        String MES[] = {"Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio", "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"};
        return day+" "+MES[mnth]+" "+year;
    }

    private String calcularEdad(String fecha) throws ParseException {
        // Obtener la fecha de nacimiento del usuario (en formato String)
        String fechaNacimientoStr = fecha;

        // Convertir la fecha de nacimiento a un objeto Date
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date fechaNacimiento = null;
            fechaNacimiento = format.parse(fechaNacimientoStr);


        // Crear un objeto Calendar para la fecha actual
        Calendar ahora = Calendar.getInstance();

        // Crear un objeto Calendar para la fecha de nacimiento
        Calendar fechaNacimientoCal = Calendar.getInstance();
        fechaNacimientoCal.setTime(fechaNacimiento);

        // Obtener la diferencia entre las fechas en años
        int edad = ahora.get(Calendar.YEAR) - fechaNacimientoCal.get(Calendar.YEAR);
        if (ahora.get(Calendar.DAY_OF_YEAR) < fechaNacimientoCal.get(Calendar.DAY_OF_YEAR)) {
            edad--;
        }
        return edad+"";
    }
    private void traerDatos(){
        SharedPreferences preferences = getSharedPreferences("usur", MODE_PRIVATE);

        Nombre.setText(preferences.getString("nombre",""));
        Documento.setText(preferences.getString("num_documento",""));
        FechaNacimiento.setText(preferences.getString("fecha_nacimiento",""));
        Edad.setText(preferences.getString("edad",""));
        if(preferences.getString("tipo_documento","").equals("Cedula de ciudadania")){
            spTDOC.setSelection(1);
        }else if(preferences.getString("tipo_documento","").equals("Tarjeta de identidad")){
            spTDOC.setSelection(2);
        }else {
            spTDOC.setSelection(3);
        }
    }
}