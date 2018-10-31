package com.example.kikeajani.spaceinvadersbeta;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.RectF;
import android.support.v4.content.ContextCompat;


public class Alien extends ObjetoVisible {

    public enum Estado {Izquierda, Derecha}

    private Bitmap bitmap;
    private Paint alienPaint;

    // Dimensiones de la pantalla
    private int screenX;
    private int screenY;

    private Estado estado;
    private boolean visible; // visibilidad

    private float velocidad;

    private int numDisparo; // id del proximo disparo

    private int columna;
    private int padding;

    private int length;
    private int height;
    private String color;

    private boolean mayor;
    private Context context;


    public Alien(Context context, int row, int column, int screenX, int screenY, SpaceInvadersJuego sij, boolean mayor, float velocidad){

        this.mayor = mayor;
        this.context=context;
        this.columna = column;
        this.padding = screenX / 20;

        this.spaceInvadersJuego = sij;

        this.screenX = screenX;
        this.screenY = screenY;

        length = screenX / 15;
        height = screenY / 28;

        float x = column * (length + padding);
        float y = row * (length + padding / 4);

        setSize(length, height);
        setPosicionInicial(x, y);

        visible = true;

        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.alien);
        bitmap = Bitmap.createScaledBitmap(bitmap, (int) length, (int) height, false);

        alienPaint = new Paint();
        alienPaint.setColorFilter(new PorterDuffColorFilter(ContextCompat.getColor(context, R.color.verde), PorterDuff.Mode.SRC_IN));


        color="Verde";

        this.velocidad = (velocidad / 50);

        estado = Estado.Derecha;

        numDisparo = 0;
    }

    @Override //ObjetoVisible
    public void update (){

        PointF loc = getPosition();
        if(estado == Estado.Izquierda){
            loc.x = loc.x - velocidad;
        } else if (estado == Estado.Derecha){
            loc.x = loc.x + velocidad;
        }

        setPosition(loc.x, loc.y);
        loc = getPosition();

        Nave nave = (Nave) spaceInvadersJuego.getControladorObjetos().get("nave");

        // disparo random
        if((this.mayor) && (((int)(Math.random() * 600)) == ((int)(Math.random() * 600))) && (visible)) {
            Disparo disparo = (Disparo) spaceInvadersJuego.getControladorObjetos().get("disparoAlien" + numDisparo);
            if(disparo.disparar(loc.x + getLength() / 2, loc.y, Disparo.Direction.Abajo, false)){
                numDisparo++;
            }
        }

        // aliens llegan abajo
        if (this.visible) {
            for (int i = 0; i < spaceInvadersJuego.getNumBarreras(); i++) {
                Barrera barrera = (Barrera) spaceInvadersJuego.getControladorObjetos().get("barrera" + i);
                if (barrera.getVisibility()) {
                    if (RectF.intersects(barrera.getBoundingRect(), getBoundingRect())) {
                        barrera.setInvisible();
                    }
                }
            }
            if (loc.y > (screenY - ((screenY / 34) * 10))) {
               // spaceInvadersJuego.reiniciar();
                spaceInvadersJuego.mostrarPuntuacionFin();

            }
        }

        // Borde de la pantalla
        if((loc.x > (screenX - ((getLength() + padding) * (6 - columna)))) || (loc.x < ((getLength() + padding) * columna ))){
            mediaVuelta();
        }
    }

    @Override
    public void draw (Canvas canvas, Paint paint){
        if (visible){
            PointF loc = getPosition();
            canvas.drawBitmap(bitmap, loc.x, loc.y, alienPaint);
        }
    }

    public void mediaVuelta(){
        if (estado == Estado.Izquierda){
            estado = Estado.Derecha;
        } else {
            estado = Estado.Izquierda;
        }
        PointF loc = getPosition();
        loc.y = loc.y + getHeight();
        setPosition(loc.x, loc.y);
    }

    public void setInvisible(){
        this.spaceInvadersJuego.alienMuere();
        visible = false;
    }

    public boolean getVisibility(){
        return visible;
    }

    public void cambiarColor(){
        switch (this.color){
            case "Verde":
                alienPaint.setColorFilter(new PorterDuffColorFilter(ContextCompat.getColor(context, R.color.morado), PorterDuff.Mode.SRC_IN));
                color="Morado";
                break;
            case "Morado":
                alienPaint.setColorFilter(new PorterDuffColorFilter(ContextCompat.getColor(context, R.color.amarillo), PorterDuff.Mode.SRC_IN));
                color="Amarillo";
                break;
            case "Amarillo":
                alienPaint.setColorFilter(new PorterDuffColorFilter(ContextCompat.getColor(context, R.color.azul), PorterDuff.Mode.SRC_IN));
                color="Azul";
                break;
            case "Azul":
                alienPaint.setColorFilter(new PorterDuffColorFilter(ContextCompat.getColor(context, R.color.rosa), PorterDuff.Mode.SRC_IN));
                color="Rosa";
                break;
            case "Rosa":
                alienPaint.setColorFilter(new PorterDuffColorFilter(ContextCompat.getColor(context, R.color.blanco), PorterDuff.Mode.SRC_IN));
                color="Blanco";
                break;
            case "Blanco":
                alienPaint.setColorFilter(new PorterDuffColorFilter(ContextCompat.getColor(context, R.color.naranja), PorterDuff.Mode.SRC_IN));
                color="Naranja";
                break;
            case "Naranja":
                alienPaint.setColorFilter(new PorterDuffColorFilter(ContextCompat.getColor(context, R.color.verde), PorterDuff.Mode.SRC_IN));
                color="Verde";
                break;
        }
    }


}
