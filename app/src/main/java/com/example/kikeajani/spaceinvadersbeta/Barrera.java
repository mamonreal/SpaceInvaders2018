package com.example.kikeajani.spaceinvadersbeta;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class Barrera extends ObjetoVisible {

    private int screenX;
    private int screenY;

    private boolean isVisible;

    public Barrera(int fila, int columna, int shelterNumber, int screenX, int screenY){

        this.screenX = screenX;
        this.screenY = screenY;

        int width = screenX / 9;
        int height = screenY / 90;

        int brickPadding = 0;

        int shelterPadding = screenX / 9;
        int startHeight = screenY - (screenY / 3);

        isVisible = true;

        setSize(width - 2 * brickPadding, height - 2 * brickPadding);
        setPosicionInicial((columna * width) + brickPadding + (shelterPadding * shelterNumber) +
                shelterPadding + (shelterPadding * shelterNumber), fila * height + brickPadding +
                startHeight);
    }

    // overrided methods to control game object
    @Override
    public void draw(Canvas canvas, Paint paint){
        if(isVisible) {
            paint.setColor(Color.argb(255, 156, 156, 156));
            canvas.drawRect(getBoundingRect(), paint);
        }
    }

   /* @Override
    public void reset(){
        super.reset();
        isVisible = true;
    }*/

    // setters
    public void setInvisible(){
        isVisible = false;
    }

    // getters
    public boolean getVisibility(){
        return isVisible;
    }
}
