package application;

import chess.ChessPiece;
import chess.ChessPosition;
import chess.Color;

import java.util.InputMismatchException;
import java.util.Scanner;

public class UI {
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_WHITE = "\u001B[37m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String GREEN_BG = "\u001B[42m";
    public static final String WHITE_BG = "\u001B[47m";


    // Bold High Intensity
    public static final String BLACK_BOLD_BRIGHT = "\033[1;90m"; // BLACK
    public static final String RED_BOLD_BRIGHT = "\033[1;91m";   // RED
    public static final String GREEN_BOLD_BRIGHT = "\033[1;92m"; // GREEN
    public static final String YELLOW_BOLD_BRIGHT = "\033[1;93m";// YELLOW
    public static final String BLUE_BOLD_BRIGHT = "\033[1;94m";  // BLUE
    public static final String PURPLE_BOLD_BRIGHT = "\033[1;95m";// PURPLE

    public static final String RED_UNDERLINED = "\033[4;31m";    // RED

    // High Intensity backgrounds
    public static final String BLACK_BACKGROUND_BRIGHT = "\033[0;100m";// BLACK
    public static final String RED_BACKGROUND_BRIGHT = "\033[0;101m";// RED
    public static final String GREEN_BACKGROUND_BRIGHT = "\033[0;102m";// GREEN
    public static final String YELLOW_BACKGROUND_BRIGHT = "\033[0;103m";// YELLOW
    public static final String BLUE_BACKGROUND_BRIGHT = "\033[0;104m";// BLUE
    public static final String PURPLE_BACKGROUND_BRIGHT = "\033[0;105m"; // PURPLE
    public static final String CYAN_BACKGROUND_BRIGHT = "\033[0;106m";  // CYAN
    public static final String WHITE_BACKGROUND_BRIGHT = "\033[0;107m";   // WHITE
    public static final String PURPLE_BOLD = "\033[1;35m"; // PURPLE
    public static final String BLACK_BOLD = "\033[1;30m";  // BLACK
    public static final String RED_BOLD = "\033[1;31m";    // RED
    public static final String GREEN_BACKGROUND = "\033[42m";  // GREEN



    public static final String BLACK_PIECES_COLOR = BLACK_BOLD;
    public static final String WHITE_PIECES_COLOR = PURPLE_BOLD;
    public static final String POSSIBLE_MOVE_COLOR = RED_BOLD;
    public static final String WHITE_PLACE_BG = CYAN_BACKGROUND_BRIGHT;
    public static final String BLACK_PLACE_BG = GREEN_BACKGROUND;
    public static final String YELLOW_BOLD = "\033[1;33m"; // YELLOW
    public static final String YELLOW_BACKGROUND = "\033[43m"; // YELLOW

    public static void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }
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
        clearScreen();
        for (int i = pieces.length - 1; i >= 0; i--) {
            String nextBackgroundColor = (i % 2 != 0) ? WHITE_PLACE_BG : BLACK_PLACE_BG;

            System.out.print(i +  1 + " ");
            for (int j = 0; j < pieces.length; j++) {

                String formarting = ANSI_RESET + nextBackgroundColor + " ";
                printPiece(pieces[i][j], nextBackgroundColor,   false);

                nextBackgroundColor = (nextBackgroundColor.equals(BLACK_PLACE_BG)) ? WHITE_PLACE_BG : BLACK_PLACE_BG;

            }
            System.out.println(ANSI_RESET);
        }
        System.out.print("   ");
        for (int i = 97; i <= 104; i++) {
            System.out.print((char) i + "  ");
        }
    }

    public static void printBoard(ChessPiece[][] pieces, boolean[][] possibleMoves) {
        clearScreen();
        for (int i = pieces.length - 1; i >= 0; i--) {
            String nextBackgroundColor = (i % 2 != 0) ? WHITE_PLACE_BG : BLACK_PLACE_BG;

            System.out.print(i +  1 + " ");

            for (int j = 0; j < pieces.length; j++) {
                printPiece(pieces[i][j], nextBackgroundColor, possibleMoves[i][j]);

                nextBackgroundColor = (nextBackgroundColor.equals(BLACK_PLACE_BG)) ? WHITE_PLACE_BG : BLACK_PLACE_BG;

            }
            System.out.println(ANSI_RESET);
        }
        System.out.print("   ");
        for (int i = 97; i <= 104; i++) {
            System.out.print((char) i + "  ");
        }
    }


    private static void printPiece(ChessPiece piece, String backgroundColor, boolean isPossibleMove) {
        String placeholder = isPossibleMove ? " •" : "  ";
        String background = isPossibleMove ? YELLOW_BACKGROUND_BRIGHT :  backgroundColor;

        if (piece == null) {
            if (isPossibleMove) {
                System.out.print(background + POSSIBLE_MOVE_COLOR + placeholder + " ");
                return;
            }
            System.out.print(background + POSSIBLE_MOVE_COLOR + placeholder + " ");
            return;
        }

        String fontColor = piece.getColor() == Color.WHITE ? WHITE_PIECES_COLOR : BLACK_PIECES_COLOR;
        fontColor = isPossibleMove ? RED_UNDERLINED : fontColor;

        System.out.print(ANSI_RESET + background  + " " + fontColor + piece + ANSI_RESET + background + " ");
    }
}
