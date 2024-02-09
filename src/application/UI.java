package application;

import chess.ChessPiece;

public class UI {
    public static void printBoard(ChessPiece[][] pieces) {
        int colIdentifier = 9;
        for (int i = 0; i < pieces.length; i++) {
            System.out.print(colIdentifier - (i + 1) + "  ");
            for (int j = 0; j < pieces.length; j++) {

                printPiece(pieces[i][j]);
            }
            System.out.println();
        }
        System.out.print("   ");
        for (int i = 97; i <= 104; i++) {
            System.out.print((char) i + " ");
        }
    }

    private static void printPiece(ChessPiece piece) {
        if (piece == null) {
            System.out.print('-');
        } else {
            System.out.print(piece);
        }
        System.out.print(" ");
    }
}
