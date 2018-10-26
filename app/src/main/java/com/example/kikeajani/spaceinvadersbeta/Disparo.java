package com.example.kikeajani.spaceinvadersbeta;

import android.app.AlertDialog;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;

import java.util.Iterator;

public class Disparo extends ObjetoVisible {

    public enum Direction{SinDireccion, Arriba, Abajo}

    private boolean activo; // si el disparo esta activo

    private Direction direccion; // direccion del disparo

    private float velocidad;

    private int screenY;

    private boolean nave;


    public Disparo(int screenY, SpaceInvadersJuego sij, boolean nave, float velocidad){
        this.nave = nave;
        this.spaceInvadersJuego = sij;
        this.screenY = screenY;
        int height = screenY / 40;
        int length = 6;
        setSize(length, height);
        direccion = Direction.SinDireccion;
        this.velocidad = (velocidad / 4);
        activo = false;
    }


    @Override
    public void update(){
        if(activo) {
            PointF posicion = getPosition(); //Posicion actual

            //Nueva posicion
            if (direccion == Direction.Arriba) {
                posicion.y = posicion.y - velocidad;
            } else {
                posicion.y = posicion.y + velocidad;
            }
            //Actualizar posicion
            setPosition(posicion.x, posicion.y);
        }

        //Colisiones

        //Parte superior o inferior de la pantalla
        if(getImpactPointY() < 0 || getImpactPointY() > screenY){
            setInactive();
        }

        //Disparo nave - alien
        if(activo && direccion == Direction.Arriba){
            for(int i = 0; i < spaceInvadersJuego.getNumAliens(); i++){
                Alien alien = (Alien) spaceInvadersJuego.getControladorObjetos().get("alien" + i);
                if(alien.getVisibility()){
                    if(RectF.intersects(alien.getBoundingRect(), getBoundingRect())){
                        alien.setInvisible();
                        setInactive();
                    }
                }
            }
        }

        //Disparo - barrera
        if(activo){
            for(int i = 0; i < spaceInvadersJuego.getNumBarreras(); i++) {
                Barrera barrera = (Barrera) spaceInvadersJuego.getControladorObjetos().get("barrera" + i);
                if (barrera.getVisibility()) {
                    if (RectF.intersects(barrera.getBoundingRect(), getBoundingRect())) {
                        barrera.setInvisible();
                        setInactive();
                        for (Alien alien:spaceInvadersJuego.getAliens()){
                            alien.cambiarColor();
                        }

                    }
                }
            }
        }

        //Disparo alien - nave
        if(activo && direccion == Direction.Abajo){
            Nave nave = (Nave) spaceInvadersJuego.getControladorObjetos().get("nave");
            if (RectF.intersects(nave.getBoundingRect(), getBoundingRect())) {
                setInactive();
                nave.setInvisible();
                spaceInvadersJuego.mostrarPuntuacionFin();
            }
        }
    }

    @Override
    public void draw (Canvas canvas, Paint paint) {
        if(activo){
            if (nave) {
                paint.setColor(Color.argb(255, 59, 131, 189));
                canvas.drawRect(getBoundingRect(), paint);
            } else {
                paint.setColor(Color.argb(255, 255, 0, 0));
                canvas.drawRect(getBoundingRect(), paint);
            }
        }
    }

    public boolean disparar(float startX, float startY, Direction direccion) {

        if(!activo){
            // get current position
            PointF loc = getPosition();

            // set new position
            loc.x = startX;
            loc.y = startY;
            setPosicionInicial(loc.x, loc.y);

            this.direccion = direccion;
            activo = true;

            return true;
        }

        return false;
    }

    public void setInactive(){
        activo = false;
    }
    public boolean getStatus(){
        return activo;
    }

    public float getImpactPointY(){
        if(direccion == Direction.Abajo){
            return getPosition().y + getHeight();
        }
        else{
            return getPosition().y;
        }
    }

}
