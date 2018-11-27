package spaceInvaders.Activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.kikeajani.spaceinvadersbeta.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;

public class PuntuacionActivity extends AppCompatActivity {

    private TextView mensajeFin;
    private TextView puntuaciones;
    private TextView punt500;
    private Button reinicio;
    private int puntos;
    private String nombre;

    MediaPlayer musica;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fin_del_juego);
        nombre = getIntent().getExtras().getString("nombre");
        puntos = Integer.parseInt(getIntent().getExtras().getString("puntos"));
        puntuaciones = (TextView) findViewById(R.id.puntuaciones);
        mensajeFin = (TextView) findViewById(R.id.puntuacion);
        punt500 = (TextView)findViewById(R.id.textPunt500);
        reinicio= (Button)findViewById(R.id.reinicio);
        mensajeFin.setText(Integer.toString(puntos));

        SharedPreferences preferencias = getSharedPreferences("Ranking", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferencias.edit();
        //editor.clear(); //reinicia el ranking cada partida
        if (puntos < 10) {
            editor.putString("0000" + " - " + nombre, "");
        } else if (puntos < 100) {
            editor.putString("00" + String.valueOf(puntos) + " - " + nombre, "");

        } else if (puntos < 1000){
            editor.putString("0" + String.valueOf(puntos) + " - " + nombre, "");

        } else {
            editor.putString(String.valueOf(puntos) + " - " + nombre, "");
        }
        editor.apply();

        if (puntos<500){
            reinicio.setVisibility(View.INVISIBLE);
        }

        Iterator it = preferencias.getAll().keySet().iterator();
        ArrayList ord = new ArrayList();
        while (it.hasNext()){
            String res = (String)it.next();
            ord.add(res);
        }
        Collections.sort(ord, new Comparator<String>() {
            public int compare(String o1, String o2) {
                return extractInt(o2)- extractInt(o1);
            }

            int extractInt(String s) {
                String num = s.replaceAll("\\D", "");
                return num.isEmpty() ? 0 : Integer.parseInt(num);
            }
        });
        String s = "";
        int cont = 0;
        for (Object i : ord) {
            String nuevo = String.valueOf(i);
            if (nuevo.equals(String.valueOf(puntos) + " - " + nombre)) {
                s += ("-->  " + String.valueOf(i) + "\n");
            } else {
                s += ("      " + String.valueOf(i) + "\n");
            }
            cont++;
            if (cont > 9) {
                break;
            }
        }
        puntuaciones.setText(s);

        musica = MediaPlayer.create(this, R.raw.supermariobros3);
        musica.start();
        musica.setLooping(true);
    }

    public void reiniciar(View view){
        while (true) {
                try {
                    if ((getIntent().getExtras().getString("tipoJuego")).equals("mayorRebotes")) {
                        musica.stop();
                        Intent mayorRebotes = new Intent(this, MayorActivityRebotes.class);
                        mayorRebotes.putExtra("nombre", nombre);
                        startActivity(mayorRebotes);
                        finish();
                        break;
                    } else if ((getIntent().getExtras().getString("tipoJuego")).equals("mayor")) {
                        musica.stop();
                        Intent mayor = new Intent(this, MayorActivity.class);
                        mayor.putExtra("nombre", nombre);
                        startActivity(mayor);
                        finish();
                        break;
                    } else {
                        musica.stop();
                        Intent menor = new Intent(this, MenorActivity.class);
                        menor.putExtra("nombre", nombre);
                        startActivity(menor);
                        finish();
                        break;
                    }
                } catch (Exception e) {
                }
        }
    }

    public void salir(View view){
        finish();
        System.exit(0);
    }

}
