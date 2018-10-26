package com.example.kikeajani.spaceinvadersbeta;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private EditText anyos;
    private TextView error;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        anyos = (EditText) findViewById(R.id.anyos);
        error = (TextView) findViewById(R.id.error);
    }

    public void ejecutarMayor(View view) {
        Intent mayor = new Intent(this, MayorActivity.class);
        startActivity(mayor);
        finish();
    }

    public void ejecutarMenor(View view) {
        Intent menor = new Intent(this, MenorActivity.class);
        startActivity(menor);
        finish();
    }

    @SuppressLint("SetTextI18n")
    public void preguntarEdad(View view) {
        try {
            String anyos_s = anyos.getText().toString();
            int anyos_int = Integer.parseInt(anyos_s);

            if (anyos_int <= 13) {
                ejecutarMenor(view);
            } else {
                ejecutarMayor(view);
            }
        } catch (Exception e) {
            error.setText("Edad no válida");
        }
    }

}
