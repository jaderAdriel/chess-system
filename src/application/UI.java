package application;

import chess.ChessMatch;
import chess.ChessPiece;
import chess.ChessPosition;
import chess.Color;

import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class UI {
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_RED = "\u001B[31m";


    // Bold High Intensity
    public static final String WHITE_BOLD_BRIGHT = "\033[1;97m";

    public static final String RED_UNDERLINED = "\033[4;31m";    // RED

    public static final String YELLOW_BACKGROUND_BRIGHT = "\033[0;103m";// YELLOW
    public static final String BLACK_BOLD = "\033[1;30m";  // BLACK
    public static final String RED_BOLD = "\033[1;31m";    // RED

    public static final String GRAY_BACKGROUND_BRIGHT = "\033[0;47m"; // Background cinza claro
    public static final String GRAY_BACKGROUND_NORMAL = "\033[0;100m"; // Background cinza escuro

    public static final String BLACK_PIECES_COLOR = BLACK_BOLD;
    public static final String WHITE_PIECES_COLOR = "\033[1;33m";
    public static final String POSSIBLE_MOVE_COLOR = RED_BOLD;
    public static final String WHITE_PLACE_BG = GRAY_BACKGROUND_BRIGHT;
    public static final String BLACK_PLACE_BG = GRAY_BACKGROUND_NORMAL;
    public static final String YELLOW_BOLD = "\033[1;33m"; // YELLOW

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

    public static void printMatch(ChessMatch chessMatch, List<ChessPiece> captured) {
        printBoard(chessMatch.getPieces());
        System.out.println("\nTurn: " + chessMatch.getTurn());
        printCapturedPieces(captured);
        if (chessMatch.isInCheck()) System.out.println("\nCheck!!");
        System.out.println("Waiting player: " + chessMatch.getCurrentPlayer());
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

    private static void printCapturedPieces(List<ChessPiece> capturedPieces) {
        List<ChessPiece> white = capturedPieces.stream().filter(x -> x.getColor() == Color.WHITE).toList();
        List<ChessPiece> black = capturedPieces.stream().filter(x -> x.getColor() == Color.BLACK).toList();
        System.out.println("Captured pieces: ");
        System.out.println("    White: " + WHITE_BOLD_BRIGHT + Arrays.toString(white.toArray()) + ANSI_RESET);
        System.out.println("    Black: " + YELLOW_BOLD + Arrays.toString(black.toArray()) + ANSI_RESET);
    }

    public static void printEndMessage(ChessPiece[][] pieces, Color winner) {
        printBoard(pieces);
        System.out.println("\nCheck mate!! \n" + winner + " pieces are the winner!!");
    }
}
