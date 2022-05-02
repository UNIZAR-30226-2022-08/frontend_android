package eina.unizar.bibliochill.components;

import android.util.Log;

public enum TipoPieza {
    QUEEN_WHITE, KING_WHITE, BISHOP_WHITE, HORSE_WHITE, TOWER_WHITE, PAWN_WHITE,
    QUEEN_BLACK, KING_BLACK, BISHOP_BLACK, HORSE_BLACK, TOWER_BLACK, PAWN_BLACK,
    EMPTY;

    // TODO: Comprobación movimiento pieza
    public boolean checkMovimiento(int row, int col, Integer lastRow, Integer lastCol) {
        switch (this) {
            // Diagonal y recto, todas las direcciones
            case QUEEN_WHITE:
            case QUEEN_BLACK:

            // Una casilla, todas las direcciones
            case KING_WHITE:
            case KING_BLACK:

            // Diagonal, todas las direcciones
            case BISHOP_WHITE:
            case BISHOP_BLACK:

            // L, todas las direcciones
            case HORSE_WHITE:
            case HORSE_BLACK:

            // Recto, todas las direcciones
            case TOWER_WHITE:
            case TOWER_BLACK:

            // Recto una casilla (dos al principio), hacia adelante
            case PAWN_WHITE:
            case PAWN_BLACK:
        }
        return true;
    }
}