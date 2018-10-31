package com.example.kikeajani.spaceinvadersbeta;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;
import java.util.Iterator;

public class SpaceInvadersJuego extends SurfaceView implements Runnable {

    private boolean jugando;

    private Context context;

    private Thread hilo;

    private int screenX;
    private int screenY;

    private final float velocidadGlobal = 100.0f;

    private ControladorObjetos controladorObjetos;

    private SurfaceHolder ourHolder;
    private Canvas canvas;
    private Paint paint;

    private Activity activity;

    private Buttons buttons;

    private Nave nave;

    private Disparo[] disparosAlien = new Disparo[150];

    private ArrayList<Alien> aliens = new ArrayList<>();
    private int numAliens;
    private int aliensActivos;

    private Barrera[] barreras = new Barrera[20];
    private int numBarreras;
    private int numDisparos = 0;

    private boolean mayor;

    private int puntuacion;

    public ArrayList<Alien> getAliens() {
        return aliens;
    }

    public SpaceInvadersJuego(Context context, Point point, boolean mayor, Activity activity) {

        //Constructor de SurfaceView
        super(context);

        this.activity = activity;
        this.context = context;

        ourHolder = getHolder();
        paint = new Paint();

        screenX = point.x;
        screenY = point.y;

        controladorObjetos = new ControladorObjetos(this);

        buttons = new Buttons(context, screenX, screenY, mayor);

        this.mayor = mayor;

        this.puntuacion = 0;

        generarNivel();
    }

    public void mostrarPuntuacionFin(){
        this.jugando=false;
        Intent intent = new Intent(activity, FinDelJuegoActivity.class);
        intent.putExtra("puntos", String.valueOf(puntuacion));
        if (mayor) {
            intent.putExtra("tipoJuego", "mayor");
        } else {
            intent.putExtra("tipoJuego", "menor");
        }
        activity.startActivity(intent);
        activity.finish();
    }


    @Override
    public void run() {
        while (jugando) {

            if (this.aliensActivos == 0) {
                this.mostrarPuntuacionFin();
                //this.reiniciar();
            }
            try {
                controladorObjetos.updateAll();
                controladorObjetos.drawAll(ourHolder, canvas, paint);
            }catch (Exception e){
                activity.finish();
            }
        }
    }

    private void generarNivel() {

        nave = new Nave(context, screenX, screenY, velocidadGlobal);
        controladorObjetos.add("nave", nave);

        for (int i = 0; i < disparosAlien.length; i++) {
            disparosAlien[i] = new Disparo(screenY, screenX, this, false, velocidadGlobal);
            controladorObjetos.add("disparoAlien" + i, disparosAlien[i]);
        }

        aliensActivos = 0;
        numAliens = 0;
        for (int columna = 0; columna < 6; columna++) {
            for (int fila = 0; fila < 5; fila++) {
                Alien al = new Alien(context, fila, columna, screenX, screenY, this, this.mayor, velocidadGlobal);
                aliens.add(al);
                controladorObjetos.add("alien" + numAliens, al);
                numAliens++;
                aliensActivos++;
            }
        }

        numBarreras = 0;
        for (int numBloque = 0; numBloque < 4; numBloque++) {
            for (int columna = 0; columna < 1; columna++) {
                for (int fila = 0; fila < 5; fila++) {
                    barreras[numBarreras] = new Barrera(fila, columna, numBloque, screenX, screenY);
                    controladorObjetos.add("barrera" + numBarreras, barreras[numBarreras]);
                    numBarreras++;
                }
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {

        switch (motionEvent.getAction() & MotionEvent.ACTION_MASK) {

            // El jugador toca la pantalla
            case MotionEvent.ACTION_DOWN:
                float xPos = motionEvent.getX();
                float yPos = motionEvent.getY();
                boolean buttonPress = false;

                Iterator itr = buttons.getButtons().iterator();
                while (itr.hasNext()) {
                    Buttons.ButtonItem button = (Buttons.ButtonItem) itr.next();
                    if (xPos > button.rect.left && xPos < button.rect.right &&
                            yPos > button.rect.top && yPos < button.rect.bottom) {
                        switch (button.action) {
                            /*case Pause:
                                gameState = GameState.Paused;
                                buttonPress = true;
                                break;*/
                            case Shoot:
                                Disparo disparo = new Disparo(screenY, screenX, this, true, velocidadGlobal);
                                numDisparos++;
                                controladorObjetos.add("disparo" + numDisparos, disparo);
                                if (disparo.disparar(nave.getPosition().x + nave.getLength() / 2, nave.getPosition().y, Disparo.Direction.Arriba, true)) {
                                    buttonPress = true;
                                }
                                break;
                        }
                    }
                }
                //Mover nave
                if (!buttonPress) {
                    if (motionEvent.getX() < screenX / 4) {
                        nave.setDirection(Nave.EstadoNave.Izquierda);
                    } else if (motionEvent.getX() > ((screenX / 4) * 3)) {
                        nave.setDirection(Nave.EstadoNave.Derecha);
                    }
                }
                break;
            case MotionEvent.ACTION_UP:

                if (jugando) {
                    //Parar nave
                    nave.setDirection(Nave.EstadoNave.Parada);
                }
                break;
        }
        return true;
    }

    public void drawButtons(Canvas c, Paint p){
        buttons.draw(c, p);
    }

    public int getNumAliens() { return numAliens; }

    public void alienMuere() {
        this.puntuacion += 100;
        aliensActivos--;
    }

    public ControladorObjetos getControladorObjetos() {
        return controladorObjetos;
    }

    public int getNumBarreras() {
        return numBarreras;
    }

    public void pause() {}

    public void resume() {
        jugando = true;
        hilo = new Thread(this);
        hilo.start();
    }

    public void reiniciar() {
        this.controladorObjetos = new ControladorObjetos(this);
        this.generarNivel();
    }



}
