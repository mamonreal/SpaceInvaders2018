package com.example.kikeajani.spaceinvadersbeta;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;

public class MayorActivity extends Activity {

    SpaceInvadersJuego spaceInvadersJuego;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Resolución de la pantalla guardada en un Point
        Display display = getWindowManager().getDefaultDisplay();
        Point point = new Point();
        display.getSize(point);

        //La vista será la del juego
        spaceInvadersJuego = new SpaceInvadersJuego(this, point, true,this);
        setContentView(spaceInvadersJuego);
    }

    @Override
    protected void onPause(){
        super.onPause();
        spaceInvadersJuego.pause();
    }

    @Override
    protected void onResume(){
        super.onResume();
        spaceInvadersJuego.resume();
    }

    @Override
    protected void onStop(){
        super.onStop();
        //spaceInvadersJuego.stop();
    }




}
