package eina.unizar.bibliochill.components;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Rect;
import android.view.View;

import eina.unizar.bibliochill.R;

public class Casilla {
    int color;
    int row, col;
    int size;
    int colorBorder;

    private Rect[] frames;
    private Bitmap piezas;

    TipoPieza pieza = TipoPieza.EMPTY;

    boolean selected = false;

    public Casilla(Context context, int color, int row, int col, int size, Rect[] frames, Bitmap piezas) {
        this.color = color;
        this.row = row;
        this.col = col;
        this.size = size;
        this.colorBorder = context.getColor(R.color.c_Amarillo_Boton);
        this.frames = frames;
        this.piezas = piezas;
    }

    Rect getRect() {
        int y = row * size;
        int x = col * size;

        Rect rect = new Rect();
        rect.top = y;
        rect.left = x;
        rect.right = x + size;
        rect.bottom = y + size;

        return rect;
    }

    public void onDraw(Canvas canvas) {
        Paint paint = new Paint();
        paint.setColor(color);
        canvas.drawRect(getRect(), paint);

        if(pieza != TipoPieza.EMPTY){
            paint = new Paint();
            canvas.drawBitmap(piezas, frames[pieza.ordinal()], getRect(), paint);
        }

        if (selected){
            Paint border = new Paint();
            border.setStyle(Paint.Style.STROKE);
            border.setColor(colorBorder);
            border.setStrokeWidth(10);
            canvas.drawRect(getRect(), border);
        }
    }
}
