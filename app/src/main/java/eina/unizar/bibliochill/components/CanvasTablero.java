package eina.unizar.bibliochill.components;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.util.Pair;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

import eina.unizar.bibliochill.R;

public class CanvasTablero extends View {
    int black = getResources().getColor(R.color.black, null);
    int white = getResources().getColor(R.color.white, null);

    Casilla[][] casillas = new Casilla[8][8];
    Integer selectedRow, selectedCol;

    private void init() {
        for (int i = 0; i < 8; i++){
            for (int j = 0; j < 8; j++){
                int color = ((i + j) % 2) == 0 ? white : black;
                casillas[i][j] = new Casilla(getContext(), color, i, j, 121);
            }
        }
    }

    public CanvasTablero(Context context) {
        super(context);
        init();
    }

    public CanvasTablero(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public OnTouchListener touchListener() {
        return new OnTouchListener() {

            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                if(motionEvent.getAction() != MotionEvent.ACTION_DOWN) {
                    return false;
                }

                int x = (int) motionEvent.getX();
                int y = (int) motionEvent.getY();

                int col = x / 121;
                int row = y / 121;

                Log.d("tablero", "touch col:" + col + ", row:" + row);

                if (col < 0 || col >= 8 || row < 0 || row >= 8) {
                    return true;
                }

                // Si ya hay una seleccionada, deseleccionamos
                if(selectedRow != null) {
                    casillas[selectedRow][selectedCol].selected = false;
                }
                // Seleccionamos la pulsada
                casillas[row][col].selected = true;
                selectedRow = row;
                selectedCol = col;
                invalidate();

                return true;
            }

        };
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
