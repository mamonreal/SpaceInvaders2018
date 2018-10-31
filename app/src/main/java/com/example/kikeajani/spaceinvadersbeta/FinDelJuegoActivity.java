package com.example.kikeajani.spaceinvadersbeta;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Iterator;

public class FinDelJuegoActivity extends AppCompatActivity {

    private TextView mensajeFin;
    private TextView puntuaciones;
    private int puntos;
    private String nombre;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fin_del_juego);
        nombre = "Nombre";//TODO Conseguir nombre
        puntos = Integer.parseInt(getIntent().getExtras().getString("puntos"));
        puntuaciones = (TextView) findViewById(R.id.puntuaciones);
        mensajeFin = (TextView) findViewById(R.id.puntuacion);
        if (puntos >= 3000){
            mensajeFin.setText("ENHORABUENA" + "\n" + puntos + " puntos");
        } else {
            mensajeFin.setText("Mala suerte..." + "\n" + puntos + " puntos");
        }
        String ranking = "Ranking:\n";
        String stringRead = "";
        try {
            String fileName = "points";
            PrintWriter pw = new PrintWriter(new OutputStreamWriter(openFileOutput(fileName,MODE_PRIVATE)));//TODO Guardar correctamente
            BufferedReader bfr = new BufferedReader(new InputStreamReader(openFileInput(fileName)));
            boolean puesto = false;
            for(int i=0;i<6;i++){
                String jug = bfr.readLine();
                if(jug!=null) {
                    int punt = Integer.parseInt(bfr.readLine());
                    if (puntos > punt && !puesto) {
                        stringRead += nombre + "\n" + puntos + "\n";
                        ranking+= nombre + " --- " + puntos + "\n";
                        puesto = true;
                    } else {
                        stringRead += jug + "\n" + punt + "\n";
                        ranking+= jug + " --- " + punt + "\n";
                    }
                }else if(!puesto){
                    stringRead += nombre + "\n" + puntos + "\n";
                    ranking+= nombre + " --- " + puntos + "\n";
                    puesto = true;
                }
            }
            bfr.close();
            pw.print(stringRead);
            pw.close();
        } catch (IOException e) {
            ranking = "No se ha leido correctamente el archivo\n"+e.toString();
        } finally {
            puntuaciones.setText(ranking);
        }
    }

    public void reiniciar(View view){
        if ((getIntent().getExtras().getString("tipoJuego")).equals("mayor")) {
            Intent mayor = new Intent(this, MayorActivity.class);
            startActivity(mayor);
            finish();
        } else {
            Intent menor = new Intent(this, MenorActivity.class);
            startActivity(menor);
            finish();
        }
    }

    public void salir(View view){
        finish();
    }
}
