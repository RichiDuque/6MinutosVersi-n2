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
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.tabs.TabLayout;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
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
    Button BtnAtrasDescanso, BtnExportar;
    TextView FC_maximo,PercentMaxFC,seispercent,ochopercent,DIFFC,FC_Recp;
    TextView DIF_Presion,DIF_Sat;
    TextView textoFormula,txttroster,txtperceDistancia;
    //Paciente
    TextView txtNombreP,txtEdadP,txtAlturaP,txtPesoP,txtIMCP,txtCedulaP,txtFechaNP,txtFCMTP;  ;

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


        BtnAtrasDescanso.setOnClickListener(view -> {
            showResultBoard(dataList);
        });

        BtnExportar.setOnClickListener(view -> {
            salir();
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
        //MaterialButton btnDistance = dialog.findViewById(R.id.buttonDistancia);
        //EditText editDistance= dialog.findViewById(R.id.editDistancia);

        

        dialog.show();
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
        editor.putString("Formula",Formula);
        editor.putString("PercentDista__",PercentDista__+"");
        editor.putString("NoVueltas__",getIntent().getExtras().getString("noVueltas"));
        editor.putString("NoDetencion",getIntent().getExtras().getString("noDetencion"));
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
        txtFCMTP.setText("FC Max T: "+FCMT__);
        try {
            Altura= Integer.parseInt(preferences.getString("Talla-mts","none"));

        }catch (Exception e){}
        try {
            altCm= Integer.parseInt(preferences.getString("Talla-cm","none"));
        }catch (Exception e){}
        Altura=Altura*100;
        Altura=Altura+altCm;
        txtAlturaP.setText(Altura+" Cm");
        Altura=Altura/100;
        double auxi=(Altura*Altura);
        double imc=peso/auxi;

        DecimalFormat df = new DecimalFormat("#.##");
        double roundedNumber = Double.parseDouble(df.format(imc));

        txtIMCP.setText("IMC: "+roundedNumber);

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
        FCMT__=edadint;
        FC_maximo.setText(edadint+"");
        double aux=(FCMaximaEncontrada/edadint)*100;

        double number = 3.14159265359;
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

    private void crearExcel(){
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("MiHoja");

        Row row = sheet.createRow(0);
        Cell cell = row.createCell(0);
        cell.setCellValue("Nombre");

        row = sheet.createRow(1);
        cell = row.createCell(0);
        cell.setCellValue("Juan");

        row = sheet.createRow(2);
        cell = row.createCell(0);
        cell.setCellValue("María");

        File file = new File(getExternalFilesDir(null), "miArchivo.xlsx");
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
            workbook.write(fos);
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}