package eina.unizar.bibliochill.components;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.view.MotionEvent;
import android.view.View;

import eina.unizar.bibliochill.R;

public class CanvasTablero extends View {
    int black = getResources().getColor(R.color.black, null);
    int white = getResources().getColor(R.color.white, null);

    Casilla[][] casillas = new Casilla[8][8];


    @SuppressLint("ClickableViewAccessibility")
    public CanvasTablero(Context context) {
        super(context);
        this.setOnTouchListener((view, motionEvent) -> {

            int x = (int) motionEvent.getX();
            int y = (int) motionEvent.getY();

            int col = x / 121;
            int row = y / 121;

            if (col < 0 && col >= 8 || row < 0 && row >= 8) {
                return true;
            }
            casillas[col][row].selected = true;

            return true;
        });

        for (int i = 0; i < 8; i++){
            for (int j = 0; j < 8; j++){
                int color = ((i + j) % 2) == 0 ? white : black;
                casillas[i][j] = new Casilla(color, i, j, 121);
            }
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        for (int i = 0; i < 8; i++){
            for (int j = 0; j < 8; j++){
                casillas[i][j].onDraw(canvas);
            }
        }
    }
}
