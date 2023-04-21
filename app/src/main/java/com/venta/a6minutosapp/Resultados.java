package com.venta.a6minutosapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;

import android.content.DialogInterface;

import android.content.ContextWrapper;

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
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.tabs.TabLayout;
import com.venta.a6minutosapp.email.JavaMailAPI;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class Resultados extends AppCompatActivity {
    TabLayout Tabresultados;
    Button BtnAtrasDescanso, BtnExportar, BtnReferencia;
    TextView FC_maximo,PercentMaxFC,seispercent,ochopercent,DIFFC,FC_Recp;
    TextView DIF_Presion,DIF_Sat;
    TextView textoFormula,txttroster,txtperceDistancia;
    //Paciente
    TextView txtNombreP,txtEdadP,txtAlturaP,txtPesoP,txtIMCP,txtCedulaP,txtFechaNP,txtFCMTP,txtNoDeten,txtTiempoDeten;

    double FCMT__,FCMAlcanzado__,sesentaFC__,ochentaFC__,DIF__FC,FC__Recp;
    double TA__,Dif__Sat;
    double Troster__,PercentDista__;

    Dialog dialog;
    String Formula;
    double VO2__,METS__;
    TextView txtVOmax,txtMETs;

    ArrayList<dataMinuto> dataList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resultados);
        BtnAtrasDescanso=findViewById(R.id.buttonAtrasDescanso);
        BtnExportar = findViewById(R.id.btnExportar);
        //VariableFC
        FC_maximo=findViewById(R.id.FC_Máximo);
        PercentMaxFC=findViewById(R.id.Dif_FCMax_Porce);
        seispercent=findViewById(R.id.siesPercent);
        ochopercent=findViewById(R.id.ochoPercent);
        DIFFC=findViewById(R.id.Dif_FC);
        FC_Recp=findViewById(R.id.FC_Recp);
        //VariablePresora
        DIF_Presion=findViewById(R.id.DIF_Presion);
        DIF_Sat=findViewById(R.id.DIF_Sat);
        //RecorridotextoFormula,txttroster,txtperceDistancia
        textoFormula=findViewById(R.id.textFormula);
        txttroster=findViewById(R.id.txtTroster);
        txtperceDistancia=findViewById(R.id.txtPercentDistancia);
        //aeróbica
        txtVOmax=findViewById(R.id.txtVOmax);
        txtMETs=findViewById(R.id.txtMETs);

        //paciente
        txtNombreP=findViewById(R.id.txtNombre);
        txtEdadP=findViewById(R.id.txtEdad);
        txtAlturaP=findViewById(R.id.txtAltura);
        txtPesoP=findViewById(R.id.txtPeso);
        txtIMCP=findViewById(R.id.txtIMC);
        txtCedulaP=findViewById(R.id.txtCedula);
        txtFechaNP=findViewById(R.id.txtFechaNacimiento);
        txtFCMTP=findViewById(R.id.txtFCPaciente);
        BtnReferencia = findViewById(R.id.btnReferencias);
        txtNoDeten=findViewById(R.id.txtNoDeten);
        txtTiempoDeten=findViewById(R.id.txtTiempoDeten);

        dialog=new Dialog(Resultados.this);

        BtnAtrasDescanso.setOnClickListener(view -> {
            showResultBoard(dataList);
        });

        BtnExportar.setOnClickListener(view -> {
            ExportarExcel();
        });

        BtnReferencia.setOnClickListener(view -> {
            Intent intent = new Intent(this, ReferenciaActivity.class);
            intent.putExtra("noDetencion",getIntent().getExtras().getString("noDetencion"));
            intent.putExtra("duracionDetencion",getIntent().getExtras().getString("duracionDetencion"));
            intent.putExtra("listaObjetos", getIntent().getSerializableExtra("listaObjetos"));
            intent.putExtra("noVueltas",getIntent().getExtras().getString("noVueltas"));
            startActivity(intent);
        });

        dataList = (ArrayList<dataMinuto> ) getIntent().getSerializableExtra("listaObjetos");
        ArrayList<Integer> l = new ArrayList<>();
        ArrayList<Integer> Ta = new ArrayList<>();
        ArrayList<Integer> sat = new ArrayList<>();

        dataMinuto person ;
        Iterator iter = dataList.iterator();
        int aux=0;
        while(iter.hasNext()){
            person = (dataMinuto) iter.next(); /* Cast del Objeto a la Clase Persona*/
            aux++;
            try {
                l.add(Integer.parseInt(person.getFC()));
            }catch (Exception e){

            }
            try {
                Ta.add(Integer.parseInt(person.getTAS()));
            }catch (Exception e){

            }
            try {
                sat.add(Integer.parseInt(person.getSaturacion()));
            }catch (Exception e){

            }

                                           //por medio de sus Getters*/
        }
        CalculoFC(l);
        /*System.out.println("//////////////resultados////////////////////");*/
        CalculoPresora(Ta);
        CalculoSat(sat);
        CalculoRecorrido();
        CalculoAerobica();
        ShowPaciente();
        GuardarResult();



    }

    private void showResultBoard(ArrayList<dataMinuto> dataList) {
        dialog.setContentView(R.layout.board);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(false);
        TextView txRep  = dialog.findViewById(R.id.textView6);

        // Define el formato para cada columna
        String formato = "%-10s %-3s %-6s %-3s %-3s %-4s %-6s\n";
// Imprime los encabezados de las columnas
        StringBuilder sb = new StringBuilder();
        sb.append(String.format(formato, "Minuto", "FC", "Satur.", "TAS", "TAD", "MMII", "Disnea"));
// Imprime una línea separadora
        sb.append("------------------------------------\n");

        for (dataMinuto data : dataList) {
            // Agrega cada valor en su columna correspondiente a la cadena
            sb.append(String.format(formato, data.getMinuto(), data.getFC(), data.getSaturacion(), data.getTAS(),
                    data.getTAD(), data.getMMII(), data.getDisnea()));
        }

// Establece la cadena de texto construida en el TextView

        txRep.setText(sb.toString());


        String resu="";
        int aux=0;
        /*
        for (dataMinuto objeto : dataList) {

            switch (aux){
                case 0:resu+=objeto.getMinuto()+"          ";break;
                case 1:resu+=objeto.getMinuto()+"        ";break;
                case 2:resu+=objeto.getMinuto()+"         ";break;
                case 3:resu+=objeto.getMinuto()+"        ";break;
                case 4:resu+=objeto.getMinuto()+"   ";break;
                case 5:resu+=objeto.getMinuto()+"     ";break;
                case 6:resu+=objeto.getMinuto()+"    ";break;
                case 7:resu+=objeto.getMinuto()+"       ";break;
                case 8:resu+=objeto.getMinuto()+"       ";break;
                case 9:resu+=objeto.getMinuto()+"       ";break;
                default:resu+=objeto.getMinuto()+"       ";break;

            }
            aux++;

            resu+=objeto.getFC()+"       ";
            resu+= objeto.getSaturacion()+"        ";
            resu+= objeto.getTAS()+"        ";
            resu+= objeto.getTAD()+"        ";
            resu+= objeto.getMMII()+"        ";
            resu+= objeto.getDisnea()+"\n";

        }*/



        dialog.show();
        dialog.setCanceledOnTouchOutside(true);
    }



    private void GuardarResult() {
        String segundosStr = getIntent().getExtras().getString("duracionDetencion");
        int segundos = Integer.parseInt(segundosStr);
        String duringDetencion = convertirSegundosAMinutos(segundos);
        
        SharedPreferences preferences=getSharedPreferences("result",MODE_PRIVATE);
        SharedPreferences.Editor editor=preferences.edit();
        //double FCMT__,FCMAlcanzado__,sesentaFC__,ochentaFC__,DIF__FC,FC__Recp;
        //    double TA__,Dif__Sat;
        //    double Troster__,PercentDista__;
        //    String Formula;
        editor.putString("FCMT__",FCMT__+"");
        editor.putString("FCMAlcanzado__",FCMAlcanzado__+"");
        editor.putString("sesentaFC__",sesentaFC__+"");
        editor.putString("ochentaFC__",ochentaFC__+"");
        editor.putString("DIF__FC",DIF__FC+"");
        editor.putString("FC__Recp",FC__Recp+"");
        editor.putString("TA__",TA__+"");
        editor.putString("Dif__Sat",Dif__Sat+"");
        editor.putString("Troster__",Troster__+"");

        editor.putString("VO2MAX__",VO2__+"");
        editor.putString("METS__",METS__+"");

        editor.putString("Formula",Formula);
        editor.putString("PercentDista__",PercentDista__+"");
        editor.putString("NoVueltas__",getIntent().getExtras().getString("noVueltas"));
        editor.putString("NoDetencion",getIntent().getExtras().getString("noDetencion"));
        txtNoDeten.setText(getIntent().getExtras().getString("noDetencion"));
        txtTiempoDeten.setText(duringDetencion);
        editor.putString("Duracion_Detencion",duringDetencion);


        editor.commit();


    }

    private String convertirSegundosAMinutos(int segundos) {
        int minutos = segundos / 60;
        int segundosRestantes = segundos % 60;
        return minutos + ":" + segundosRestantes;
    }

    private void ShowPaciente() {
        int Altura=0;
        int altCm=0;
        int peso=0;

        SharedPreferences preferences=getSharedPreferences("usur",MODE_PRIVATE);
        try{peso=Integer.parseInt(preferences.getString("peso","none"));}catch (Exception e){}
        txtNombreP.setText(preferences.getString("nombre","none"));
        txtEdadP.setText("Edad: "+preferences.getString("edad","none"));
        txtPesoP.setText(peso+" Kg");

        txtCedulaP.setText(preferences.getString("num_documento","none"));
        txtFechaNP.setText(preferences.getString("fecha_nacimiento","none"));
        txtFCMTP.setText("FC Teo "+FCMT__);

        try {
            Altura= Integer.parseInt(preferences.getString("Talla-mts","none"));

        }catch (Exception e){}
        try {
            altCm= Integer.parseInt(preferences.getString("Talla-cm","none"));
        }catch (Exception e){}
        Altura=Altura*100;
        Altura=Altura+altCm;
        double alturin=Altura;
        txtAlturaP.setText(Altura+" Cm");

        alturin=Altura/100.0;
        double auxi=(alturin*alturin);
        double imc=peso/auxi;

        DecimalFormat df = new DecimalFormat("#.##");
        double roundedNumber = Double.parseDouble(df.format(imc));

        txtIMCP.setText("IMC"+roundedNumber);

    }

    private void CalculoAerobica() {
        FormulasaUsar formulita=new FormulasaUsar();
        List<String> lista = new ArrayList<String>();
        lista=formulita.aerobica(this);
        try {
            txtVOmax.setText(lista.get(0));
        }catch (Exception e){}
        try {
            txtMETs.setText(lista.get(1));
        }catch (Exception e){}
        //VO2__,METS__
        try{VO2__= Double.parseDouble(lista.get(0));}catch (Exception e){}
        try{METS__=Double.parseDouble(lista.get(1));}catch (Exception e){}



    }

    private void CalculoRecorrido() {

        //Troster__,PercentDista__;
        //    String Formula;

        FormulasaUsar formulita=new FormulasaUsar();
        txttroster.setText(formulita.calculaFormula(this));
        try{Troster__= Double.parseDouble(formulita.calculaFormula(this));}catch (Exception e){}

        textoFormula.setText(formulita.nombreForm(this));
        Formula=formulita.nombreForm(this);
        txtperceDistancia.setText(formulita.percentDistance(this,formulita.calculaFormula(this))+" %");
        try{PercentDista__= Double.parseDouble(formulita.percentDistance(this,formulita.calculaFormula(this)));}catch (Exception e){}
    }

    private void CalculoSat(ArrayList<Integer> sat) {
        int satMinimaEncontrada = Integer.MAX_VALUE;
        int satMaximaEncontrada= Integer.MIN_VALUE;
        // Inicializamos la variable con el valor mínimo posible de un entero
        System.out.println("Max Value"+satMinimaEncontrada);
        for (int numero : sat) {
            if (numero < satMinimaEncontrada) {
                satMinimaEncontrada = numero;
            }
            if(numero>satMaximaEncontrada){
                satMaximaEncontrada=numero;
            }
        }
        //TA__,Dif__Sat
        try {
            DIF_Sat.setText((satMinimaEncontrada-satMaximaEncontrada)+"");

        }catch (Exception e){

        }
        try{Dif__Sat=satMinimaEncontrada-satMaximaEncontrada;}catch (Exception e){}

    }



    private void CalculoFC(ArrayList<Integer> l) {


        int numeroMayor = Integer.MIN_VALUE; // Inicializamos la variable con el valor mínimo posible de un entero
        for (int numero : l) {
            if (numero > numeroMayor) {
                numeroMayor = numero;
            }
        }
        int FCMaximaEncontrada=  numeroMayor;
        SharedPreferences preferences=getSharedPreferences("usur",MODE_PRIVATE);
        double edadint=Integer.parseInt(preferences.getString("edad","1"));
        double roundedNumber = Math.round(edadint * 100.0) / 100.0;
        edadint=roundedNumber;
        edadint =208.75-(0.73*edadint);
        //FCMT&,FCMAlcanzado__,sesentaFC__,ochentaFC__,DIF__FC,FC__Recp;


        DecimalFormat df3 = new DecimalFormat("#.##");
        String rounded = df3.format(edadint);
        try {
            edadint=Double.parseDouble(rounded);
        }catch (Exception e){

        }

        FCMT__=edadint;
        FC_maximo.setText(edadint+"");
        double aux=(FCMaximaEncontrada/edadint)*100;

        DecimalFormat df = new DecimalFormat("#.##");


        FCMAlcanzado__=aux;
        PercentMaxFC.setText(Math.round(aux)+""+" %");

        double sesenta65 = Double.parseDouble(df.format(0.65*edadint));
        seispercent.setText(sesenta65+"");
        sesentaFC__=sesenta65;
        double ochenta85 = Double.parseDouble(df.format(0.85*edadint));
        ochopercent.setText((ochenta85)+"");
        ochentaFC__=ochenta85;
        try {

            DIFFC.setText((FCMaximaEncontrada- l.get(0))+"");
            DIF__FC=FCMaximaEncontrada- l.get(0);
        }catch (Exception e){

        }
        FC_Recp.setText(primerMinDesc(dataList));
        try {
            FC__Recp= Double.parseDouble(primerMinDesc(dataList));
        }catch (Exception e){}







    }

    private String primerMinDesc(ArrayList<dataMinuto> dataList) {
        int minDesc=0,desca=0;
        dataMinuto person ;
        boolean bandera1=true, bandera2=true;
        Iterator iter = dataList.iterator();

        while(iter.hasNext()){
            person = (dataMinuto) iter.next(); /* Cast del Objeto a la Clase Persona*/

            if(person.getMinuto().equals("Fin Prueba")){

                try {
                    desca=Integer.parseInt(person.getFC());

                }catch (Exception e){

                    bandera1=false;
                }

            };
            if(person.getMinuto().equals("M 1 Desc")){

                try {
                    minDesc=Integer.parseInt(person.getFC());

                }catch (Exception e){

                    bandera2=false;
                }

            };


        }

        String result="0";
        if(bandera1==true&&bandera2==true){
            result=minDesc-desca+"";
            return result;
        }

        return result;
    }

    private void CalculoPresora(ArrayList<Integer> Ta) {
        int TaMaximaEncontrada = Integer.MIN_VALUE; // Inicializamos la variable con el valor mínimo posible de un entero
        for (int numero : Ta) {
            if (numero > TaMaximaEncontrada) {
                TaMaximaEncontrada = numero;
            }
        }
        //TA__,Dif__Sat
        try {
            DIF_Presion.setText((TaMaximaEncontrada-Ta.get(0))+"");

        }catch (Exception e){

        }
        try{TA__=TaMaximaEncontrada-Ta.get(0);}catch (Exception e){}

    }



    private void salir(){
        final Dialog dialog=new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.exportar_layout);


        Button buttonSi=dialog.findViewById(R.id.ButtonSi);
        Button buttonNo=dialog.findViewById(R.id.ButtonNo);
        buttonSi.setOnClickListener(view -> {
            Intent intent = new Intent(this,MainActivity.class);
            startActivity(intent);
        });
        buttonNo.setOnClickListener(view -> {
            finishAffinity();
        });
        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations =R.style.DialogAnimations;
        dialog.getWindow().setGravity(Gravity.BOTTOM);
    }

    private void ExportarExcel() {
        SharedPreferences preferencesuser = getSharedPreferences("usur", MODE_PRIVATE);
        SharedPreferences preferencesresult = getSharedPreferences("result", MODE_PRIVATE);
        Workbook wb = new HSSFWorkbook();
        Cell cell = null;

        Sheet sheet = null;
        sheet = wb.createSheet("Datos paciente");

        CellStyle style = wb.createCellStyle();
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);


        Row row = null;

        row = sheet.createRow(0);
        cell = row.createCell(0);
        cell.setCellValue("Paciente");

        sheet.createRow(1);
        cell = row.createCell(1);
        cell.setCellValue(preferencesuser.getString("nombre", "none"));

        row = sheet.createRow(1);
        cell = row.createCell(0);
        cell.setCellValue("Tipo de documento");

        cell = row.createCell(1);
        cell.setCellValue(preferencesuser.getString("tipo_documento", "none"));

        row = sheet.createRow(2);
        cell = row.createCell(0);
        cell.setCellValue("N° Documento");

        cell = row.createCell(1);
        cell.setCellValue(preferencesuser.getString("num_documento", "none"));

        row = sheet.createRow(3);
        cell = row.createCell(0);
        cell.setCellValue("Fecha de nacimiento");

        cell = row.createCell(1);
        cell.setCellValue(preferencesuser.getString("fecha_nacimiento", "none"));

        row = sheet.createRow(4);
        cell = row.createCell(0);
        cell.setCellValue("Edad");

        cell = row.createCell(1);
        cell.setCellValue(preferencesuser.getString("edad", "none"));

        row = sheet.createRow(5);
        cell = row.createCell(0);
        cell.setCellValue("Sexo");

        cell = row.createCell(1);
        cell.setCellValue(preferencesuser.getString("sexo", "none"));

        row = sheet.createRow(6);
        cell = row.createCell(0);
        cell.setCellValue("Medicamentos");

        cell = row.createCell(1);
        cell.setCellValue(preferencesuser.getString("Medicamentos", "none"));

        row = sheet.createRow(7);
        cell = row.createCell(0);
        cell.setCellValue("Peso");

        cell = row.createCell(1);
        cell.setCellValue(preferencesuser.getString("peso", "none"));

        row = sheet.createRow(8);
        cell = row.createCell(0);
        cell.setCellValue("Talla");

        cell = row.createCell(1);
        cell.setCellValue(preferencesuser.getString("Talla-mts", "none") + "," + preferencesuser.getString("Talla-cm", "none"));

        row = sheet.createRow(9);
        cell = row.createCell(0);
        cell.setCellValue("Formula");

        cell = row.createCell(1);
        cell.setCellValue(preferencesuser.getString("formula", "none"));

        row = sheet.createRow(10);
        cell = row.createCell(0);
        cell.setCellValue("distancia");

        cell = row.createCell(1);
        cell.setCellValue(preferencesuser.getString("distancia", "none"));

        row = sheet.createRow(11);
        cell = row.createCell(0);
        cell.setCellValue("");

        cell = row.createCell(1);
        cell.setCellValue("");

        row = sheet.createRow(12);
        cell = row.createCell(0);
        cell.setCellValue("FC-teórica");

        cell = row.createCell(1);
        cell.setCellValue(preferencesuser.getString("FC-teorica", "none"));

        row = sheet.createRow(13);
        cell = row.createCell(0);
        cell.setCellValue("FCM Alcanzada");

        cell = row.createCell(1);
        cell.setCellValue(preferencesresult.getString("FCMAlcanzado__", "none"));

        row = sheet.createRow(14);
        cell = row.createCell(0);
        cell.setCellValue("65%");

        cell = row.createCell(1);
        cell.setCellValue(preferencesresult.getString("sesentaFC__", "none"));

        row = sheet.createRow(15);
        cell = row.createCell(0);
        cell.setCellValue("85%");

        cell = row.createCell(1);
        cell.setCellValue(preferencesresult.getString("ochentaFC__", "none"));

        row = sheet.createRow(16);
        cell = row.createCell(0);
        cell.setCellValue("DIF__FC");

        cell = row.createCell(1);
        cell.setCellValue(preferencesresult.getString("DIF__FC", "none"));


        row = sheet.createRow(17);
        cell = row.createCell(0);
        cell.setCellValue("FC__Recp");

        cell = row.createCell(1);
        cell.setCellValue(preferencesresult.getString("FC__Recp", "none"));

        row = sheet.createRow(18);
        cell = row.createCell(0);
        cell.setCellValue("");

        cell = row.createCell(1);
        cell.setCellValue("");

        row = sheet.createRow(19);
        cell = row.createCell(0);
        cell.setCellValue("DIF_TA");

        cell = row.createCell(1);
        cell.setCellValue(preferencesresult.getString("TA__", "none"));

        row = sheet.createRow(20);
        cell = row.createCell(0);
        cell.setCellValue("DIF_SAT");

        cell = row.createCell(1);
        cell.setCellValue(preferencesresult.getString("Dif__Sat", "none"));

        row = sheet.createRow(21);
        cell = row.createCell(0);
        cell.setCellValue("");

        cell = row.createCell(1);
        cell.setCellValue("");

        row = sheet.createRow(22);
        cell = row.createCell(0);
        cell.setCellValue("PercentDista");

        cell = row.createCell(1);
        cell.setCellValue(preferencesresult.getString("PercentDista__", "none"));

        row = sheet.createRow(23);
        cell = row.createCell(0);
        cell.setCellValue("");

        cell = row.createCell(1);
        cell.setCellValue("");

        row = sheet.createRow(24);
        cell = row.createCell(0);
        cell.setCellValue("Troster");

        cell = row.createCell(1);
        cell.setCellValue(preferencesresult.getString("Troster__", "none"));

        row = sheet.createRow(25);
        cell = row.createCell(0);
        cell.setCellValue("VO2__");

        cell = row.createCell(1);
        cell.setCellValue(preferencesresult.getString("Troster__", "none"));

        row = sheet.createRow(26);
        cell = row.createCell(0);
        cell.setCellValue("METS_");

        cell = row.createCell(1);
        cell.setCellValue(preferencesresult.getString("Troster__", "none"));

        cell = sheet.getRow(0).getCell(0);
        // Establecer ancho de columna
        sheet.setColumnWidth(cell.getColumnIndex(), 5000);

        cell = sheet.getRow(0).getCell(1);
        // Establecer ancho de columna
        sheet.setColumnWidth(cell.getColumnIndex(), 5000);

        // Obtener celda o rango de celdas
        CellRangeAddress range = CellRangeAddress.valueOf("A1:B22");
        for (int row2 = range.getFirstRow(); row2 <= range.getLastRow(); row2++) {
            for (int col = range.getFirstColumn(); col <= range.getLastColumn(); col++) {
                cell = sheet.getRow(row2).getCell(col);

                // Aplicar estilo de borde a la celda
                cell.setCellStyle(style);
            }
        }

        File file = new File(getExternalFilesDir(null), preferencesuser.getString("nombre", "none") + "_" + preferencesuser.getString("num_documento", "none") + ".xls");
        FileOutputStream outputStream = null;

        try {
            outputStream = new FileOutputStream(file);
            wb.write(outputStream);
            Toast.makeText(getApplicationContext(), "Documento exportado correctamente", Toast.LENGTH_LONG).show();
        } catch (java.io.IOException e) {
            e.printStackTrace();

            Toast.makeText(getApplicationContext(), "Hubo un error en la exportación", Toast.LENGTH_LONG).show();
            try {
                outputStream.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        JavaMailAPI javaMailAPI = new JavaMailAPI(this, preferencesuser.getString("CorreoP", "none") + "", "Prueba paciente " + preferencesuser.getString("nombre", "none"), "Resultados obtenidos", getFilePath(), preferencesuser.getString("nombre", "none") + "_" + preferencesuser.getString("num_documento", "none") + ".xls");
        javaMailAPI.execute();
        salir();
    }

    private String getFilePath(){
        SharedPreferences preferencesuser=getSharedPreferences("usur",MODE_PRIVATE);
        ContextWrapper contextWrapper = new ContextWrapper(getApplicationContext());
        File pdfDirectory = contextWrapper.getExternalFilesDir(null);
        File file = new File(pdfDirectory, preferencesuser.getString("nombre","none")+"_"+preferencesuser.getString("num_documento","none")+".xls");
        return file.getPath();
    }

}