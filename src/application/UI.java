package application;

import chess.ChessPiece;
import chess.ChessPosition;
import chess.Color;

import java.util.InputMismatchException;
import java.util.Scanner;

public class UI {
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_WHITE = "\u001B[37m";

    public static final String ANSI_BLACK_BACKGROUND = "\u001B[40m";
    public static final String ANSI_RED_BACKGROUND = "\u001B[41m";
    public static final String ANSI_GREEN_BACKGROUND = "\u001B[42m";
    public static final String ANSI_YELLOW_BACKGROUND = "\u001B[43m";
    public static final String ANSI_BLUE_BACKGROUND = "\u001B[44m";
    public static final String ANSI_PURPLE_BACKGROUND = "\u001B[45m";
    public static final String ANSI_CYAN_BACKGROUND = "\u001B[46m";
    public static final String ANSI_WHITE_BACKGROUND = "\u001B[47m";
    public static final String ANSI_GOLD = "\u001B[33m";

    public static ChessPosition readChessPosition(Scanner sc) {
        try {
            String position = sc.nextLine();
            int row = Character.getNumericValue(position.charAt(1));
            char column = position.charAt(0);
            return new ChessPosition(row, column);
        }
        catch (RuntimeException e) {
            throw new InputMismatchException("Error reading ChessPosition. Valid values are from a1 to h8.");
        }
    }
    public static void printBoard(ChessPiece[][] pieces) {

        for (int i = pieces.length - 1; i >= 0; i--) {
            String nextColor = (i % 2 != 0) ? ANSI_WHITE_BACKGROUND : ANSI_GREEN_BACKGROUND;

            System.out.print(i +  1 + " ");
            for (int j = 0; j < pieces.length; j++) {
                printPiece(pieces[i][j], nextColor);

                nextColor = (nextColor.equals(ANSI_GREEN_BACKGROUND)) ? ANSI_WHITE_BACKGROUND : ANSI_GREEN_BACKGROUND;

            }
            System.out.println(ANSI_RESET);
        }
        System.out.print("   ");
        for (int i = 97; i <= 104; i++) {
            System.out.print((char) i + "  ");
        }
    }

    private static void printPiece(ChessPiece piece, String background) {
        if (piece == null) {
            System.out.print(ANSI_RESET + background + " -");
        } else {
            if (piece.getColor() == Color.WHITE) {
                System.out.print(ANSI_RESET + background + " " + piece);
            }
            else {
                System.out.print(ANSI_RESET + background + " " + ANSI_BLACK + piece);
            }
        }
        System.out.print(" ");
    }
}
