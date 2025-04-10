package application;

import chess.ChessMatch;
import chess.ChessPiece;
import chess.ChessPosition;
import chess.Color;

import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class UI {
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_RED = "\u001B[31m";

    public static final String CHESS_COM_LIGHT_BG = "\033[48;5;151m"; // Verde claro (destaque)
    public static final String CHESS_COM_DARK_BG  = "\033[48;5;71m";  // Verde escuro (destaque)

    // Cores para ações
    public static final String CHESS_COM_DARK_PLACE_TARGET_BG = "\033[48;5;191m"; // Destino selecionado (casa escura)
    public static final String CHESS_COM_LIGHTER_PLACE_TARGET_BG = "\033[48;5;149m"; // Destino selecionado (casa clara)

    public static final String LIGHTER_PLACE_TARGET_BG = CHESS_COM_LIGHTER_PLACE_TARGET_BG; // Destino selecionado (casa clara)
    public static final String DARK_PLACE_TARGET_BG = CHESS_COM_DARK_PLACE_TARGET_BG; // Destino selecionado (casa escura)

    // Peças pretas: cor preta forte
    public static final String BLACK_PIECES_COLOR = "\033[1;30m";

    // Peças brancas: cor branca forte
    public static final String WHITE_PIECES_COLOR = "\033[1;97m";

    // Casas do tabuleiro
    public static final String DARKER_PLACE_COLOR = CHESS_COM_DARK_BG;
    public static final String LIGHTER_PLACE_COLOR = CHESS_COM_LIGHT_BG;

    // Outros
    public static final String YELLOW_BOLD = "\033[1;93m";         // Amarelo vibrante
    public static final String RED_BOLD = "\033[1;91m";            // Vermelho vibrante
    public static final String RED_UNDERLINED = "\033[4;91m";      // Vermelho sublinhado
    public static final String POSSIBLE_MOVE_COLOR = YELLOW_BOLD;

    // Bold High Intensity
    public static final String WHITE_BOLD_BRIGHT = "\033[1;97m";

    public static final String YELLOW_BACKGROUND_BRIGHT = "\033[0;103m";// YELLOW
    public static final String BLACK_BOLD = "\033[1;30m";  // BLACK

    public static final String GRAY_BACKGROUND_BRIGHT = "\033[0;47m"; // Background cinza claro
    public static final String GRAY_BACKGROUND_NORMAL = "\033[0;100m"; // Background cinza escuro

    public static void clearScreen() {
        System.out.print("\n\033[H\033[2J");
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
        boolean[][] arr = new boolean[pieces.length][pieces.length];
        printBoard(pieces, arr );
    }

    public static void printBoard(ChessPiece[][] pieces, boolean[][] possibleMoves) {
        clearScreen();

        for (int i = pieces.length - 1; i >= 0; i--) {
            boolean isLighterPlace = (i % 2 != 0);

            System.out.print(i +  1 + " ");

            for (int j = 0; j < pieces.length; j++) {
                boolean placeHasPossibleMove = false;

                if (possibleMoves[0].length == pieces[0].length && possibleMoves[1].length == pieces[1].length )
                    placeHasPossibleMove = possibleMoves[i][j];

                printPlace(isLighterPlace, pieces[i][j], placeHasPossibleMove);

                isLighterPlace = !isLighterPlace;
            }
            System.out.println(ANSI_RESET);
        }
        System.out.print("   ");
        for (int i = 97; i <= 104; i++) {
            System.out.print((char) i + "  ");
        }
    }

    private static void printPlace(boolean isLighterPlace, ChessPiece piece, boolean hasPossibleMove) {
        String placeholder = hasPossibleMove ? " •" : "  ";

        String backgroundColor;

        if (hasPossibleMove) {
            backgroundColor = isLighterPlace ? LIGHTER_PLACE_TARGET_BG : DARK_PLACE_TARGET_BG;
        } else {
            backgroundColor = isLighterPlace ? LIGHTER_PLACE_COLOR : DARKER_PLACE_COLOR;
        }

        if (piece == null) {
            if (hasPossibleMove) {
                System.out.print(backgroundColor + POSSIBLE_MOVE_COLOR + placeholder + " ");
                return;
            }
            System.out.print(backgroundColor + POSSIBLE_MOVE_COLOR + placeholder + " ");
            return;
        }


        System.out.print(ANSI_RESET + backgroundColor + " " + getPieceFormating(piece, hasPossibleMove) + " " + ANSI_RESET);
    }

    private static String getPieceFormating(ChessPiece piece, boolean isPossibleMove) {

        String fontColor = piece.getColor() == Color.WHITE ? WHITE_PIECES_COLOR : BLACK_PIECES_COLOR;
        fontColor = isPossibleMove ? RED_UNDERLINED : fontColor;

        return fontColor + getChessPieceUnicode(piece);
    }


    private static String getChessPieceUnicode (ChessPiece piece) {

        int unicode = 0x265A;

        switch (piece.getType()) {
            case "King":
                break;
            case "Queen":
                unicode += 1;
                break;
            case "Rook":
                unicode += 2;
                break;
            case "Bishop":
                unicode += 3;
                break;
            case "Knight":
                unicode += 4;
                break;
            case "Pawn":
                unicode += 5;
                break;
        }

        return String.valueOf((char) unicode);
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
