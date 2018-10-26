package com.example.kikeajani.spaceinvadersbeta;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class FinDelJuegoActivity extends AppCompatActivity {

    private TextView mensajeFin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fin_del_juego);

        String puntos = getIntent().getExtras().getString("puntos");
        mensajeFin = (TextView) findViewById(R.id.puntuacion);
        if ((Integer.parseInt(puntos)) == 3000){
            mensajeFin.setText("ENHORABUENA" + "\n" + puntos + " puntos");
        } else {
            mensajeFin.setText("Mala suerte..." + "\n" + puntos + " puntos");
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
