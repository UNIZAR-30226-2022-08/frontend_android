package eina.unizar.bibliochill.components;

import android.content.Context;
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

    boolean selected = false;

    public Casilla(Context context, int color, int row, int col, int size) {
        this.color = color;
        this.row = row;
        this.col = col;
        this.size = size;
        this.colorBorder = context.getColor(R.color.c_Amarillo_Boton);
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

        if (selected){
            Paint border = new Paint();
            border.setStyle(Paint.Style.STROKE);
            border.setColor(colorBorder);
            border.setStrokeWidth(10);
            canvas.drawRect(getRect(), border);
        }
    }
}
