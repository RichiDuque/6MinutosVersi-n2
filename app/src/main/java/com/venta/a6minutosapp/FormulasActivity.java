package com.venta.a6minutosapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.drawable.DrawableCompat;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class FormulasActivity extends AppCompatActivity {
FloatingActionButton btnSuguienteFormulas;

TextView TabItem1,TabItem2;



private int selectedTabNumber=1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formulas);

        btnSuguienteFormulas=findViewById(R.id.btnSuguienteFormulas);
        TabItem1=findViewById(R.id.TabItem1);
        TabItem2=findViewById(R.id.TabItem2);
        Bundle datos = getIntent().getExtras();
        String sexo = datos.getString("sexo");


        getSupportFragmentManager().beginTransaction().setReorderingAllowed(true)
                .replace(R.id.fragmentContainer, FragmentOne.class,null).commit();

        btnSuguienteFormulas.setOnClickListener(view -> {
            Intent intent=new Intent(this,Datos2Activity.class);
            startActivity(intent);
        });

        if (sexo.equals("Hombre")){
            selectTab(1);
        }else{
            selectTab(2);
        }
        TabItem1.setOnClickListener(view -> {
            selectTab(1);

        });
        TabItem2.setOnClickListener(view -> {
            selectTab(2);
        });

    }
    private void selectTab(int tabNumber){
        TextView selectedTextView = null;
        TextView nonSelectedTextView1=null;
        if (tabNumber==1){
            selectedTextView=TabItem1;
            nonSelectedTextView1=TabItem2;

            getSupportFragmentManager().beginTransaction()
                    .setReorderingAllowed(true)
                    .replace(R.id.fragmentContainer,FragmentOne.class,null).commit();
        } else if (tabNumber==2) {
            selectedTextView=TabItem2;
            nonSelectedTextView1=TabItem1;

            getSupportFragmentManager().beginTransaction()
                    .setReorderingAllowed(true)
                    .replace(R.id.fragmentContainer,FragmentTwo.class,null).commit();

        }
        float slideTo=(tabNumber -selectedTabNumber)* selectedTextView.getWidth();
        TranslateAnimation translateAnimation=new TranslateAnimation(0,slideTo,0,0);
        translateAnimation.setDuration(100);
        if(selectedTabNumber==1){
            TabItem1.startAnimation(translateAnimation);
        }else if (selectedTabNumber==2){
            TabItem2.startAnimation(translateAnimation);
        }

        TextView finalSelectedTextView = selectedTextView;
        TextView finalNonSelectedTextView = nonSelectedTextView1;
        translateAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

                finalSelectedTextView.setBackgroundResource(R.drawable.custom_boton);
                Drawable buttonDrawable = finalSelectedTextView.getBackground();
                buttonDrawable = DrawableCompat.wrap(buttonDrawable);
                //the color is a direct color int and not a color resource
                DrawableCompat.setTint(buttonDrawable, Color.parseColor("#61090F"));
                finalSelectedTextView.setBackground(buttonDrawable);
                finalSelectedTextView.setTextColor(Color.WHITE);


                finalNonSelectedTextView.setBackgroundResource(R.drawable.custom_boton);
                //finalNonSelectedTextView.setBackgroundColor(Color.parseColor("#D9D9D9"));
                Drawable buttonDrawable2 = finalNonSelectedTextView.getBackground();
                buttonDrawable = DrawableCompat.wrap(buttonDrawable2);
                //the color is a direct color int and not a color resource
                DrawableCompat.setTint(buttonDrawable2, Color.parseColor("#D9D9D9"));
                finalNonSelectedTextView.setBackground(buttonDrawable2);
                finalNonSelectedTextView.setTextColor(Color.BLACK);

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        selectedTabNumber=tabNumber;

    }
}