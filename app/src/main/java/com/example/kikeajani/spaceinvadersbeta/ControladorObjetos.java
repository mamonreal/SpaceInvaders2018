package com.example.kikeajani.spaceinvadersbeta;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.SurfaceHolder;

import java.util.concurrent.ConcurrentHashMap;

public class ControladorObjetos {

    private SpaceInvadersJuego spaceInvadersJuego;

    private ConcurrentHashMap<String, ObjetoVisible> objetosInGame;

    public ControladorObjetos(SpaceInvadersJuego spaceInvadersJuego) {
        this.spaceInvadersJuego = spaceInvadersJuego;
        objetosInGame = new ConcurrentHashMap<String, ObjetoVisible>();
    }

    public void add(String nombre, ObjetoVisible objetoVisible) {
        objetosInGame.put(nombre, objetoVisible);
    }

    public void remove(String name) {
        objetosInGame.remove(name);
    }

    public int getObjetosInGameSize() {
        return objetosInGame.size();
    }

    public ObjetoVisible get(String name) {
        return objetosInGame.get(name);
    }

    public void drawAll(SurfaceHolder ourHolder, Canvas canvas, Paint paint) {
        if (ourHolder.getSurface().isValid()) {
            canvas = ourHolder.lockCanvas();
            canvas.drawColor(Color.argb(255, 0, 0, 0));
            for (ConcurrentHashMap.Entry<String, ObjetoVisible> entry : objetosInGame.entrySet()) {
                ObjetoVisible value = entry.getValue();
                value.draw(canvas, paint);
            }
            //SpaceInvadersJuego.drawScoreBoard(canvas, paint);
            spaceInvadersJuego.drawButtons(canvas, paint);
            ourHolder.unlockCanvasAndPost(canvas);
        }
    }

    public void updateAll() {
        for (ConcurrentHashMap.Entry<String, ObjetoVisible> entry : objetosInGame.entrySet()) {
            ObjetoVisible value = entry.getValue();
            value.update();

        }
    }

}