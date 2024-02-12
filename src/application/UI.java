package application;

import chess.ChessPiece;

public class UI {
    public static void printBoard(ChessPiece[][] pieces) {

        for (int i = pieces.length - 1; i >= 0; i--) {
            System.out.print(i +  1 + " ");
            for (int j = 0; j < pieces.length; j++) {

                printPiece(pieces[i][j]);
            }
            System.out.println();
        }
        System.out.print("  ");
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
