package com.example.kikeajani.spaceinvadersbeta;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;

public class Nave extends ObjetoVisible {

    // object's bitmapDisparar
    Bitmap bitmap;

    // velocity of object
    private float velocidad;

    public enum EstadoNave {Parada, Izquierda, Derecha}

    //Estado de la nave
    private EstadoNave estadoNave;

    //Dimensiones de la pantalla
    private int screenX;
    private int screenY;

    private boolean visible;

    public Nave(Context context, int screenX, int screenY, float velocidad) {

        visible = true;

        // dimensions of object
        int length;
        int height;

        // position of object
        float x;
        float y;

        this.screenX = screenX;
        this.screenY = screenY;

        length = screenX / 10;
        height = screenY / 20;

        x = ((screenX / 2) - (height / 2));
        y = (screenY - ((screenY / 44) * 10));

        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.nave);
        bitmap = Bitmap.createScaledBitmap(bitmap, (int) length, (int) height, false);

        setSize(length, height);
        setPosicionInicial(x, y);

        this.velocidad = (velocidad / 7);

        estadoNave = EstadoNave.Parada;
    }

    @Override
    public void update() {

        //Posicion actual
        PointF posicion = getPosition();

        //Nueva posicion
        if ((estadoNave == EstadoNave.Izquierda) && (posicion.x > 0)) {
            posicion.x = posicion.x - velocidad;
        }
        if ((estadoNave == EstadoNave.Derecha) && (posicion.x < (screenX - getHeight()))) {
            posicion.x = posicion.x + velocidad;
        }

        //Actualizar posicion
        setPosition(posicion.x, posicion.y);
    }

    @Override
    public void draw(Canvas canvas, Paint paint) {
        if (visible) {
            canvas.drawBitmap(bitmap, getPosition().x, (screenY - ((screenY / 43) * 10)), paint);
        }
    }

    public void setDirection(EstadoNave state) {
        estadoNave = state;
    }

    public void setInvisible() {
        visible = false;
    }

}
