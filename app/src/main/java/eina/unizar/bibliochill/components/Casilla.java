package eina.unizar.bibliochill.components;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.View;

import eina.unizar.bibliochill.R;

public class Casilla {
    int color;
    int row, col;
    int size;

    boolean selected = false;

    public Casilla(int color, int row, int col, int size) {
        this.color = color;
        this.row = row;
        this.col = col;
        this.size = size;
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
        if (selected)
            paint.setAlpha(50);
        canvas.drawRect(getRect(), paint);
    }
}
