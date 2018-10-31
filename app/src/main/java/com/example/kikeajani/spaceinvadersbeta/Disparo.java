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

    public enum Tipo{Nave, Alien, General}

    private boolean activo; // si el disparo esta activo

    private Direction direccion; // direccion del disparo

    private float velocidad;
    private float inclinacion;

    private int screenY;
    private int screenX;

    private Tipo tipo;


    public Disparo(int screenY, int screenX, SpaceInvadersJuego sij, boolean nave, float velocidad){
        if (nave) {
            this.tipo = Tipo.Nave;
        } else {
            this.tipo = Tipo.Alien;
        }
        this.spaceInvadersJuego = sij;
        this.screenX = screenX;
        this.screenY = screenY;
        int height = screenY / 75;
        int length = screenY / 75;
        setSize(length, height);
        direccion = Direction.SinDireccion;
        this.velocidad = (velocidad / 4);
        inclinacion = 0;
        activo = false;
    }


    @Override
    public void update(){
        if(activo) {
            PointF posicion = getPosition(); //Posicion actual

            //Nueva posicion
            if (direccion == Direction.Arriba) {
                posicion.y -= velocidad;
            } else {
                posicion.y += velocidad;
            }
            if (inclinacion != 0) {
                posicion.x -= (inclinacion);
            }

            //Actualizar posicion
            setPosition(posicion.x, posicion.y);
        }

        //Colisiones

        /*
        //Parte superior o inferior de la pantalla
        if(getImpactPointY() < 0 || getImpactPointY() > screenY){
            setInactive();
        }
        */

        //Disparo nave - alien
        if(activo && ((tipo == Tipo.Nave) || (tipo == Tipo.General))) {
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

        //Disparo alien - nave
        if(activo && ((tipo == Tipo.Alien) || (tipo == Tipo.General))){
            Nave nave = (Nave) spaceInvadersJuego.getControladorObjetos().get("nave");
            if (RectF.intersects(nave.getBoundingRect(), getBoundingRect())) {
                setInactive();
                nave.setInvisible();
                spaceInvadersJuego.mostrarPuntuacionFin();
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

        //Rebote arriba o abajo
        if ((getImpactPointY() - getHeight()) > screenY) {
            direccion = Direction.Arriba;
            tipo = Tipo.General;
            inclinacion = (float) Math.random() * 30 - 15;
        } else if (getImpactPointY() < 0) {
            direccion = Direction.Abajo;
            tipo = Tipo.General;
            inclinacion = (float) Math.random() * 30 - 15;
        }

        //Rebote lateral
        if ((getImpactPointX() > screenX) || (getImpactPointX() < 0)) {
            inclinacion = inclinacion * -1;
        }

    }

    @Override
    public void draw (Canvas canvas, Paint paint) {
        if(activo){
            paint.setColor(Color.argb(255, 255, 0, 0));
            canvas.drawRect(getBoundingRect(), paint);
        }
    }

    public boolean disparar(float startX, float startY, Direction direccion, boolean nave) {

        if(!activo){
            if (nave) {
                this.tipo = Tipo.Nave;
            } else {
                this.tipo = Tipo.Alien;
            }

            // get current position
            PointF loc = getPosition();

            // set new position
            loc.x = startX;
            loc.y = startY;
            setPosicionInicial(loc.x, loc.y);
            inclinacion = 0;

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

    public float getImpactPointX(){
        return getPosition().x + (getLength() / 2);
    }

}
