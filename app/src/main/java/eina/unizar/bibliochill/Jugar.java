package eina.unizar.bibliochill;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import eina.unizar.bibliochill.components.CanvasTablero;

public class Jugar extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jugar);

        CanvasTablero tablero = findViewById(R.id.tablero);
        tablero.setOnTouchListener(tablero.touchListener());
    }
}