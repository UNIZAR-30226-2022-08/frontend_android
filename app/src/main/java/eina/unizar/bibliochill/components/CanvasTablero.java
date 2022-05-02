package eina.unizar.bibliochill.components;

import android.app.DownloadManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.Nullable;

import java.io.IOException;

import eina.unizar.bibliochill.R;

import androidx.appcompat.app.AppCompatActivity;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

// TODO: getState() -> Devuelve el status del juego, incluyendo las posiciones de las piezas, el
//                     siguiente en mover en el campo "response". Devuelve 400 y error en caso contrario.
//       getMoves() -> Devuelve idGame, x e y
//       move() -> Devuelve 200 si la pieza en x e y se moverá a newX y newY.
//                 Devuelve 400 si el movimiento es ilegal


public class CanvasTablero extends View {
    private Bitmap piezas;
    // Tamaño de la casilla del tablero
    final int tileSize = 121;

    final int N_FRAMES = 12;
    // Tamaño de las casillas del spritesheet
    int tileWidth;
    int tileHeight;
    Rect[] frames = new Rect[N_FRAMES];

    int grey = getResources().getColor(R.color.grey, null);
    int white = getResources().getColor(R.color.white, null);

    Casilla[][] casillas = new Casilla[8][8];
    Integer selectedRow, selectedCol;

    //public static final MediaType JSON = MediaType.get("application/json; charset=utf-8");
    OkHttpClient client = new OkHttpClient();
    EditText gameID, posX, posY, newX, newY;

    private void init() {
        // Crear los sprites de las fichas
        addSprites();

        // Dibujar tablero
        drawTablero();

        // Dibujar fichas en el tablero
        initFichas();
    }

    void addSprites() {
        piezas = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.chess_pieces_sprite);
        int cols = 6;
        int rows = 2;
        tileWidth = piezas.getWidth() / cols;
        tileHeight = piezas.getHeight() / rows;

