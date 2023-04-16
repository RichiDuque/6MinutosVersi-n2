package com.venta.a6minutosapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.text.InputFilter;
import android.text.Spanned;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Reloj extends AppCompatActivity {
    ImageButton btnvuelAtras, btnvuelAdelante ;
    Button btndetenciones, btnresultados;
    List<dataMinuto> dataList;
    RecyclerView Recycler;
    TextView textcronometroSeg, textcronometroMin,txtVueltas,txtMinutostitulo,txtNoDetenciones;
    FloatingActionButton btn_Start_Stop;

    private Chronometer chronometer;
    private Button button;

    private boolean isRunning = false; // Variable para indicar si el cronómetro está corriendo
    private long elapsedTime = 0;

    Dialog dialog;

    int noDetenciones;
    long tiempo_Detención;

    EditText edFC,edSat,edTas,edTad,edMI,edDisnea;

    private boolean runningTest = false;
    private boolean runningDescanso = true;
    private int minuto=0;
    private int segundos = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reloj);
        ArrayList<dataMinuto> listadeMinutos = new ArrayList<>();

        if (savedInstanceState != null) {

            String valueFC = savedInstanceState.getString("valueFC");
            String valueSAT = savedInstanceState.getString("valueSAT");
            String valueTAS = savedInstanceState.getString("valueTAS");

            edFC=findViewById(R.id.editTextdeFC);
            edSat=findViewById(R.id.editTextdeSat);
            edTas=findViewById(R.id.editTextdeTas);

            edFC.setText(valueFC);
            edSat.setText(valueSAT);
            edTas.setText(valueTAS);


            listadeMinutos = (ArrayList<dataMinuto>) savedInstanceState.getSerializable("dataMinuto");
            dataList=listadeMinutos;
            putDataIntoRecyclerView(dataList);
        }


        btnvuelAtras=findViewById(R.id.botonizquierdovueltas);
        btnvuelAdelante=findViewById(R.id.botonderechovueltas);
        btndetenciones=findViewById(R.id.ButtonDetencion);
        btn_Start_Stop=findViewById(R.id.button_start_stop);
        btnresultados=findViewById(R.id.ButtonResultados);
        txtMinutostitulo=findViewById(R.id.txtMinutostitulo);
        textcronometroSeg=findViewById(R.id.cronometroSegundos);
        textcronometroMin=findViewById(R.id.cronometroMinutos);
        txtVueltas=findViewById(R.id.txtNoVueltas);
        //nuevo crono
        chronometer = findViewById(R.id.chronometer);
        txtNoDetenciones=findViewById(R.id.txtNoDetenciones);
        button = findViewById(R.id.ButtonDetencion);
        //Dialogo
        dialog=new Dialog(Reloj.this);


        //TextView de las variables
        edFC=findViewById(R.id.editTextdeFC);
        edSat=findViewById(R.id.editTextdeSat);
        edTas=findViewById(R.id.editTextdeTas);
        edTad=findViewById(R.id.editTextdeTad);
        edMI=findViewById(R.id.editTextdeMI);
        edDisnea=findViewById(R.id.editTextdeDisnea);

        Recycler=findViewById(R.id.Recycler);
        dataList=new ArrayList<>();
        textcronometroMin.setText(minuto+"");
        textcronometroSeg.setText(segundos+"0");

        InputFilter inputFilterFC = new InputFilter() {

            int min = 0;
            int max = 400;

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

        edFC.setFilters(new InputFilter[]{inputFilterFC});

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isRunning) { // Si el cronómetro está corriendo
                    chronometer.stop(); // Detener el cronómetro
                    elapsedTime = SystemClock.elapsedRealtime() - chronometer.getBase(); // Calcular tiempo transcurrido
                    isRunning = false; // Indicar que el cronómetro no está corriendo
                    button.setText("Detención"); // Cambiar el texto del botón
                    long elapsedSeconds = elapsedTime / 1000;
                    button.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.normal)));
                    System.out.println("Tiempo transcurrido: " + elapsedSeconds); // Imprimir tiempo transcurrido en consola
                    tiempo_Detención=elapsedSeconds;
                } else { // Si el cronómetro no está corriendo
                    chronometer.setBase(SystemClock.elapsedRealtime() - elapsedTime); // Restablecer el cronómetro
                    chronometer.start(); // Iniciar el cronómetro
                    isRunning = true; // Indicar que el cronómetro está corriendo
                    noDetenciones++;
                    txtNoDetenciones.setText(noDetenciones+"");
                    button.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.red)));
                    button.setText("Continua"); // Cambiar el texto del botón
                }
            }
        });

        btn_Start_Stop.setOnClickListener(view->{
            dataList.clear();
            //Si el juego está funcionanado
            if (runningTest) {

                //Interrumpe el juego
                minuto = 6;//Asigna valor que indica terminación del juego
                runningTest = false;//Indica que el juego no está funcionando
                runningDescanso=false;
                setTextChronometer(0);//Asigna valor de cero al cronómetro para visualizar terminación
                setTextChronometerMinutos(0);
                changeIcon(MediaType.PLAY);//Modifica el ícono que se encontraba en STOP a PLAY
            } else {
                //Comienza el juego
                runningDescanso=true;
                //setTextCountPress();//Asigna el contador al texto
                runningGame();//Inicia la ejecución del juego
            }
        });
        btnresultados.setVisibility(View.INVISIBLE);
        btnvuelAtras.setOnClickListener(view -> {
            int vueltas=Integer.parseInt(txtVueltas.getText().toString());
            if (vueltas != 0) {
                vueltas--;
            }

            txtVueltas.setText(vueltas+"");
        });
        btnvuelAdelante.setOnClickListener(view -> {
            int vueltas=Integer.parseInt(txtVueltas.getText().toString());
            vueltas++;
            txtVueltas.setText(vueltas+"");
        });
        changeCongratulationsVisibility(View.INVISIBLE);
        btnresultados.setOnClickListener(view -> {
            if (isRunning) {
                Toast.makeText(this, "Pare el cronómetro de detenciones", Toast.LENGTH_SHORT).show();
            }else{
                if (edFC.getText().toString().equals("")||edSat.getText().toString().equals("")||edTas.getText().toString().equals("")) {
                    Toast.makeText(this, "Escribe los valores del minuto 5 de descanso", Toast.LENGTH_SHORT).show();
                }else{
                    recogedataListDescanso();
                    ArrayList<dataMinuto> listaAdress = new ArrayList<dataMinuto>(dataList);
                    Intent intent = new Intent(this, Resultados.class);
                    intent.putExtra("noDetencion",noDetenciones+"");
                    intent.putExtra("duracionDetencion",tiempo_Detención+"");
                    intent.putExtra("listaObjetos", listaAdress);
                    intent.putExtra("noVueltas",txtVueltas.getText().toString());
                    startActivity(intent);
                }
            }


        });

        if(savedInstanceState!=null){
            putDataIntoRecyclerView((List<dataMinuto>) savedInstanceState.getSerializable("dataMinuto"));
        }

        edFC.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_NEXT) { // verifica si se presionó la tecla "Enter"
                    edTas.requestFocus(); // mueve el foco al siguiente EditText
                    return true;
                }
                return false;
            }
        });
        edTas.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_NEXT) { // verifica si se presionó la tecla "Enter"
                    edMI.requestFocus(); // mueve el foco al siguiente EditText
                    return true;
                }
                return false;
            }
        });
        edMI.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_NEXT) { // verifica si se presionó la tecla "Enter"
                    edSat.requestFocus(); // mueve el foco al siguiente EditText
                    return true;
                }
                return false;
            }
        });
        edSat.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_NEXT) { // verifica si se presionó la tecla "Enter"
                    edTad.requestFocus(); // mueve el foco al siguiente EditText
                    return true;
                }
                return false;
            }
        });
        edTad.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_NEXT) { // verifica si se presionó la tecla "Enter"
                    edDisnea.requestFocus(); // mueve el foco al siguiente EditText
                    return true;
                }
                return false;
            }
        });



    }




    @SuppressLint("ResourceAsColor")
    private void runningGame() {

        //putDataIntoRecyclerView(dataList);
        cleanVariables();



        //Drawable shape = (Drawable) imgContainer.getBackground();
        //shape.setColorFilter(Color.parseColor(colorToApply), android.graphics.PorterDuff.Mode.SRC);
        //Se define hilo de procesamiento
        new Thread(() -> {
            segundos=0;
            minuto=0;
            //Ciclo de repetición para controlar los segundos
            setTextChronometerMinutos(minuto);
            recogedataList();
            putDataIntoRecyclerView(dataList);
            while (minuto != 6) {
                runningTest = true;
                //Valor de true indica que el juego se encuentra activado (descontando segundos)
                if(segundos==20 & minuto>0){
                    recogedataList();
                    putDataIntoRecyclerView(dataList);

                    cleanVariables();
                }
                if(segundos==60){
                    segundos=0;
                    minuto++;
                    setTextChronometerMinutos(minuto);
                    mostrarMensaje(minuto);

                }//Aumenta el número de segundos
                setTextChronometer(segundos);//Asigna valor de segundos al cronómetro
                segundos++;

                try {
                    Thread.sleep(100);//Interrumpe por un segundo el ciclo de repetición, dando la percepción de tiempo
                } catch (InterruptedException e) {
                    e.printStackTrace();//Impresión por consola del error generado durante el sleep
                }
            }

            //Termina el juego cuando el tiempo llega a cero
            if (minuto == 6) {
                //Esto fue lo que quité para el reloj de descanso, despues del min 6
                //Juego finalizado

                setTextChronometer(0);
                //pruebin();
                setTextChronometerMinutos(0);//Actualización del cronómetro
                if(runningDescanso)
                    runningDescanso();


                //changeIcon(MediaType.PLAY);//Modificación del ícono para iniciar un nuevo juego
                //checkTopScore();//Valida puntaje
            }

        }).start();


        changeIcon(MediaType.STOP);



    }

    public void mostrarMensaje(int minutosTranscurridos) {

        Handler handler = new Handler(Looper.getMainLooper());
        int minutosRestantes = 6 - minutosTranscurridos;
        String mensaje = "Ya ha pasado " + minutosTranscurridos + " minutos, te faltan " + minutosRestantes;
        if (minutosTranscurridos == 6) {
            mensaje = "Ya finalizó la prueba";
        }

// Luego, en cualquier otro hilo, puedes mostrar el Toast utilizando el objeto Handler creado anteriormente:
        String finalMensaje = mensaje;
        handler.post(new Runnable() {
            @Override
            public void run() {
                Toast toast = Toast.makeText(getApplicationContext(), finalMensaje, Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL, 0, 0);
                toast.show();
            }
        });

    }

    private void showDialogDistance() {
        dialog.setContentView(R.layout.custom_dialog_layout);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(false);
        MaterialButton btnDistance = dialog.findViewById(R.id.buttonDistancia);
        EditText editDistance= dialog.findViewById(R.id.editDistancia);

        btnDistance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences preferences=getSharedPreferences("usur",MODE_PRIVATE);
                SharedPreferences.Editor editor=preferences.edit();
                editor.putString("distancia",editDistance.getText().toString());
                editor.commit();
                dialog.dismiss();
            }
        });

        dialog.show();
    }
    private void showDialogDetencion() {
        dialog.setContentView(R.layout.custom_dialog_layout);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(false);
        MaterialButton btnDistance = dialog.findViewById(R.id.buttonDistancia);
        EditText editDistance= dialog.findViewById(R.id.editDistancia);

        btnDistance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences preferences=getSharedPreferences("usur",MODE_PRIVATE);
                SharedPreferences.Editor editor=preferences.edit();
                editor.putString("distancia",editDistance.getText().toString());
                editor.commit();
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void pruebin() {
        for (int i = 0; i < dataList.size(); i++) {
            System.out.println(dataList.get(i).getFC());
            System.out.println(dataList.get(i).getSaturacion());
            System.out.println("///////////");
        }

    }

    private void cleanVariables() {
        edFC.post(()->{edFC.setText("");});
        edSat.post(()->{edSat.setText("");});
        edTas.post(()->{edTas.setText("");});
        edTad.post(()->{edTad.setText("");});
        edMI.post(()->{edMI.setText("");});
        edDisnea.post(()->{edDisnea.setText("");});

    }

    private void recogedataList() {
        String count="";
        dataMinuto daMin =new dataMinuto();
        if(runningTest==true){
            switch (textcronometroMin.getText().toString()){
                case "1":daMin.setMinuto("Min Uno");count="Escriba ahora los datos para el 2Min";break;
                case "2":daMin.setMinuto("Min Dos");count="Escriba ahora los datos para el 3Min";break;
                case "3":daMin.setMinuto("Min Tres");count="Escriba ahora los datos para el 4Min";break;
                case "4":daMin.setMinuto("Min Cuatro");count="Escriba ahora los datos para el 5Min";break;
                case "5":daMin.setMinuto("Min Cinco");count="Escriba ahora los datos para Fin Prueba";break;
                case "6":daMin.setMinuto("Fin de la Prueba");count="Datos Fin de la Prueba";break;
                case "7":daMin.setMinuto("Min Seis");count="Datos para el M Seis";break;
                default:daMin.setMinuto("Min Seis");count="Datos para el M Seiso";break;
            };
        }else{
            daMin.setMinuto("Reposo");
            count="Escriba ahora los datos para el 1Min";
        }
        String finalCount = count;
        txtMinutostitulo.post(() -> {
            String textCount = String.valueOf(finalCount);//Asigna al contador la palabra 'segundos'
            txtMinutostitulo.setText(textCount);//Modifica el texto del cronómetro
        });


        if(edFC.getText().toString().isEmpty()){
            daMin.setFC("-");
        }else{
            daMin.setFC(edFC.getText().toString());
        }
        if(edSat.getText().toString().isEmpty()){
            daMin.setSaturacion("-");
        }else{
            daMin.setSaturacion(edSat.getText().toString());
        }
        if(edTas.getText().toString().isEmpty()){
            daMin.setTAS("-");
        }else{
            daMin.setTAS(edTas.getText().toString());
        }
        if(edTad.getText().toString().isEmpty()){
            daMin.setTAD("-");
        }else{
            daMin.setTAD(edTad.getText().toString());
        }
        if(edMI.getText().toString().isEmpty()){
            daMin.setMMII("-");
        }else{
            daMin.setMMII(edMI.getText().toString());
        }
        if(edDisnea.getText().toString().isEmpty()){
            daMin.setDisnea("-");
        }else{
            daMin.setDisnea(edDisnea.getText().toString());
        }
            dataList.add(daMin);

    }

    private void putDataIntoRecyclerView(List<dataMinuto> dataList) {


        Recycler.post(()->{
            minutoAdapter adapery=new minutoAdapter(this, dataList);
            Recycler.setLayoutManager(new LinearLayoutManager(this));

            Recycler.setAdapter(adapery);
        });

    }

    private void setTextChronometer(final int count) {
        //Para modificar un TextView desde un hilo, es necesario utilizar POST
        textcronometroSeg.post(() -> {
            String textCount = String.valueOf(count);
            if(count<10){
                textCount="0"+textCount;
            }//Asigna al contador la palabra 'segundos'
            textcronometroSeg.setText(textCount);//Modifica el texto del cronómetro
        });
    }
    private void setTextChronometerMinutos(final int count) {
        //Para modificar un TextView desde un hilo, es necesario utilizar POST
        textcronometroMin.post(() -> {
            String textCount = String.valueOf(count);//Asigna al contador la palabra 'segundos'
            textcronometroMin.setText(textCount);//Modifica el texto del cronómetro
        });
    }
    private void changeIcon(final MediaType type) {
        //Componente asociado al Layout
        final FloatingActionButton startButton = findViewById(R.id.button_start_stop);
        //Se define un Contex correspondiente al actual para poder modificar los elementos del Layout desde un hilo de procesamiento
        final Context context = getBaseContext();
        if (startButton != null) {
            //A través de POST se puede modificar el contenido del Layout
            startButton.post(() -> {
                Drawable icon = null;//Define el componente para asignar el ícono
                switch (type) {
                    //Se asigna el ícono de acuerdo al parámetro (se controla como enumeración)
                    case PLAY:
                        icon = ContextCompat.getDrawable(context, R.drawable.play);
                        break;
                    case STOP:
                        icon = ContextCompat.getDrawable(context, R.drawable.stop);
                        break;
                }
                startButton.setImageDrawable(icon);//Asigna el ícono al componente
            });
        }
    }
    private enum MediaType {
        PLAY, STOP
    }
    private void runningDescanso() {

        //putDataIntoRecyclerView(dataList);
  
        setDraw();

        //Se define hilo de procesamiento
        new Thread(() -> {
            segundos=0;
            minuto=0;

            //Ciclo de repetición para controlar los segundos
            while (minuto != 5 && runningDescanso!=false) {



                //Valor de true indica que el juego se encuentra activado (descontando segundos)
                if(segundos==20){
                    if(minuto==1||minuto==3) {
                        recogedataListDescanso();
                        putDataIntoRecyclerView(dataList);
                        cleanVariables();
                    }


                }
                if(segundos==21 & minuto==0){
                    recogedataListDescanso();
                    putDataIntoRecyclerView(dataList);
                    cleanVariables();
                }

                if(segundos==60){
                    segundos=0;
                    minuto++;
                    setTextChronometerMinutos(minuto);

                }//Aumenta el número de segundos
                setTextChronometer(segundos);//Asigna valor de segundos al cronómetro
                segundos++;

                try {
                    Thread.sleep(100);//Interrumpe por un segundo el ciclo de repetición, dando la percepción de tiempo
                } catch (InterruptedException e) {
                    e.printStackTrace();//Impresión por consola del error generado durante el sleep
                }
            }

            //Termina el juego cuando el tiempo llega a cero
            if (minuto == 5) {
                runningTest = false;//Juego finalizado
                runningDescanso=false;
                setTextChronometer(0);
                changeCongratulationsVisibility(View.VISIBLE);
                //pruebin();
                setFindeJuegoText();
                setTextChronometerMinutos(5);//Actualización del cronómetro
                changeIcon(MediaType.PLAY);//Modificación del ícono para iniciar un nuevo juego
                dataMinuto person ;
                btnVisibleREs();

                Iterator iter = dataList.iterator();
                System.out.println("//////////////////////////////////");
                while(iter.hasNext()){
                    person = (dataMinuto) iter.next(); /* Cast del Objeto a la Clase Persona*/

                }

                //checkTopScore();//Valida puntaje
            }
        }).start();

        changeIcon(MediaType.STOP);

    }

    private void setFindeJuegoText() {
        String finalCount = "Fin de la Prueba, escriba los datos del 5Min Desc";
        txtMinutostitulo.post(() -> {
            String textCount = String.valueOf(finalCount);//Asigna al contador la palabra 'segundos'
            txtMinutostitulo.setText(textCount);//Modifica el texto del cronómetro
        });
    }

    private void btnVisibleREs() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                btnresultados.setVisibility(View.VISIBLE);
            }
        });

    }

    private void recogedataListDescanso() {


        String count="";
        dataMinuto daMin =new dataMinuto();
        switch (textcronometroMin.getText().toString()){
             case "0":daMin.setMinuto("Fin Prueba");count="Escriba los datos para 1Min Desc";break;

             case "1":daMin.setMinuto("M 1 Desc");count="Escriba los datos para 3Min Desc";break;

             case "3":daMin.setMinuto("M 3 Desc");count="Escriba los datos para 5Min Desc";setDistance();break;
             case "5":daMin.setMinuto("M 5 Desc");count="Fin de la prueba";break;
             case "6":daMin.setMinuto("Desc Seis");count="Datos Desc Seis";break;
             default:daMin.setMinuto("Desc Seis");count="Datos Desc Seis";break;
        };
        String finalCount = count;
        txtMinutostitulo.post(() -> {
            String textCount = String.valueOf(finalCount);//Asigna al contador la palabra 'segundos'
            txtMinutostitulo.setText(textCount);//Modifica el texto del cronómetro
        });


        if(edFC.getText().toString().isEmpty()){
            daMin.setFC("-");
        }else{
            daMin.setFC(edFC.getText().toString());
        }
        if(edSat.getText().toString().isEmpty()){
            daMin.setSaturacion("-");
        }else{
            daMin.setSaturacion(edSat.getText().toString());
        }
        if(edTas.getText().toString().isEmpty()){
            daMin.setTAS("-");
        }else{
            daMin.setTAS(edTas.getText().toString());
        }
        if(edTad.getText().toString().isEmpty()){
            daMin.setTAD("-");
        }else{
            daMin.setTAD(edTad.getText().toString());
        }
        if(edMI.getText().toString().isEmpty()){
            daMin.setMMII("-");
        }else{
            daMin.setMMII(edMI.getText().toString());
        }
        if(edDisnea.getText().toString().isEmpty()){
            daMin.setDisnea("-");
        }else{
            daMin.setDisnea(edDisnea.getText().toString());
        }
        dataList.add(daMin);

    }
    private void setDraw(){
        //final TextView text,final String value
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ConstraintLayout imgContainer = findViewById(R.id.fondoReloj);
                String colorToApply = "#405841";
                Drawable shape = (Drawable) imgContainer.getBackground();
                shape.setColorFilter(Color.parseColor(colorToApply), android.graphics.PorterDuff.Mode.SRC);
                //Looper.prepare();
                //Looper.loop();
            }
        });
    }
    private void setDistance(){
        //final TextView text,final String value
        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                showDialogDistance();

            }
        });
    }
    private void changeCongratulationsVisibility(final int visibility) {
        //Se define un componente View por el hilo de procesamiento y la rrestricciones de edición directa
        //final View LayoutSiguiente = findViewById(R.id.LayoutBtnSiguiente);

        //Como se hace uso de un hilo de procesamiento, para modificar el Layput se debe utilizar un POST
       // LayoutSiguiente.post(() ->{
            //Visualiza el Texto que indica puntaje máximo alcanzado
       //     LayoutSiguiente.setVisibility(visibility);
       // });




    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        edFC=findViewById(R.id.editTextdeFC);
        edSat=findViewById(R.id.editTextdeSat);
        edTas=findViewById(R.id.editTextdeTas);
        String valueFC = edFC.getText().toString();
        String valueSAT = edSat.getText().toString();
        String valueTAS = edTas.getText().toString();

        outState.putString("valueFC", valueFC);
        outState.putString("valueSAT", valueSAT);
        outState.putString("valueTAS", valueTAS);
        outState.putSerializable("dataMinuto", (Serializable) dataList);
    }



}