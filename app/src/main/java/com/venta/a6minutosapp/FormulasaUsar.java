package com.venta.a6minutosapp;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.List;

public class FormulasaUsar {



    private final double Fórmula_frecuencia_cardíaca_máxima=230;

// enrigh (7.57 × estatura cm) – (5.02 × edad años) – \n(1.76 × peso kg) - 309 m
//        "
    //trooster 218(5.14 x estatura(cm)-532 x edad(años)-[(1.80 x Peso Kg)+(51.31 x Sexo)]\n(Hombres:1 , Mujeres:0)

    //GABBons 686.8 - (2.29 x edad años)-(74.7 x Sexo) \n(hombres: 1, mujeres: 0)
    public String calculaFormula(Context context) {
        double respuesta=0;
        int edad=0;
        int peso=0;
        int sexo=1;
        String sex="HOMBRE";
        SharedPreferences preferences = context.getSharedPreferences("usur",MODE_PRIVATE);
        String prefForm=preferences.getString("formula","none");
        try{
            edad= Integer.parseInt(preferences.getString("edad","0"));
        }catch (Exception e){}
        try{
            peso= Integer.parseInt(preferences.getString("peso","0"));
        }catch (Exception e){}
        try{
            sex=preferences.getString("sexo","HOMBRE");
        }catch (Exception e){}
        if(sex.equals("HOMBRE")){
            sexo=1;
        }else {sexo=0;}
        if(prefForm.equals("Enright")){
            if(sexo==0){
                prefForm="EnMujer";
            }

        }

        switch (prefForm){
            case "Enright":

                respuesta=(5.67 * estatura(context))-(5.02 * edad )-(1.76*peso)-309;


                break;
            case "EnMujer":


                respuesta=(2.11 * estatura(context))-(5.78 * edad )-(2.29*peso)+667;


                break;
            case "Trooster":
                respuesta=218*(5.14 * estatura(context)-532 * edad-((1.80 * peso )+(51.31 * sexo)));


                break;
            case "Gibbons":
                respuesta=(686.8 - (2.29 * edad)-(74.7 * sexo));

                break;
            default:
                respuesta=0;

        }

        double redondeado = Math.round(respuesta * 100.0) / 100.0;
        return redondeado+"";
    }

    private int estatura(Context context) {
        SharedPreferences preferences = context.getSharedPreferences("usur", MODE_PRIVATE);
        int prefmets=0;
        try {
            prefmets = Integer.parseInt(preferences.getString("Talla-mts", "0"));
            prefmets *= 100;

        } catch (Exception e) {

        }
        int prefcm = 0;
        try {
            prefcm = Integer.parseInt(preferences.getString("Talla-cm", "0"));

        } catch (Exception e) {
        }


        int result =prefcm+prefmets;
        return result;

    }

    public String nombreForm(Context context) {
        SharedPreferences preferences = context.getSharedPreferences("usur",MODE_PRIVATE);
        String prefForm=preferences.getString("formula","none");

        return prefForm;

    }

    public String percentDistance(Context context, String s) {
        SharedPreferences preferences = context.getSharedPreferences("usur",MODE_PRIVATE);
        String distancia=preferences.getString("distancia","none");

        double dist=0;
        try {
            dist=Double.parseDouble(distancia);
        }catch (Exception e){

        }
        double trost=0;
        try {
            trost=Double.parseDouble(s);
        }catch (Exception e){

        }
        double result=(dist*100)/trost;


        double redondeado = Math.round(result * 100.0) / 100.0;
        return redondeado+"";
    }

    public List<String> aerobica(Context context) {
        List<String> lista = new ArrayList<String>();
        SharedPreferences preferences = context.getSharedPreferences("usur",MODE_PRIVATE);
        String distancia=preferences.getString("distancia","none");
        double convert=0;
        try {
            convert=Double.parseDouble(distancia);
        }catch (Exception e){

        }
        convert=(4.989+(0.023*convert));
        double redondeado = Math.round(convert * 100.0) / 100.0;
        System.out.println("convert antes: "+redondeado);
        lista.add(redondeado+"");
        convert/=3.5;
        redondeado = Math.round(convert * 100.0) / 100.0;
        System.out.println("convert despues: "+redondeado);
        lista.add(redondeado+"");
        System.out.println("lista"+lista.get(0));


        return lista;
    }
}