        for (int i = 0; i < rows; i++) {
            // Calculamos la fila en la que estamos
            final int ty = i * tileHeight;

            for (int j = 0; j < cols; j++) {
                // Calculamos la columna en la que estamos
                final int tx = j * tileWidth;

                Rect sprite = new Rect(tx, ty, tx + tileWidth, ty + tileHeight);

                // Añadimos el sprite
                frames[i * cols + j] = sprite;
            }
        }
    }

    void drawTablero() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                int color = ((i + j) % 2) == 0 ? white : grey;
                casillas[i][j] = new Casilla(getContext(), color, i, j, tileSize, frames, piezas);
            }
        }
    }

    void initFichas() {
        // Inicializar peones
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (i == 1) {
                    casillas[i][j].pieza = TipoPieza.PAWN_BLACK;
                }
                if (i == 6) {
                    casillas[i][j].pieza = TipoPieza.PAWN_WHITE;
                }
            }
        }

        // Inicializar torres
        casillas[0][0].pieza = TipoPieza.TOWER_BLACK;
        casillas[0][7].pieza = TipoPieza.TOWER_BLACK;

        casillas[7][0].pieza = TipoPieza.TOWER_WHITE;
        casillas[7][7].pieza = TipoPieza.TOWER_WHITE;

        // Inicializar rey y reina
        casillas[0][3].pieza = TipoPieza.KING_BLACK;
        casillas[0][4].pieza = TipoPieza.QUEEN_BLACK;

        casillas[7][3].pieza = TipoPieza.KING_WHITE;
        casillas[7][4].pieza = TipoPieza.QUEEN_WHITE;

        // Inicializar caballos
        casillas[0][1].pieza = TipoPieza.HORSE_BLACK;
        casillas[0][6].pieza = TipoPieza.HORSE_BLACK;

        casillas[7][1].pieza = TipoPieza.HORSE_WHITE;
        casillas[7][6].pieza = TipoPieza.HORSE_WHITE;

        // Inicializar alfiles
        casillas[0][2].pieza = TipoPieza.BISHOP_BLACK;
        casillas[0][5].pieza = TipoPieza.BISHOP_BLACK;

        casillas[7][2].pieza = TipoPieza.BISHOP_WHITE;
        casillas[7][5].pieza = TipoPieza.BISHOP_WHITE;
    }

    public CanvasTablero(Context context) {
        super(context);
        init();
    }

    public CanvasTablero(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    // Funciones de la maquina de estamos
    public boolean isSelected() {
        return selectedRow != null;
    }

    boolean fueraDelTablero(int row, int col) {
        if (row < 0 || row > 7 || col < 0 || col > 7) {
            return true;
        } else {
            return false;
        }
    }

    boolean hayPieza(int row, int col) {
        if (!fueraDelTablero(row, col)) {
            if (casillas[row][col].pieza != TipoPieza.EMPTY) {
                return true;
            }
        }
        return false;
    }

    void matar(int row, int col) {
        casillas[row][col].pieza = casillas[selectedRow][selectedCol].pieza;
        casillas[selectedRow][selectedCol].pieza = TipoPieza.EMPTY;
    }

    void mover(int row, int col) {

        // La pieza se puede mover
        try {
            if(move().contains("200")) {
                Log.d("move()", "se puede mover");
                casillas[row][col].pieza = casillas[selectedRow][selectedCol].pieza;
                casillas[selectedRow][selectedCol].pieza = TipoPieza.EMPTY;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


        //casillas[row][col].pieza = casillas[selectedRow][selectedCol].pieza;
        //casillas[selectedRow][selectedCol].pieza = TipoPieza.EMPTY;
    }

    void seleccionarCasilla(int row, int col) {
        selectedRow = row;
        selectedCol = col;
    }

    void deseleccionarCasilla() {
        selectedRow = null;
        selectedCol = null;
    }

    boolean esVacia(int row, int col) {
        if (!fueraDelTablero(row, col)) {
            if (casillas[row][col].pieza == TipoPieza.EMPTY) {
                return true;
            }
        }
        return false;
    }
    // Fin funciones de la maquina de estados

    public OnTouchListener touchListener() {
        return new OnTouchListener() {

            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                if (motionEvent.getAction() != MotionEvent.ACTION_DOWN) {
                    return false;
                }

                int x = (int) motionEvent.getX();
                int y = (int) motionEvent.getY();

                int col = x / 121;
                int row = y / 121;

                if (col < 0 || col >= 8 || row < 0 || row >= 8) {
                    return true;
                }

                // Máquina de estados movimiento piezas
                if (isSelected()) {
                    if (hayPieza(row, col) && casillas[selectedRow][selectedCol].pieza.checkMovimiento(row, col, selectedRow, selectedCol)) {
                        matar(row, col);
                        deseleccionarCasilla();
                    } else if (esVacia(row, col) && casillas[selectedRow][selectedCol].pieza.checkMovimiento(row, col, selectedRow, selectedCol)) {
                        mover(row, col);
                        deseleccionarCasilla();
                    }
                } else if (!isSelected()) {
                    if (hayPieza(row, col)) {
                        seleccionarCasilla(row, col);
                    }
                }

                invalidate();

                return true;
            }

        };
    }

    // TODO: modificar para que funcione
    String getMoves() throws IOException {
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("idGame", gameID.getText().toString())
                .addFormDataPart("x", posX.getText().toString())
                .addFormDataPart("y", posY.getText().toString())
                .build();

        Request request = new Request.Builder()
                .url("https://queenchess-backend.herokuapp.com/game/getMoves")
                .post(requestBody)
                .build();

        try (Response response = client.newCall(request).execute()){
            return response.body().string();
        }
    }

    String move() throws IOException {
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("idGame", gameID.getText().toString())
                .addFormDataPart("x", posX.getText().toString())
                .addFormDataPart("y", posY.getText().toString())
                .addFormDataPart("newX", newX.getText().toString())
                .addFormDataPart("newY", newY.getText().toString())
                .build();

        Request request = new Request.Builder()
                .url("https://queenchess-backend.herokuapp.com/game/move")
                .post(requestBody)
                .build();

        try (Response response = client.newCall(request).execute()){
            return response.body().string();
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                casillas[i][j].onDraw(canvas);
            }
        }
    }
}